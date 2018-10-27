package org.revcrm.util

fun getResourceAsText(path: String): String {
    return object {}.javaClass.getResource(path).readText()
}
