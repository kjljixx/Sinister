package dev.frozenmilk.sinister.apphooks

import android.content.Context
import dev.frozenmilk.sinister.Preload

/**
 * a more type-safe version of [org.firstinspires.ftc.ftccommon.external.OnCreate]
 *
 * static implementations of this class will be run as [org.firstinspires.ftc.ftccommon.external.OnCreate] methods are
 */
@Preload
@FunctionalInterface
@JvmDefaultWithCompatibility
fun interface OnCreate {
	fun onCreate(context: Context)
	fun registerInstance(): OnCreate {
		OnCreateFilter.register(this)
		return this
	}
}

@Suppress("unused")
object OnCreateFilter : HookFilter<OnCreate>(OnCreate::class.java) {
	fun onCreate(context: Context) {
		allHooks.forEach { it.onCreate(context) }
	}
}
