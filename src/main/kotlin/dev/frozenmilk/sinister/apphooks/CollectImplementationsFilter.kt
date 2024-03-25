package dev.frozenmilk.sinister.apphooks

import com.qualcomm.robotcore.util.RobotLog
import dev.frozenmilk.sinister.SinisterFilter
import dev.frozenmilk.sinister.staticInstancesOf
import dev.frozenmilk.sinister.targeting.NarrowSearch

abstract class CollectImplementationsFilter<T: Any>(private val clazz: Class<T>) : SinisterFilter {
	protected open var tag = this::class.java.simpleName
	override val targets = NarrowSearch()
	protected val found = mutableSetOf<T>()
	override fun init() {
		found.clear()
	}
	override fun filter(clazz: Class<*>) {
		clazz.staticInstancesOf(this.clazz)
				.forEach {
					found.add(it)
					RobotLog.vv(tag, "found implementing instance: ${it::class.java.simpleName}")
				}
	}
}