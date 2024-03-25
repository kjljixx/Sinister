package dev.frozenmilk.sinister

import dev.frozenmilk.sinister.targeting.SearchTarget

/**
 * static implementations of this will get invoked at runtime
 */
@Preload
@JvmDefaultWithCompatibility
interface SinisterFilter {
	/**
	 * items that should be ignored
	 */
	val targets: SearchTarget
	fun init()
	fun filter(clazz: Class<*>)
}