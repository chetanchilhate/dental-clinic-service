package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.Bill
import java.time.LocalDateTime

data class BillDto(
  val id: Int, val createDateTime: LocalDateTime, val total: Double
) {

  constructor(bill: Bill) : this(
    id = bill.id!!, createDateTime = bill.createDateTime, total = bill.total

  )
}

