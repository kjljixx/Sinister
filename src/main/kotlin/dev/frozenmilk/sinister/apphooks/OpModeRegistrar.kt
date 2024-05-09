package dev.frozenmilk.sinister.apphooks

import com.qualcomm.robotcore.eventloop.opmode.AnnotatedOpModeManager
import dev.frozenmilk.sinister.Preload

/**
 * a more type-safe version of [com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar]
 *
 * static implementations of this class will be run as [com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar] methods are
 */
@Preload
@FunctionalInterface
@JvmDefaultWithCompatibility
fun interface OpModeRegistrar {
	fun registerOpModes(opModeManager: AnnotatedOpModeManager)
	fun registerInstance(): OpModeRegistrar {
		OpModeRegistrarFilter.register(this)
		return this
	}
}

@Suppress("unused")
object OpModeRegistrarFilter : HookFilter<OpModeRegistrar>(OpModeRegistrar::class.java) {
	@JvmStatic
	@com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar
	fun registerOpModes(opModeManager: AnnotatedOpModeManager) {
		allHooks.forEach { it.registerOpModes(opModeManager) }
	}
}