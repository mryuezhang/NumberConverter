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
 * Since I wanted to keep this project as much 'Kotliny' as I can, I decided not to use Java built-in functions.
 * Instead, I wrote my own functions in Kotlin, in order to keep(or increase) the purity of Kotlin
 */
class NumberConverter {

    companion object {

        fun decimalToBinary(p0 : String): String {
            return if (p0 == "") ""
            else {
                var i = p0.toLong()
                var result = ""
                while (i != 0.toLong()){
                    result = (i%2).toString() + result
                    i /= 2
                }
                result
            }
        }

        fun decimalToOctal(p0: String): String{
            return if (p0 == "") ""
            else{
                var i = p0.toLong()
                var result = ""
                while (i != 0.toLong()){
                    result = (i%8).toString() + result
                    i /= 8
                }
                result
            }
        }

        fun decimalToHexadecimal(p0: String): String{
            return if (p0 == "") ""
            else{
                var i = p0.toLong()
                var result = ""
                while (i != 0.toLong()){
                    result = when(i%16){
                        10.toLong() -> "A"
                        11.toLong() -> "B"
                        12.toLong() -> "C"
                        13.toLong() -> "D"
                        14.toLong() -> "E"
                        15.toLong() -> "F"
                        else -> (i%16).toString()
                    } + result
                    i /= 16
                }
                result
            }
        }

        fun binaryToDecimal(p0: String): String{
            return if (p0 == "") ""
            else {
                val digits = p0.toCharArray()
                val maxIndex = digits.size - 1
                var result = 0.toLong()
                var counter = maxIndex
                while (counter > -1){
                    result += SimpleMath.pow(2, maxIndex - counter) * (digits[counter].toString().toInt()%10)
                    counter--
                }
                result.toString()
            }
        }

        fun binaryToOctal(p0: String): String = decimalToOctal(binaryToDecimal(p0))

        fun binaryToHexadecimal(p0:String): String = decimalToHexadecimal(binaryToDecimal(p0))

        fun octalToDecimal(p0: String): String{
            return if (p0 == "") ""
            else {
                val digits = p0.toCharArray()
                val maxIndex = digits.size - 1
                var result = 0
                var counter = maxIndex
                while (counter > -1){
                    result += SimpleMath.pow(8, maxIndex - counter) * (digits[counter].toString().toInt()%10)
                    counter--
                }
                result.toString()
            }
        }

        fun octalToBinary(p0: String): String = decimalToBinary(octalToDecimal(p0))

        fun octalToHexadecimal(p0: String): String = decimalToHexadecimal(octalToDecimal(p0))

        fun hexadecimalToDecimal(p0: String): String{
            return if (p0 == "") ""
            else {
                val digits = p0.toCharArray()
                val maxIndex = digits.size - 1
                var result = 0.toLong()
                var counter = maxIndex
                while (counter > -1){
                    val i = when (digits[counter]){
                        'A' -> 10
                        'B' -> 11
                        'C' -> 12
                        'D' -> 13
                        'E' -> 14
                        'F' -> 15
                        else -> digits[counter].toString().toInt()
                    }
                    result += SimpleMath.pow(16, maxIndex - counter) * i
                    counter--
                }
                result.toString()
            }
        }

        fun hexadecimalToBinary(p0: String): String = decimalToBinary(hexadecimalToDecimal(p0))

        fun hexadecimalToOctal(p0: String): String = decimalToOctal(hexadecimalToDecimal(p0))

        fun decimalToBinary_OnesComplement(p0: String, bitLength: Int): String = when {
            p0 == "" -> ""
            p0.contains("-") -> String(SimpleMath.formatBinary(decimalToBinary(p0.replace("-", "")), bitLength)
                    .replace(" ", "")
                    .map { SimpleMath.flipBit(it) }
                    .toCharArray())
            else -> SimpleMath.formatBinary(decimalToBinary(p0), bitLength)
        }

        fun bianryToDecimal_OnesComplement(p0: String): String = when {
            p0 == "" -> ""
            p0[0] == '1' -> {
                "-" + binaryToDecimal(SimpleMath.flipBits(p0))
            }
            else -> binaryToDecimal(p0)
        }

    }
}