import org.gradle.plugins.ide.idea.model.IdeaModule
import org.jetbrains.gradle.ext.ModuleSettings
import org.jetbrains.gradle.ext.PackagePrefixContainer

plugins {
    kotlin("jvm")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.0.1"
}

fun IdeaModule.settings(block: ModuleSettings.() -> Unit) = (this as ExtensionAware).extensions.configure(block)

val ModuleSettings.packagePrefix: PackagePrefixContainer get() = (this as ExtensionAware).extensions["packagePrefix"] as PackagePrefixContainer

idea.module.settings {
    sourceSets.all {
        packagePrefix["src/$name/kotlin"] = "com.bitwiserain.remindme.core"
    }

}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
}
