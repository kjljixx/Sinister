package dev.frozenmilk.sinister.targeting

/**
 * Searches ALL classes, this is not recommend, and usually a [WideSearch] should be used
 */
open class FullSearch : SearchTarget(Inclusion.INCLUDE)