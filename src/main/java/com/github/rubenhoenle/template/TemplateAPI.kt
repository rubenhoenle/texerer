package com.github.rubenhoenle.template;

import jakarta.inject.Inject
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/*
* author: ruben.hoenle
* date: 03-AUG-2024
* */
@Path("/template")
class TemplateAPI() {
    @Inject
    private lateinit var templateProvider : TemplateProvider

    @GET
    fun listTemplates(): Map<String, Template> {
        return templateProvider.getTemplates()
    }
}
