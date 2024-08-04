package com.github.rubenhoenle.template

import com.github.rubenhoenle.template.placeholder.TemplatePlaceholder
import java.io.File

/*
* The template class contains the information about a Latex template (from where it was imported, which placeholders it has, etc.)
*
* author: ruben.hoenle
* date: 03-AUG-2024
* */
class Template(val description: String = "", val gitUrl: String, val texFile: String = "main.tex") {
    private val placeholders: MutableList<TemplatePlaceholder> = mutableListOf()

    private fun extractPlaceholders() {
        TODO()
    }

    fun getPlaceholders(): List<TemplatePlaceholder> {
        return placeholders
    }
}