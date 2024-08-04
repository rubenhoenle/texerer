package com.github.rubenhoenle.document

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.io.File
import java.util.*

@ApplicationScoped
class DocumentRenderer {
    @ConfigProperty(name = "documents.location")
    var documentDirectory: String? = null

    // TODO: remove
    @ConfigProperty(name = "templates.location")
    var templatesDirectory: String? = null

    companion object {
        const val FILE_ENDING_PDF: String = ".pdf"
    }

    @PostConstruct
    fun init() {
        val documentDirectoryPath = File(documentDirectory)
        documentDirectoryPath.mkdirs()
    }

    suspend fun renderDocumentBlocking(): String = runBlocking {
        val fileUUID: UUID = UUID.randomUUID()
        renderDocument(fileUUID)
        return@runBlocking fileUUID.toString()
    }

    suspend fun renderDocument(): String = coroutineScope {
        val fileUUID: UUID = UUID.randomUUID()
        launch {
            renderDocument(fileUUID)
        }
        return@coroutineScope fileUUID.toString()
    }

    private fun renderDocument(uuid: UUID) {
        val fileName = "$uuid.pdf"
        val renderedDocument = File(documentDirectory + File.separator + fileName)
        println(renderedDocument.absolutePath)
        if(renderedDocument.exists()) {
            throw Exception("File with this uuid already exists")
        }
        renderedDocument.createNewFile()

        val process = ProcessBuilder("pdflatex", "-output-directory=" + File(documentDirectory).absolutePath, "-jobname=$uuid",
            File(templatesDirectory).absolutePath + "/hello-world/main.tex"
        ).redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor()
    }

    fun getDocument(uuid: String) : File {
        return File(documentDirectory + File.separator + uuid + FILE_ENDING_PDF)
    }
}