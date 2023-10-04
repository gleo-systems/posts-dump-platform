package pl.gleosys.postsdump.infrastructure

fun String.removeWhitespaceChars() = this.replace("\\s".toRegex(), "")
