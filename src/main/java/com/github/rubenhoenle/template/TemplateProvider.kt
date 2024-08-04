package com.github.rubenhoenle.template

import io.quarkus.runtime.Startup
import io.quarkus.runtime.StartupEvent
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Singleton
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.io.File
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.microprofile.config.ConfigProvider

/*
* author: ruben.hoenle
* date: 03-AUG-2024
* */
@ApplicationScoped
class TemplateProvider {
    private var templates = listOf<Template>()

    @ConfigProperty(name = "templates.location")
    lateinit var templatesDirectoryPath: String

    //@PostConstruct
    //fun onStart(@Observes ev: StartupEvent) {
    fun initOnStartup() {
        println("HELLO IMPORT THE TEMPLATES PLEASE")
        println(templatesDirectoryPath)
        templates = getImportTemplatesConfiguration()
        cleanupTemplateDirectory()
        templates.forEach { cloneGitTemplate(it) }
    }

    fun getTemplates(): List<Template> {
        return templates
    }

    fun getTemplatesDirectory(): File {
        return File(templatesDirectoryPath)
    }

    private fun cleanupTemplateDirectory() {
        val directory = File(templatesDirectoryPath)
        directory.mkdirs()
        directory.deleteRecursively()
        directory.mkdirs()
    }

    private fun cloneGitTemplate(template: Template) {
        val localPath = templatesDirectoryPath + "/" + template.id
        return try {
            println(template.gitUrl)
            println(File(localPath).absolutePath)
            Git.cloneRepository()
                .setURI(template.gitUrl)
                .setDirectory(File(localPath))
                .call()

            println("Repository cloned successfully to $localPath")
        } catch (e: GitAPIException) {
            println("Exception occurred while cloning repository: ${e.message}")
            throw e
        }
    }

    private fun getImportTemplatesConfiguration(): List<Template> {
        val config = ConfigProvider.getConfig()
        val templates = mutableListOf<Template>()

        // Find all property names with the prefix "templates.import"
        val templateNames = config.propertyNames
            .filter { it.startsWith("templates.import") }
            .map { it.substring("templates.import.".length) }
            .map { it.split(".")[0] }
            .toSet()

        // Group properties by template name and create Template objects
        templateNames.forEach { templateName ->
            val descriptionKey = "templates.import.$templateName.description"
            val urlKey = "templates.import.$templateName.url"
            val texfileKey = "templates.import.$templateName.texfile"

            val description = config.getValue(descriptionKey, String::class.java)
            val url = config.getValue(urlKey, String::class.java)
            val texfile = config.getValue(texfileKey, String::class.java)

            templates.add(Template(templateName, description, url, texfile))
        }

        return templates
    }
}