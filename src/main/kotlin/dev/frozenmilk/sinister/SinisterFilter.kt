package dev.frozenmilk.sinister

import dev.frozenmilk.sinister.targeting.SearchTarget

/**
 * static implementations of this will get invoked at runtime
 */
@Preload
interface SinisterFilter {
	/**
	 * items that should be ignored
	 */
	val targets: SearchTarget

	fun filter(clazz: Class<*>)
}