package com.github.rubenhoenle.beans

import com.github.rubenhoenle.template.TemplateProvider
import io.quarkus.runtime.Startup
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@Startup
@ApplicationScoped
class StartupBean {
    @Inject
    private lateinit var templateProvider : TemplateProvider

    @PostConstruct
    fun onStart() {
        templateProvider.initOnStartup()
    }
}