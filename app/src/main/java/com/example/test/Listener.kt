package com.example.test

interface Listener {
    fun closeDialog()
    fun dataChanged(isDate: Boolean, data:String)
}