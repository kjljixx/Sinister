@file:JvmName("SinisterUtil")
package dev.frozenmilk.sinister

import java.lang.reflect.Field
import java.lang.reflect.Member
import java.lang.reflect.Method
import java.lang.reflect.Modifier

@JvmOverloads
fun Class<*>.getAllMethods(predicate: (Method) -> Boolean = { true } ): List<Method> {
	return this.declaredMethods.toList().filter(predicate) + (this.superclass?.getAllMethods(predicate) ?: emptyList())
}

@JvmOverloads
fun Class<*>.getAllFields(predicate: (Field) -> Boolean = { true } ): List<Field> {
	return this.declaredFields.toList().filter(predicate) + (this.superclass?.getAllFields(predicate) ?: emptyList())
}

fun Member.isInterface() : Boolean = Modifier.isInterface(modifiers)
fun Member.isNative() : Boolean = Modifier.isNative(modifiers)
fun Member.isSynchronized() : Boolean = Modifier.isSynchronized(modifiers)
fun Member.isTransient() : Boolean = Modifier.isTransient(modifiers)
fun Member.isVolatile() : Boolean = Modifier.isVolatile(modifiers)
fun Member.isStatic() : Boolean = Modifier.isStatic(modifiers)
fun Member.isPublic() : Boolean = Modifier.isPublic(modifiers)
fun Member.isPrivate() : Boolean = Modifier.isPrivate(modifiers)
fun Member.isProtected() : Boolean = Modifier.isProtected(modifiers)
fun Member.isAbstract() : Boolean = Modifier.isAbstract(modifiers)
fun Member.isFinal() : Boolean = Modifier.isFinal(modifiers)

/**
 * used to find all [SinisterFilter]s, will give access to all loaded instances of a preloaded class, which is useful for kotlin objects, and similar java singletons
 */
fun <T> Class<*>.staticInstancesOf(type: Class<T>) : List<T> =
	this.getAllFields {
		it.isStatic() && type.isAssignableFrom(it.type)
	}
	.mapNotNull {
		it.isAccessible = true
		@Suppress("UNCHECKED_CAST")
		it.get(this) as? T
	}

@Throws(NoLoaderException::class)
fun Class<*>.load() = classLoader?.loadClass(this.name) ?: throw NoLoaderException()

@Throws(NoLoaderException::class, NoPreloadException::class)
fun Class<*>.preload() = if (!inheritsAnnotation(Preload::class.java)) throw NoPreloadException() else load()

class NoLoaderException : Exception("Tried to load a class with no class loader")

class NoPreloadException : Exception("Tried to load a class that isn't allowed to be preloaded")

fun Class<*>.inheritsAnnotation(annotation: Class<out Annotation>): Boolean = if (isAnnotationPresent(annotation)) true else interfaces.any { it.inheritsAnnotation(annotation) } || (superclass?.inheritsAnnotation(annotation) ?: false)

