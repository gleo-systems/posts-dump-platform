package pl.gleosys.postsdump.core

fun String.removeWhitespaceChars() = this.replace("\\s".toRegex(), "")
