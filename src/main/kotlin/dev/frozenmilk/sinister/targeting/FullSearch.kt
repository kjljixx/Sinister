package dev.frozenmilk.sinister.targeting

/**
 * Searches ALL classes (other than those that cannot be found in the compiled app), this is not recommend, and usually a [WideSearch] should be used
 */
open class FullSearch : SearchTarget(Inclusion.INCLUDE) {
	init {
		exclude("androidx.core.app.AppComponentFactory")
		exclude("androidx.core.app.CoreComponentFactory")
		exclude("androidx.core.app.JobIntentService\$JobServiceEngineImpl")
		exclude("androidx.core.view.ViewCompat\$2")
		exclude("com.android.tools.r8")
		exclude("com.journeyapps.barcodescanner.ScanContract")
		exclude("com.qualcomm.robotcore.wifi.WifiDirectAssistantAndroid10Extensions\$1")
		exclude("com.sun.tools.javac.model.AnnotationProxyMaker\$MirroredTypeExceptionProxy")
		exclude("com.sun.tools.javac.model.AnnotationProxyMaker\$MirroredTypesExceptionProxy")
		exclude("com.sun.tools.javac.model.AnnotationProxyMaker\$ValueVisitor\$1AnnotationTypeMismatchExceptionProxy")
		exclude("com.sun.tools.javac.nio.JavacPathFileManager\$1")
		exclude("java.nio.file.Path")
		exclude("java.nio.file.LinkOption")
		exclude("kotlin.io.path.DirectoryEntriesReader")
		exclude("kotlin.io.path.ExceptionsCollector")
		exclude("kotlin.io.path.LinkFollowing")
		exclude("kotlin.io.path.PathNode")
		exclude("kotlin.io.path.PathRelativizer")
		exclude("kotlin.io.path.PathTreeWalk")
		exclude("kotlin.io.path.PathsKt__PathRecursiveFunctionsKt\$copyToRecursively\$5")
		exclude("kotlin.io.path.PathsKt__PathRecursiveFunctionsKt\$copyToRecursively\$5\$1")
		exclude("kotlin.io.path.PathsKt__PathRecursiveFunctionsKt\$copyToRecursively\$5\$2")
		exclude("kotlin.io.path.PathsKt__PathRecursiveFunctionsKt\$copyToRecursively\$5\$3")
		exclude("kotlin.io.path.PathsKt__PathRecursiveFunctionsKt\$copyToRecursively\$5\$4")
		exclude("kotlin.io.path.FileVisitorImpl")
		exclude("org.openftc.easyopencv.OpenCvVuforiaPassthroughImpl")
		exclude("org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer\$Parameters")
	}
}