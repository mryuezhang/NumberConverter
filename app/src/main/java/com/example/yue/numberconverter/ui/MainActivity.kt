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
package com.example.yue.numberconverter.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import com.example.yue.numberconverter.R
import com.example.yue.numberconverter.core.NumberConverter
import com.example.yue.numberconverter.core.SimpleMath
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        setUpEditTexts()

        setUpBitLengthSwitches()
        setUpBitRepresentationSwitch()

        (nav_view.menu.getItem(0).subMenu.getItem(0).actionView as RadioButton).isChecked = true
        (nav_view.menu.getItem(1).subMenu.getItem(0).actionView as RadioButton).isChecked = true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    //MARK: Private methods
    private fun setUpEditTexts(){
        val editTexts:Array<EditText> = arrayOf(decimal_input, binary_input, octal_input, hexadecimal_input)

        editTexts.forEach {
            it.setOnLongClickListener {
            (it as EditText).text.clear()
            true
        }

            it.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val input = p0.toString()
                    if (it.hasFocus()){
                        when(it){
                            decimal_input -> {
                                if (!SimpleMath.isValidDecimal(input)){
                                    setErrorColor(decimal, decimal_input)
                                    decimal.text = resources.getString(R.string.error_decimal)
                                }
                                else {
                                    if (!rangeCheck_Decimal(input)){
                                        setErrorColor(decimal, decimal_input)
                                        decimal.text = resources.getString(R.string.error_out_of_range)
                                    }
                                    else {
                                        resetColor_Decimal()
                                        when (getBitRepresentation()){
                                            0 -> {
                                                binary_input.setText(SimpleMath.formatBinary(NumberConverter.decimalToBinary(input), getBitLength()), TextView.BufferType.EDITABLE)
                                                octal_input.setText(NumberConverter.decimalToOctal(input), TextView.BufferType.EDITABLE)
                                                hexadecimal_input.setText(NumberConverter.decimalToHexadecimal(input), TextView.BufferType.EDITABLE)
                                            }
                                            1 -> {
                                                val binaryValue = NumberConverter.decimalToBinary_OnesComplement(input, getBitLength())
                                                binary_input.setText(binaryValue, TextView.BufferType.EDITABLE)
                                                octal_input.setText(NumberConverter.binaryToOctal(SimpleMath.reserveFormatBinary(binaryValue)), TextView.BufferType.EDITABLE)
                                                hexadecimal_input.setText(NumberConverter.binaryToHexadecimal(SimpleMath.reserveFormatBinary(binaryValue)), TextView.BufferType.EDITABLE)
                                            }
                                            else -> {
                                                val binaryValue = NumberConverter.decimalToBinary_OnesComplement(input, getBitLength())
                                                binary_input.setText(NumberConverter.decimalToBinary_OnesComplement(binaryValue, getBitLength()), TextView.BufferType.EDITABLE)
                                                octal_input.setText(NumberConverter.decimalToOctal(SimpleMath.reserveFormatBinary(binaryValue)), TextView.BufferType.EDITABLE)
                                                hexadecimal_input.setText(NumberConverter.decimalToHexadecimal(SimpleMath.reserveFormatBinary(binaryValue)), TextView.BufferType.EDITABLE)
                                            }
                                        }
                                    }
                                }
                            }
                            binary_input -> {
                                if (!SimpleMath.isValidBinary(input.replace(" ", ""))){
                                    setErrorColor(binary, binary_input)
                                    binary.text = resources.getString(R.string.error_binary)
                                }
                                else {
                                    val actualBinaryNumber = SimpleMath.reserveFormatBinary(input)
                                    val decimalEquivalence = NumberConverter.binaryToDecimal(actualBinaryNumber)
                                    if (!rangeCheck_Decimal(decimalEquivalence)){
                                        setErrorColor(binary, binary_input)
                                        binary.text = resources.getString(R.string.error_out_of_range)
                                    }
                                    else {
                                        resetColor_Binary()
                                        decimal_input.setText(decimalEquivalence, TextView.BufferType.EDITABLE)
                                        octal_input.setText(NumberConverter.binaryToOctal(actualBinaryNumber), TextView.BufferType.EDITABLE)
                                        hexadecimal_input.setText(NumberConverter.binaryToHexadecimal(actualBinaryNumber), TextView.BufferType.EDITABLE)
                                    }
                                }
                            }
                            octal_input -> {
                                if (!SimpleMath.isValidOctal(p0!!.toString())){
                                    setErrorColor(octal, octal_input)
                                    octal.text = resources.getString(R.string.error_oct)
                                }
                                else{
                                    val decimalEquivalence = NumberConverter.octalToDecimal(octal_input.text.toString())
                                    if (!rangeCheck_Decimal(decimalEquivalence)){
                                        setErrorColor(octal, octal_input)
                                        octal.text = resources.getString(R.string.error_out_of_range)
                                    }
                                    else {
                                        resetColor_Octal()
                                        decimal_input.setText(decimalEquivalence, TextView.BufferType.EDITABLE)
                                        binary_input.setText(SimpleMath.formatBinary(NumberConverter.octalToBinary(octal_input.text.toString()), getBitLength()), TextView.BufferType.EDITABLE)
                                        hexadecimal_input.setText(NumberConverter.octalToHexadecimal(octal_input.text.toString()), TextView.BufferType.EDITABLE)
                                    }
                                }
                            }
                            hexadecimal_input -> {
                                if (!SimpleMath.isValidHex(input)){
                                    setErrorColor(hexadecimal, hexadecimal_input)
                                    hexadecimal.text = resources.getString(R.string.error_hex)
                                }
                                else {
                                    val decimalEquivalence = NumberConverter.hexadecimalToDecimal(input)
                                    if (!rangeCheck_Decimal(decimalEquivalence)){
                                        setErrorColor(hexadecimal, hexadecimal_input)
                                        hexadecimal.text = resources.getString(R.string.error_out_of_range)
                                    }
                                    else {
                                        resetColor_Hex()
                                        decimal_input.setText(decimalEquivalence, TextView.BufferType.EDITABLE)
                                        binary_input.setText(SimpleMath.formatBinary(NumberConverter.hexadecimalToBinary(input), getBitLength()), TextView.BufferType.EDITABLE)
                                        octal_input.setText(NumberConverter.hexadecimalToOctal(input), TextView.BufferType.EDITABLE)
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun bitLengthRadioGroup(): Array<RadioButton>{
        return arrayOf((nav_view.menu.getItem(0).subMenu.getItem(0).actionView as RadioButton),
                (nav_view.menu.getItem(0).subMenu.getItem(1).actionView as RadioButton),
                (nav_view.menu.getItem(0).subMenu.getItem(2).actionView as RadioButton))
    }

    private fun bitRepresentationRadioGroup(): Array<RadioButton>{
        return arrayOf((nav_view.menu.getItem(1).subMenu.getItem(0).actionView as RadioButton),
                (nav_view.menu.getItem(1).subMenu.getItem(1).actionView as RadioButton),
                (nav_view.menu.getItem(1).subMenu.getItem(2).actionView as RadioButton))
    }

    private fun setUpBitLengthSwitches(){
        val bitLengthRadioButtons: Array<RadioButton> = bitLengthRadioGroup()

        bitLengthRadioButtons.forEach { it.setOnClickListener {
            when (it) {
                bitLengthRadioButtons[0] -> {
                    bitLengthRadioButtons.filter { it != bitLengthRadioButtons[0] }.forEach { it.isChecked = false }
                }
                bitLengthRadioButtons[1] -> bitLengthRadioButtons.filter { it != bitLengthRadioButtons[1] }.forEach { it.isChecked = false }
                else -> bitLengthRadioButtons.filter { it != bitLengthRadioButtons[2] }.forEach { it.isChecked = false }
            } }
        }

    }

    private fun setUpBitRepresentationSwitch(){
        val radioButtonGroup: Array<RadioButton> = arrayOf((nav_view.menu.getItem(1).subMenu.getItem(0).actionView as RadioButton),
                (nav_view.menu.getItem(1).subMenu.getItem(1).actionView as RadioButton),
                (nav_view.menu.getItem(1).subMenu.getItem(2).actionView as RadioButton))

        radioButtonGroup.forEach { it.setOnClickListener {
            when (it) {
                radioButtonGroup[0] -> {
                    radioButtonGroup.filter { it != radioButtonGroup[0] }.forEach { it.isChecked = false }
                    decimal_input.inputType = InputType.TYPE_CLASS_NUMBER
                }
                radioButtonGroup[1] -> {
                    radioButtonGroup.filter { it != radioButtonGroup[1] }.forEach { it.isChecked = false }
                    decimal_input.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
                else -> {
                    radioButtonGroup.filter { it != radioButtonGroup[2] }.forEach { it.isChecked = false }
                    decimal_input.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
            } }
        }
    }

    private fun getBitLength(): Int{
        val bitLengthRadioButtons: Array<RadioButton> = bitLengthRadioGroup()
        return when {
            bitLengthRadioButtons[2].isChecked -> 32
            bitLengthRadioButtons[1].isChecked -> 16
            else -> 8
        }
    }

    /**
     * Return:
     * 0 : unsigned
     * 1 : 1's complement
     * 2 : 2's complement
     */
    private fun getBitRepresentation(): Int{
        val bitLengthRadioButtons: Array<RadioButton> = bitRepresentationRadioGroup()
        return when {
            bitLengthRadioButtons[2].isChecked -> 2
            bitLengthRadioButtons[1].isChecked -> 1
            else -> 0
        }
    }

    private fun rangeCheck_Decimal(p0: String): Boolean {
        return if (p0 == "" || p0 == "-") true
        else {
            try {
                when (getBitRepresentation()){
                    0 -> {
                        when(getBitLength()){
                            32 -> p0.toLong() in 0..4294967295
                            16 -> p0.toLong() in 0..65536
                            else  -> p0.toLong() in 0..255
                        }
                    }
                    1 -> {
                        when(getBitLength()){
                            32 -> p0.toDouble() in -2147483647..2147483647
                            16 -> p0.toDouble() in -32767..32767
                            else  -> p0.toDouble() in -127..127
                        }
                    }
                    else -> {
                        when(getBitLength()){
                            32 -> p0.toDouble() in -2147483647..2147483648
                            16 -> p0.toDouble() in -32767..32768
                            else  -> p0.toDouble() in -128..127
                        }
                    }
                }
            } catch (e: NumberFormatException){
                false
            }
        }
    }

    private fun setErrorColor(p0: TextView, p1: EditText){
        p0.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorError))
        p1.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorError))
    }

    private fun resetColor_Decimal(){
        if ((decimal_input.background as ColorDrawable).color != R.color.colorPrimary) {
            decimal_input.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
            decimal.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
            decimal.text = resources.getString(R.string.decimal)
        }
    }

    private fun resetColor_Binary(){
        if ((binary_input.background as ColorDrawable).color != R.color.colorBinary) {
            binary_input.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorBinary))
            binary.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorBinary))
            binary.text = resources.getString(R.string.binary)
        }
    }

    private fun resetColor_Octal(){
        if ((octal_input.background as ColorDrawable).color != R.color.colorOct) {
            octal_input.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorOct))
            octal.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorOct))
            octal.text = resources.getString(R.string.oct)
        }
    }

    private fun resetColor_Hex(){
        if ((hexadecimal_input.background as ColorDrawable).color != R.color.colorHex) {
            hexadecimal_input.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorHex))
            hexadecimal.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorHex))
            hexadecimal.text = resources.getString(R.string.hex)
        }
    }
}
