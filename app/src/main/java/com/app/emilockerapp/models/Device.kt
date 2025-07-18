package com.app.emilockerapp.models

data class Device(
    val id: String,
    val name: String,
    val model: String,
    val status: String,
    val emiPaid: Int,
    val totalEmi: Int,
    val dueAmount: Int,
    val dueDate: String = "25-06-2025"
)
