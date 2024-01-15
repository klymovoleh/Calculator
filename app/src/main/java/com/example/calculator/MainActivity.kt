package com.example.calculator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var operation: TextView
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        operation = findViewById(R.id.math_operation)
        resultText = findViewById(R.id.result_text)

        setupButtons()
    }

    private fun setupButtons() {
        val buttonIds = arrayOf(
            R.id.btn_minus, R.id.btn_plus, R.id.btn_div, R.id.btn_multi, R.id.btn_breketOpen,
            R.id.btn_breketClose, R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_point,
            R.id.btn_AC, R.id.btn_equl, R.id.btn_back
        )

        for (buttonId in buttonIds) {
            findViewById<TextView>(buttonId).setOnClickListener {
                handleButtonClick(buttonId)
            }
        }
    }

    private fun handleButtonClick(buttonId: Int) {
        when (buttonId) {
            R.id.btn_equl -> evaluateExpression()
            R.id.btn_back -> handleBackspace()
            R.id.btn_AC -> clearTextViews()
            else -> setTextFields(findViewById<TextView>(buttonId).text.toString())
        }
    }

    private fun setTextFields(str: String) {
        operation.textSize = 40f
        operation.append(str)
        when (operation.text.length) {
            in 11..18 -> operation.textSize = 30f
            in 19..(operation.text.length + 1) -> operation.textSize = 20f
        }

    }

    private fun evaluateExpression() {
        try {
            val expression = operation.text.toString()
            val ex = ExpressionBuilder(expression).build()
            val result = ex.evaluate()

            val longRes = result.toLong()
            if (result == longRes.toDouble()) resultText.text = longRes.toString()
            else {
                resultText.text = result.toString()
                resultText.textSize = 30f
            }

        } catch (e: ArithmeticException) {
            // Обробка помилок ділення на нуль
            resultText.text = "Помилка: Ділення на нуль"
            resultText.textSize = 20f
        } catch (e: IllegalArgumentException) {
            // Обробка помилок некоректного математичного выразу
            resultText.text = "Помилка: Некоректний вираз"
            resultText.textSize = 20f
        } catch (e: Exception) {
            // Обробка інших несподіваних помилок
            val message = "Помилка: Некоректний вираз"
            resultText.text = message
            resultText.textSize = 20f
        }
    }

    private fun handleBackspace() {
        val str = operation.text.toString()
        if (str.isNotEmpty()) operation.text = str.substring(0, str.length - 1)
        resultText.text = ""
    }

    private fun clearTextViews() {
        operation.text = ""
        resultText.text = ""
    }
}