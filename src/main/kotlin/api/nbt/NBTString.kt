package de.nycode.mcserver.api.nbt

class NBTString(value: String) : NBTTag<String>(value), Cloneable {

    override val type: NBTType = NBTType.STRING

    override fun toMSONString(): String {
        return toMSONString(this.value)
    }

    override fun clone(): Any {
        return NBTString(value)
    }

    companion object {
        /**
         * Converts a regular string into a Mojangson string by surrounding it with quotes and escaping backslashes and
         * quotes inside it.
         *
         * @param str the string
         * @return the Mojangson string
         */
        fun toMSONString(str: String): String {
            val builder = StringBuilder("\"")
            val chars = str.toCharArray()
            for (c in chars) {
                if (c == '\\' || c == '"') {
                    builder.append('\\')
                }
                builder.append(c)
            }
            return builder.append('\"').toString()
        }
    }
}
