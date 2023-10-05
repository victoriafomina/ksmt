import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.support.unzipTo
import java.io.File

fun Project.usePreparedMaxSmtBenchmarkTestData(path: File) =
    usePreparedBenchmarkTestData(path, "maxSmt")

fun Project.maxSmtBenchmarkTestData(name: String, testDataRevision: String) = tasks.register("maxSmtBenchmark-$name") {
    doLast {
        val path = buildDir.resolve("maxSmtBenchmark/$name")
        val downloadTarget = path.resolve("$name.zip")
        val url = "https://github.com/victoriafomina/gen-benchmark/releases/download/$testDataRevision/$name.zip"

        download(url, downloadTarget)

        path.executeIfNotReady("unpack-complete") {
            copy {
                from(zipTree(downloadTarget))
                into(path)
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }
        }

        val testResources = testResourceDir() ?: error("No resource directory found for $name benchmark")
        val testData = testResources.resolve("testData")

        testData.executeIfNotReady("$name-copy-complete") {
            val maxSmtFiles = path.walkTopDown().filter { it.extension == "smt2" || it.extension == "maxsmt" }.toList()
            copy {
                from(maxSmtFiles.toTypedArray())
                into(testData)
                rename { "${name}_$it" }
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }
        }
    }
}

fun Project.usePreparedMaxSATBenchmarkTestData(path: File): Task =
    usePreparedBenchmarkTestData(path, MAXSAT_BENCHMARK_NAME_L)
        .get()
        .finalizedBy(unzipMaxSATBenchmarkTestFiles())

fun Project.downloadMaxSATBenchmarkTestData(downloadPath: File, testDataPath: File, testDataRevision: String) =
    downloadPreparedBenchmarkTestData(
        downloadPath,
        testDataPath,
        MAXSAT_BENCHMARK_NAME_U,
        "$MAXSAT_BENCHMARK_REPO_URL/releases/download/$testDataRevision/maxsat-benchmark.zip",
    )

fun Project.unzipMaxSATBenchmarkTestFiles() =
    tasks.register("unzip${MAXSAT_BENCHMARK_NAME_U}BenchmarkFiles") {
        doLast {
            val testResources = testResourceDir() ?: error("No resource directory found for benchmarks")
            val testData = testResources.resolve("testData")

            testData.listFiles()?.forEach { if (it.isFile && it.extension == "zip") unzipTo(it.parentFile, it) }
        }
    }

private const val MAXSAT_BENCHMARK_REPO_URL = "https://github.com/victoriafomina/ksmt"

private const val MAXSAT_BENCHMARK_NAME_U = "MaxSat"

private const val MAXSAT_BENCHMARK_NAME_L = "maxSat"