pluginManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        gradlePluginPortal()
    }
}

rootProject.name = "dnsapi-server-aliyun"

include("dnsapi-server")
include("ddns-cron")