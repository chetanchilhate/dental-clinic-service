package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.BillLine

data class BillLineDto(
  val treatment: String, val fee: Double,
) {
  constructor(billLine: BillLine) : this(treatment = billLine.treatment, fee = billLine.fee)
}
