package com.github.rubenhoenle;

import jakarta.inject.Inject
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.ResponseBuilder
import java.io.File

@Path("/document")
class Document() {
    @Inject
    private var documentRenderer : DocumentRenderer? = null

    @GET
    @Path("/render")
    @Produces(MediaType.TEXT_PLAIN)
    fun renderDocument(): String {
        return "Hello from Quarkus REST " + documentRenderer!!.renderDocument()
    }

    @GET
    @Path("/instant")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    fun renderAndDownloadDocument(): Response {
        var uuid = documentRenderer!!.renderDocument()
        return downloadDocument(uuid)
    }

    @GET
    @Path("/download/{file}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    fun downloadDocument(@PathParam("file") file : String) : Response {
        val fileDownload : File = documentRenderer!!.getDocument(file)
        val response : ResponseBuilder = Response.ok(fileDownload as Any?)
        response.header("Content-Disposition", "attachment;filename=" + file)
        return response.build()
    }
}
