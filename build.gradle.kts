plugins {
    kotlin("jvm") version "1.8.0"
}

group = "org.odencave"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    api("com.badlogicgames.gdx:gdx:1.11.0")
    api("com.badlogicgames.gdx:gdx-ai:1.8.0")
    api("com.badlogicgames.gdx-controllers:gdx-controllers-core:2.2.2")
    api("com.badlogicgames.gdx:gdx-freetype:$1.11.0")
   // api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    // (bKtx libs)
    api("io.github.libktx:ktx-app:1.11.0-rc3")
    api("io.github.libktx:ktx-log:1.11.0-rc3")
    api("io.github.libktx:ktx-graphics:1.11.0-rc3")
    api("io.github.libktx:ktx-math:1.11.0-rc3")
    api("io.github.libktx:ktx-async:1.11.0-rc3")
    api("io.github.libktx:ktx-actors:1.11.0-rc3")
    api("io.github.libktx:ktx-collections:1.11.0-rc3")
    api("io.github.libktx:ktx-freetype:1.11.0-rc3")
    api("io.github.libktx:ktx-inject:1.11.0-rc3")
    api("io.github.libktx:ktx-i18n:1.11.0-rc3")


    api("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.11.0")
    api("com.badlogicgames.gdx:gdx-platform:1.11.0:natives-desktop")
    api("com.badlogicgames.gdx-controllers:gdx-controllers-desktop:2.2.2")
    api("com.badlogicgames.gdx:gdx-freetype-platform:1.11.0:natives-desktop")


    //api('org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2')
    api("org.reflections:reflections:0.9.12")
    api("org.jetbrains.kotlin:kotlin-reflect:1.3.72")
}

tasks.test {
    useJUnitPlatform()
}

task("nls") {
    val project = "./"             // Will contain generated enum class.
    var source = "src"   // Kotlin source path of the project.
    var pack = "com.odencave.i18n"    // Enum target package.
    val name = "Nls"                 // Enum class name.
    val fileName = "nls.kt"          // Name of Kotlin file containing the enum.
    val bundle = "./src/main/resources/nls.properties" // Path to i18n bundle file.

    println("Processing i18n bundle file at ${bundle}...")
    val builder = StringBuilder()
    builder.append("""package ${pack}
import ktx.i18n.BundleLine
/** Generated from ${bundle} file. */
enum class ${name} : BundleLine {
""")
    val newLine = System.getProperty("line.separator")
    file(bundle).forEachLine {
        val data = it.trim()
        val separator = data.indexOf('=')
        if (!data.isEmpty() && separator > 0 && !data.startsWith('#')) {
            val id = data.substring(0, separator)
            builder.append("    ").append("/** ${data.substring(separator+1)} **/").append(newLine).append("    ").append(id).append(',').append(newLine)
        }
    }
    // If you want a custom enum body, replace the following append:
    builder.append("    ;").append(newLine).append("}").append(newLine)

    source = source.replace("/", File.separator)
    pack = pack.replace(".", File.separator)
    val path = project + File.separator + source + File.separator + "main/kotlin/generated" +
            File.separator + fileName
    println("Saving i18n bundle enum at ${path}...")
    val enumFile = file(path)
    enumFile.delete()
    enumFile.parentFile.mkdirs()
    enumFile.createNewFile()
    enumFile.writeText(builder.toString())
    enumFile.appendText(newLine)
    println("Done. I18n bundle enum generated.")
}

kotlin {
    jvmToolchain(11)
}