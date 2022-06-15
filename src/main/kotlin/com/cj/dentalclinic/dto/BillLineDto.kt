package com.cj.dentalclinic.dto

data class BillLineDto(
  val id: Int? = null, val treatment: String, val fee: Double,
)
