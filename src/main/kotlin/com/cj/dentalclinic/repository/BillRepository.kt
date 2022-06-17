package com.cj.dentalclinic.repository

import com.cj.dentalclinic.entity.Bill
import com.cj.dentalclinic.entity.BillDetailView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BillRepository : JpaRepository<Bill, Int> {

  @Query("SELECT b FROM Bill b WHERE b.patient.id = ?1")
  fun findBillsByPatientId(patientId: Int): List<Bill>

  @Query(nativeQuery = true)
  fun findBillDetailsById(billId: Int): BillDetailView

}
