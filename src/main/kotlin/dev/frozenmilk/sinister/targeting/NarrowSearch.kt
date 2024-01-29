package dev.frozenmilk.sinister.targeting

/**
 * a [WideSearch] that also excludes the SDK
 */
open class NarrowSearch : WideSearch() {
	init {
		exclude("dev.frozenmilk")
	}
}