package com.cj.dentalclinic.repository

import com.cj.dentalclinic.entity.BillLine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BillLineRepository : JpaRepository<BillLine, Int> {

  @Query("SELECT bl FROM BillLine bl WHERE bl.bill.id = ?1")
  fun findBillLinesByBillId(billId: Int): List<BillLine>

}
