package dev.frozenmilk.sinister

import dev.frozenmilk.sinister.targeting.SearchTarget

/**
 * static implementations of this will get invoked at runtime
 *
 * WARNING: all classes that implement [SinisterFilter] are [Preload]ed by [Sinister], this should most likely not cause issues
 */
@Preload
@JvmDefaultWithCompatibility
interface SinisterFilter {
	/**
	 * items that should be ignored
	 */
	val targets: SearchTarget

	/**
	 * gets run before any calls to [filter]
	 */
	fun init() {}

	/**
	 * gets run for all classes as accepted by [targets]
	 *
	 * this method can be run in parallel with other instances of [SinisterFilter], but only one [filter] for an instance can run at once.
	 *
	 * this helps to cut down on boot time, but also should prevent most synchronisation issues from appearing
	 */
	fun filter(clazz: Class<*>)
}