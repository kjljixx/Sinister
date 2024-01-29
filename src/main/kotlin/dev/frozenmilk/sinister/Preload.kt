package dev.frozenmilk.sinister

import java.lang.annotation.Inherited

/**
 * causes a class to be loaded if it is found by [Sinister], all [SinisterFilter]s already have this applied
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Inherited
annotation class Preload
