package com.example.calculator
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class CalculatorViewModel(private val state: SavedStateHandle) : ViewModel() {

    var operationText: String
        get() = state.get("operationText") ?: ""
        set(value) = state.set("operationText", value)

    var resultText: String
        get() = state.get("resultText") ?: ""
        set(value) = state.set("resultText", value)
}