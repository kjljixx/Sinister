@file:Suppress("DEPRECATION")

package dev.frozenmilk.sinister

import android.content.Context
import com.qualcomm.robotcore.util.RobotLog
import dalvik.system.DexFile
import dev.frozenmilk.sinister.apphooks.OnCreateFilter
import dev.frozenmilk.sinister.targeting.FullSearch
import dev.frozenmilk.util.cell.LateInitCell
import org.firstinspires.ftc.ftccommon.external.OnCreate

private object Sinister {
	private var dexFile by LateInitCell<DexFile>()
	private var context by LateInitCell<Context>()
	private val loader = this::class.java.classLoader
	private val rootSearch = FullSearch()
	private var run = false
	private const val TAG = "Sinister"

	@OnCreate
	@JvmStatic
	@Suppress("unused")
	fun onCreate(context: Context) {
		RobotLog.vv(TAG, "attempting boot on create")
		if (run) {
			OnCreateFilter.onCreate(context)
			return
		}
		RobotLog.vv(TAG, "not yet booted, booting")
		this.context = context
		dexFile = DexFile(context.packageCodePath)
		selfBoot()
		run = true
		dexFile.close()
		OnCreateFilter.onCreate(context)
		RobotLog.vv(TAG, "finished boot process")
	}

	private fun allClassNames(): List<String> {
		return dexFile.entries().toList()
	}

	private fun allClasses(): List<Pair<Class<*>, String>> {
		return allClassNames().mapNotNull {
			if (!rootSearch.determineInclusion(it)) return@mapNotNull null

			try {
				return@mapNotNull Class.forName(it, false, loader) to it
			}
			catch (e: Throwable) {
				RobotLog.ee(TAG, "Error occurred while locating class: $e")
			}

			rootSearch.exclude(it)

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
					catch (_: Throwable) {
						null
					}
				}

		val filters = preloaded
				.flatMap {
					it.first.staticInstancesOf(SinisterFilter::class.java)
				}

		filters.forEach {
			RobotLog.vv(TAG, "found filter ${it.javaClass.simpleName}")
			it.init()
		}

		RobotLog.vv(TAG, "running filters")
		allClasses.forEach {
			filters.forEach { filter ->
				try {
					 if (filter.targets.determineInclusion(it.second)) {
						 filter.filter(it.first)
					 }
				}
				catch (e: Throwable) {
					RobotLog.ee(TAG, "Error occurred while running SinisterFilter: ${filter::class.simpleName} | ${filter}\nFiltering Class:${it.second}\nError: $e\nStackTrace: ${e.stackTraceToString()}")
				}
			}
		}
		RobotLog.vv(TAG, "...booted")
	}
}

