package de.nycode.mcserver.api

class Identifier {

    private val pattern = "([a-z0-9_-])+".toRegex()
    private val doublePattern = "([a-z0-9_-])+:([a-z0-9_-])+".toRegex()

    var namespace: String
        private set
    var value: String
        private set

    constructor(namespace: String, value: String) {
        if (!namespace.matches(pattern)) {
            throw IllegalArgumentException("Invalid namespace")
        }
        if (!value.matches(pattern)) {
            throw IllegalArgumentException("Invalid value")
        }

        this.namespace = namespace
        this.value = value
    }

    constructor(string: String) {
        if (string.matches(doublePattern)) {
            val split = string.split(":").toTypedArray()
            namespace = split[0]
            value = split[1]
        } else {
            namespace = "minecraft"
            value = string
        }
    }
}
