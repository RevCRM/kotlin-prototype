package org.revcrm.util

import java.util.Random

fun randomString(length: Int): String {
    val leftLimit = 33
    val rightLimit = 126
    val random = Random()
    val buffer = StringBuilder(length)
    for (i in 0 until length) {
        val randomLimitedInt = leftLimit + (random.nextFloat() * (rightLimit - leftLimit + 1)).toInt()
        buffer.append(randomLimitedInt.toChar())
    }
    return buffer.toString()
}