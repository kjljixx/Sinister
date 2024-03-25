import dev.frozenmilk.sinister.NoPreloadException
import dev.frozenmilk.sinister.Preload
import dev.frozenmilk.sinister.apphooks.CollectImplementationsFilter
import dev.frozenmilk.sinister.apphooks.HookFilter
import dev.frozenmilk.sinister.apphooks.OnCreateEventLoopFilter
import dev.frozenmilk.sinister.preload
import org.junit.Assert
import org.junit.Test

class Preloading {
	@Test(expected = NoPreloadException::class)
	fun preloadFails() {
		NoPreloadObject::class.java.preload()
		Assert.fail()
	}

	@Test
	fun preloadObject() {
		PreLoadObject::class.java.preload()
	}

	@Test
	fun preloadImplementation() {
		PreloadImplementation::class.java.preload()
	}

	@Test
	fun preloadComplex() {
		CollectImplementationsFilter::class.java.preload()
		HookFilter::class.java.preload()
		OnCreateEventLoopFilter::class.java.preload()
	}
}

private object NoPreloadObject

@Preload
private object PreLoadObject

@Preload
private interface PreloadInterface

private object PreloadImplementation : PreloadInterface