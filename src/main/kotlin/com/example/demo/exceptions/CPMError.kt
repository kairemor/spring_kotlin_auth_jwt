package com.example.demo.exceptions

interface CPMError {
    fun getCodeError(): String?
    fun getMessage(): String?
}