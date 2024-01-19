package com.example.calculator

import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var operation: TextView
    private lateinit var result: TextView
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        operation = findViewById(R.id.math_operation)
        result = findViewById(R.id.result_text)

        if (savedInstanceState == null) {
            operation.text = viewModel.operationText
            result.text = viewModel.resultText
        } else {
            operation.text = savedInstanceState.getString("KEY", "")
            result.text = savedInstanceState.getString("RESULT_KEY", "")
        }

        setupButtons()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("KEY", operation.text.toString())
        outState.putString("RESULT_KEY", viewModel.resultText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString("KEY", "")
        operation.text = savedText
        val savedResult = savedInstanceState.getString("RESULT_KEY", "")
        if (savedResult != null && savedResult.isNotEmpty()) {
            result.text = savedResult
            if (savedResult.contains("Помилка")) {
                result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            } else {
                result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            }
        }
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

    private fun evaluateExpression(): String? {
        try {
            val expression = operation.text.toString()
            val ex = ExpressionBuilder(expression).build()
            val resultExp = ex.evaluate()

            val longRes = resultExp.toLong()
            val resultString = if (resultExp == longRes.toDouble()) {
                longRes.toString()
            } else {
                resultExp.toString()
            }

            result.text = resultString
            result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)

            // Зберігаємо у ViewModel
            viewModel.operationText = operation.text.toString()
            viewModel.resultText = resultString

            return resultString
        } catch (e: ArithmeticException) {
            result.text = "Помилка: Ділення на нуль"
            result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            return null
        } catch (e: IllegalArgumentException) {
            val message = "Помилка: Некоректний вираз"
            result.text = message
            viewModel.resultText = message
            result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            return null
        } catch (e: Exception) {
            val message = "Помилка: Некоректний вираз"
            result.text = message
            viewModel.resultText = message
            result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            return null
        }
    }


    private fun handleBackspace() {
        val str = operation.text
        if (str.isNotEmpty()) {
            operation.text = str.substring(0,str.length - 1)
        }
        result.text = ""
    }

    private fun clearTextViews() {
        operation.text = ""
        result.text = ""
    }

}