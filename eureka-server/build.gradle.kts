dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}

springBoot {
    buildInfo()
}

tasks.bootRun {
    systemProperty("dd.service.name", "eureka-server")
    systemProperty("dd.env", "development")
    systemProperty("dd.version", "1.0.0")
    systemProperty("dd.appsec.enabled", "false")
    systemProperty("dd.remote_config.enabled", "false")
}