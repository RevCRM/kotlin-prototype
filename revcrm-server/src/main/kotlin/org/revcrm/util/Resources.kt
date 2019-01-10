package org.revcrm.util

fun getResourceAsText(path: String): String {
    val resource = object {}.javaClass.getResource(path)
    if (resource == null)
        throw Error("Resource '$path' not found.")
    return resource.readText()
}
