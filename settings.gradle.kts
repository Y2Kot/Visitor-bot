enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven(url = "https://maven.nexus.qoollo.com/")
    }
}
rootProject.name = "Automation-bots"
include(
    ":studentvisitorbot",
    ":studentsyndicatebot",
    ":photocutter",
    ":photocutter:desktop",
    ":common",
)
