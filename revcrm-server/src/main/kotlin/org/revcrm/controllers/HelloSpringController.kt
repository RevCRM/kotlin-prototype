package org.revcrm.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloSpringController {

    @GetMapping("/test")
    fun hello(): String {
        return "Hello Spring!"
    }
}