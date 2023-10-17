package pl.gleosys.postsdump.util

fun String.removeWhitespaceChars() = this.replace("\\s".toRegex(), "")
