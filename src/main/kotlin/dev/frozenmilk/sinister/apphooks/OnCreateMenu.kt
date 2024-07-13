package dev.frozenmilk.sinister.apphooks

import android.content.Context
import android.view.Menu
import dev.frozenmilk.sinister.Preload

/**
 * a more type-safe version of [org.firstinspires.ftc.ftccommon.external.OnCreateMenu]
 *
 * static implementations of this class will be run as [org.firstinspires.ftc.ftccommon.external.OnCreateMenu] methods are
 */
@Preload
@FunctionalInterface
@JvmDefaultWithCompatibility
fun interface OnCreateMenu {
	fun onCreateMenu(context: Context, menu: Menu)
}

@Suppress("unused")
object OnCreateMenuFilter : HookFilter<OnCreateMenu>(OnCreateMenu::class.java) {
	@JvmStatic
	@org.firstinspires.ftc.ftccommon.external.OnCreateMenu
	fun onCreateMenu(context: Context, menu: Menu) {
		allHooks.forEach { it.onCreateMenu(context, menu) }
	}
}