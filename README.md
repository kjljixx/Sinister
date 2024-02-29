# Sinister
Bootstrapping class filtering library for FTC
- Extension utilities written in Kotlin, making the Kotlin API super easy to use
- Easy to use filtering system to reduce the footprint of processors
- Type safe system that prefers static instances of classes or interfaces over annotated methods
- Self-discovery process means that filters are scanned for by the library itself
- Easy to use pre-loading system via the `@Preload` annotation

NOTE:

The extension utils are compiled as static methods to a class called `SinisterUtil` for use in Java.

## Usage
Using Sinister is super easy, just implement and make an instance of [`SinisterFilter`](src/main/kotlin/dev/frozenmilk/sinister/Sinister.kt), and Sinister will call the processing method according to the set filter.

### Java
1. Create a class that implements [`SinisterFilter`](src/main/kotlin/dev/frozenmilk/sinister/Sinister.kt).
2. Create a static instance of the class (`private static final FilterImpl instance = new FilterImpl();`).
3. Supply a filter from among the presets, and customise it to your needs.
4. Fill in the filtering method.

For example, this implementation scans the TeamCode module for implementations of OpModes, and 'records' their names.
```java
final class JavaOpModeFilter implements SinisterFilter {
	private JavaOpModeFilter() {}
	private static final JavaOpModeFilter INSTANCE = new JavaOpModeFilter();
	// we only want to search the contents of the TeamCode module
	private static final SearchTarget SEARCH_TARGET = new TeamCodeSearch();
	@NonNull
	@Override
	public SearchTarget getTargets() {
		return SEARCH_TARGET;
	}

	@Override
	public void filter(@NonNull Class<?> clazz) {
		if (!clazz.isInstance(OpMode.class)) return;
		boolean auto = clazz.isAnnotationPresent(Autonomous.class);
		boolean teleop = clazz.isAnnotationPresent(TeleOp.class);
		if (!(auto || teleop)) return;
		if (auto && teleop) throw new IllegalStateException("OpMode has both @Autonomous and @TeleOp annotations, it may only have one.");
		String name = "";
		if (auto) name = Objects.requireNonNull(clazz.getAnnotation(Autonomous.class)).name();
		if (teleop) name = Objects.requireNonNull(clazz.getAnnotation(TeleOp.class)).name();
		RobotLog.vv("OpMode Filter", "Registered { " + clazz.getSimpleName() + " } as { " + name + " }" );
	}
}
```

And this example uses SinisterUtil to filter for methods, ensuring to grab methods also defined on super classes (which probably isn't desirable here, but a good demonstration)
```java
final class JavaOnCreateFilter implements SinisterFilter {
	private JavaOnCreateFilter() {}
	private static final JavaOnCreateFilter INSTANCE = new JavaOnCreateFilter();
	// Search everything except for the SDK's default search exclusions
	private static final SearchTarget SEARCH_TARGET = new WideSearch();
	@NonNull
	@Override
	public SearchTarget getTargets() {
		return SEARCH_TARGET;
	}

	@Override
	public void filter(@NonNull Class<?> clazz) {
		// We actually only want declared methods, but this demonstrates the utility method
		List<Method> methods = SinisterUtil.getAllMethods(clazz);
		methods.forEach(method -> {
			if (method.isAnnotationPresent(OnCreate.class) && SinisterUtil.isStatic(method)) {
				method.setAccessible(true);
				try {
					// yes a context should be passed here, but this is easy for demonstration
					method.invoke(null);
				} catch (IllegalAccessException | InvocationTargetException e) {
					RobotLog.ww("@OnCreate Filter", "Error occurred while running @OnCreate method: " + Arrays.toString(e.getStackTrace()));
				}
			}
		});
	}
}
```

### Kotlin
1. Create an `object` that implements [`SinisterFilter`](src/main/kotlin/dev/frozenmilk/sinister/Sinister.kt).
2. Supply a filter from among the presets, and customise it to your needs.
3. Fill in the filtering method.

The `object` keyword means that a public final instance of this class is automatically made. Kotlin abstracts this away, but it still exists, and gets picked up by Sinister itself, when it bootstraps.

For example, this implementation scans TeamCode and other libraries for static implementations of `Subsytem`, like this is a static implementation of `SinisterFilter`, and registers them with the MercurialScheduler.
```kt
private object SubsystemSinisterFilter : SinisterFilter {
	// this search also excludes the SDK, narrower than the WideSearch
	override val targets = NarrowSearch()

	override fun filter(clazz: Class<*>) {
		clazz.staticInstancesOf(Subsystem::class.java)
				.forEach {
					Mercurial.registerSubsystem(it)
				}
	}
}
```

## Installation
[![](https://www.jitpack.io/v/Dairy-Foundation/Dairy.svg)](https://www.jitpack.io/#Dairy-Foundation/Dairy)

Sinister only depends upon the common Util library of Dairy **NOT CORE**, which itself is dependency free, and is other than that dependency free.

1. add `maven { url 'https://www.jitpack.io' }` to the bottom of your `repositories` block in your `build.dependencies.gradle`
2. add `implementation 'com.github.Dairy-Foundation.Dairy:Sinister:<version tag>'` to the bottom of your `dependencies` block in your `build.dependencies.gradle`
3. run a gradle sync

