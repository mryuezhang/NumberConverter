/**
 * <NumberConverter - A simple program that converts numeric values among decimal, binary, octal, and hexadecimal numeral systems.>
 * Copyright (C) <2017>  <Yue Zhang>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 */
package com.example.yue.numberconverter.core
/**
 * Since I wanted to keep this project as much 'Kotliny' as I can, I decided not to use Java.lang.Math
 * Instead, I wrote my own functions in Kotlin, in order to keep(or increase) the purity of Kotlin
 */
class SimpleMath {

    companion object {
        fun pow(base: Int, index: Int): Int{
            var counter = 0
            var result = 1
            while (counter < index){
                if (counter == index/2 && index%2 == 0){
                    result *= result
                    break
                }
                else if (counter * 2 + 1 == index){
                    result = (result * result) * base
                    break
                }
                result *= base
                counter++
            }
            return result
        }

        fun isValidBinary(p0: String): Boolean =
                (!(p0.contains("2") ||
                    p0.contains("3") ||
                    p0.contains("4") ||
                    p0.contains("5") ||
                    p0.contains("6") ||
                    p0.contains("7") ||
                    p0.contains("8") ||
                    p0.contains("9")) && p0.matches("\\d+(\\d+)?".toRegex())) || p0 == ""

        fun isValidOctal(p0: String): Boolean =
                (!(p0.contains("8") ||
                    p0.contains("9")) && p0.matches("-?\\d+(\\.\\d+)?".toRegex())) || p0 == ""

        fun isValidHex(p0: String): Boolean =
                p0.matches("\\d+(\\.\\d+)?".toRegex()) ||
                    p0.contains("A") ||
                    p0.contains("B") ||
                    p0.contains("C") ||
                    p0.contains("D") ||
                    p0.contains("E") ||
                    p0.contains("F") || p0 == ""

        fun generateZeroString(length: Int): String = if (length == 0) ""
            else {
                var i = length
                var string = ""
                while (i > 0){
                    string += "0"
                    i--
                }
                string
            }

        fun insertSpace(p0: String): String{
            var i = 1
            val array = ArrayList<String>(p0.split(""))
            var output = ""
            while(5 * i < array.size){
                array.add(5*i, " ")
                i++
            }
            array.forEach { output += it }
            return output.trim()
        }

        fun formatBinary(p0: String, bitLength: Int): String = when {
            p0 == "" -> ""
            p0.length < bitLength -> insertSpace(generateZeroString(bitLength - p0.length) + p0)
            else -> insertSpace(p0)
        }

        fun trimZeroes(p0: String): String = when {
            p0.length - p0.replace("1", "").length == 1 -> "1" //only 1
            p0.length - p0.replace("1", "").length == 0 -> "0" //all 0's
            p0.length - p0.replace("0", "").length == 0 -> p0 //all 1's
            else -> "1" + p0.split("1".toRegex(), 2)[1] //general
        }

        fun reserveFormatBinary(p0: String): String = if (p0 == "") ""
        else trimZeroes(p0.replace(" ", ""))

        fun flipBit(bit: Char): Char = if (bit == '0') '1'
        else '0'

        fun flipBits(bitString: String): String = String(bitString.map { flipBit(it) }.toCharArray())
    }
}