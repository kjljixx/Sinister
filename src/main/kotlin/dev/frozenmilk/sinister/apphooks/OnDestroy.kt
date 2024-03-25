package dev.frozenmilk.sinister.apphooks

import android.content.Context
import dev.frozenmilk.sinister.Preload

/**
 * a more type-safe version of [org.firstinspires.ftc.ftccommon.external.OnDestroy]
 *
 * static implementations of this class will be run as [org.firstinspires.ftc.ftccommon.external.OnDestroy] methods are
 */
@Preload
@FunctionalInterface
@JvmDefaultWithCompatibility
fun interface OnDestroy {
	fun onDestroy(context: Context)
	fun registerInstance(): OnDestroy {
		OnDestroyFilter.register(this)
		return this
	}
}

@Suppress("unused")
object OnDestroyFilter : HookFilter<OnDestroy>(OnDestroy::class.java) {
	@JvmStatic
	@org.firstinspires.ftc.ftccommon.external.OnDestroy
	fun onDestroy(context: Context) {
		allHooks.forEach { it.onDestroy(context) }
	}
}