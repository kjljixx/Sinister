@file:Suppress("DEPRECATION")

package dev.frozenmilk.sinister

import android.content.Context
import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.util.RobotLog
import dalvik.system.DexFile
import dev.frozenmilk.sinister.targeting.FullSearch
import dev.frozenmilk.util.cell.LateInitCell
import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop
import org.firstinspires.ftc.robotcore.internal.opmode.InstantRunHelper

private object Sinister {
	private var dexFile by LateInitCell<DexFile>()
	private var context by LateInitCell<Context>()
	private val loader = this::class.java.classLoader
	private val rootSearch = FullSearch()
	private var run = false
	private const val TAG = "Sinister"

	@OnCreateEventLoop
	@JvmStatic
	@Suppress("UNUSED_PARAMETER")
	fun onCreate( context: Context, ftcEventLoop: FtcEventLoop) {
		RobotLog.vv(TAG, "attempting boot on create")
		if (run) return
		RobotLog.vv(TAG, "not yet booted, booting")
		this.context = context
		dexFile = DexFile(context.packageCodePath)
		selfBoot()
		run = true
		dexFile.close()
	}

	private fun allClassNames(): List<String> {
		return dexFile.entries().toList()// + InstantRunHelper.getAllClassNames(context)
	}

	private fun allClasses(): List<Pair<Class<*>, String>> {
		return allClassNames().mapNotNull {
			if (!rootSearch.determineInclusion(it)) return@mapNotNull null

			try {
				return@mapNotNull Class.forName(it, false, loader) to it
			}
			catch (_: ClassNotFoundException) {}
			catch (_: NoClassDefFoundError) {}

			var className = it
			if (className.contains('$')) {
				className = className.substring(0, className.indexOf("$") /* -1 */);
			}
			rootSearch.exclude(className)

			null
		}
	}

	private fun selfBoot() {
		RobotLog.vv(TAG, "self booting...")
		val allClasses = allClasses()

		val preloaded = allClasses
				.mapNotNull {
					try {
						it.first.preload()
						RobotLog.vv(TAG, "preloading: ${it.first.simpleName}")
						return@mapNotNull it
					}
					catch (_: Exception) {
						null
					}
				}

		val filters = preloaded
				.flatMap {
					it.first.staticInstancesOf(SinisterFilter::class.java)
				}

		filters.forEach {
			RobotLog.vv(TAG, "found filter ${it.javaClass.simpleName}")
		}

		RobotLog.vv(TAG, "running filters")
		allClasses.forEach {
			filters.forEach { filter ->
				if (filter.targets.determineInclusion(it.second)) {
					filter.filter(it.first)
				}
			}
		}
		RobotLog.vv(TAG, "...booted")
	}
}

