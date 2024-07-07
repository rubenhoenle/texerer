package com.github.rubenhoenle

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.io.File
import java.util.*

@ApplicationScoped
class DocumentRenderer {
    @ConfigProperty(name = "documents.location")
    var documentDirectory: String? = null

    @ConfigProperty(name = "templates.location")
    var templatesDirectory: String? = null

    companion object {
        const val FILE_ENDING_PDF: String = ".pdf"
    }

    @PostConstruct
    fun init() {
        val documentDirectoryPath : File = File(documentDirectory)
        documentDirectoryPath.mkdirs()
    }

    fun renderDocument(): String {
        val fileUUID: UUID = UUID.randomUUID()
        val fileName = "$fileUUID.pdf"
        val renderedDocument = File(documentDirectory + File.separator + fileName)
        println(renderedDocument.absolutePath)
        renderedDocument.createNewFile()

        val process = ProcessBuilder("pdflatex", "-output-directory=" + File(documentDirectory).absolutePath, "-jobname=$fileUUID",
            File(templatesDirectory).absolutePath + "/main.tex"
        ).redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor()
        return fileUUID.toString()
    }

    fun getDocument(uuid: String) : File {
        return File(documentDirectory + File.separator + uuid + FILE_ENDING_PDF)
    }
}