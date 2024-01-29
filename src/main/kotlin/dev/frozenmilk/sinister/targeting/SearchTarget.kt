package dev.frozenmilk.sinister.targeting

import dev.frozenmilk.util.tree.Tree

/**
 * a class that represents a hierarchical include / exclude search pattern
 *
 * i.e.: if a [SearchTarget] is set up to include targets by default
 * and [exclude] is applied to the package `com.example`, applying [include] to `com.example.Demo` will then result in that class being included
 * ```
 * FullSearch()
 * 	.exclude("com.example")
 * 	.include("com.example.Demo")
 */
abstract class SearchTarget(
		/**
		 * may not be [Inclusion.INHERIT]
		 */
		default: Inclusion
) {
	enum class Inclusion {
		INCLUDE,
		EXCLUDE,
		INHERIT
	}

	private val targets = Tree<String, Inclusion>(default.apply { if (this == Inclusion.INHERIT) throw IllegalArgumentException("Default inclusion status may not be INHERIT") })

	/**
	 * includes this package or class
	 */
	fun include(target: String): SearchTarget {
		setStatus(target, Inclusion.INCLUDE)
		return this
	}

	/**
	 * excludes this package or class
	 */
	fun exclude(target: String): SearchTarget {
		setStatus(target, Inclusion.EXCLUDE)
		return this
	}

	private fun setStatus(target: String, status: Inclusion) {
		var targetPlumb = targets
		target.split('.')
				.forEach {
					targetPlumb.putIfAbsent(it, Inclusion.INHERIT)
					targetPlumb = targetPlumb[it]!!
				}
		targetPlumb.contents = status
	}

	fun determineInclusion(path: String) : Boolean {
		var targetPlumb = targets
		var inclusion = targets.contents
		path.split('.')
				.forEach {
					targetPlumb = targetPlumb[it] ?: return inclusion == Inclusion.INCLUDE
					when (val contents = targetPlumb.contents) {
						Inclusion.INCLUDE,
						Inclusion.EXCLUDE -> {
							inclusion = contents
						}
						Inclusion.INHERIT -> {}
					}
				}
		return inclusion == Inclusion.INCLUDE
	}
}
