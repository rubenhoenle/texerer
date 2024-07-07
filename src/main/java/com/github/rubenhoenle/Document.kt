package com.github.rubenhoenle;

import jakarta.ws.rs.FormParam
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.ResponseBuilder
import java.io.File
import java.util.UUID

@Path("/document")
class Document() {
    val DOCUMENT_DIRECTORY_PATH: String = System.getProperty("user.home") + File.separator + "texerer";

    init {
        val documentDirectoryPath : File = File(DOCUMENT_DIRECTORY_PATH)
        documentDirectoryPath.mkdirs()
    }

    @GET
    @Path("/render")
    @Produces(MediaType.TEXT_PLAIN)
    fun renderDocument(): String {
        val fileName: String = UUID.randomUUID().toString() + ".pdf";
        val renderedDocument : File = File(DOCUMENT_DIRECTORY_PATH + File.separator + fileName);
        renderedDocument.createNewFile();
        renderedDocument.appendText("hello there");
        return "Hello from Quarkus REST " + fileName;
    }

    @GET
    @Path("/download/{file}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    fun downloadDocument(@PathParam("file") file : String) : Response {
        val fileDownload : File = File(DOCUMENT_DIRECTORY_PATH + File.separator + file);
        val response : ResponseBuilder = Response.ok(fileDownload as Any?);
        response.header("Content-Disposition", "attachment;filename=" + file);
        return response.build();
    }
}
