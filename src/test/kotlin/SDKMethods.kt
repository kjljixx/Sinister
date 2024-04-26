import android.content.Context
import dev.frozenmilk.sinister.SinisterFilter
import dev.frozenmilk.sinister.apphooks.OnCreate
import dev.frozenmilk.sinister.apphooks.OnCreateEventLoop
import dev.frozenmilk.sinister.apphooks.OnCreateEventLoopFilter
import dev.frozenmilk.sinister.apphooks.OnDestroy
import dev.frozenmilk.sinister.staticInstancesOf
import org.junit.Assert
import org.junit.Test

class SDKMethods {
	@Test
	fun onCreate() {
		Assert.assertEquals(OnCreateEventLoopFilter::class.java.staticInstancesOf(SinisterFilter::class.java), listOf(OnCreateEventLoopFilter))
	}
}

private object OnCreateImpl : OnCreate {
	override fun onCreate(context: Context) {}
}

private object OnCreateEventLoopImpl : OnCreateEventLoop {
	override fun onCreateEventLoop(context: Context, ftcEventLoop: com.qualcomm.ftccommon.FtcEventLoop) {}
}

private object OnDestroyImpl : OnDestroy {
	override fun onDestroy(context: Context) {}
}