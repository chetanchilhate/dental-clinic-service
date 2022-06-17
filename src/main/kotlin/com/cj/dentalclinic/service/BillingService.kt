package com.cj.dentalclinic.service

import com.cj.dentalclinic.dto.BillDetailDto
import com.cj.dentalclinic.dto.BillDto
import com.cj.dentalclinic.dto.BillLineDto
import com.cj.dentalclinic.entity.Bill
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.BillLineRepository
import com.cj.dentalclinic.repository.BillRepository
import com.cj.dentalclinic.repository.PatientRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BillingService(private val billRepository: BillRepository,
                     private val billLineRepository: BillLineRepository,
                     private val patientRepository: PatientRepository) {

  fun getAllBillsByPatientId(patientId: Int): List<BillDto> = billRepository
    .findBillsByPatientId(patientId)
    .map { BillDto(it) }
    .toList()

  fun getAllBillLinesByBillId(billId: Int): List<BillLineDto> = billLineRepository
    .findBillLinesByBillId(billId)
    .map { BillLineDto(it) }
    .toList()

  fun getBillDetailByBillId(billId: Int): BillDetailDto {
    if(billRepository.existsById(billId).not()) {
      throw ResourceNotFoundException("Bill", billId)
    }

    val billDetailView = billRepository.findBillDetailsById(billId);

    val billLinesDto = getAllBillLinesByBillId(billId)

    return BillDetailDto(billDetailView, billLinesDto)
  }

  fun createBill(patientId: Int, billLines: List<BillLineDto>): BillDto {
    val patient = patientRepository.getReferenceById(patientId)
    return try {
      val bill = Bill(patient = patient)
      bill.addBillLines(billLines)
      BillDto(billRepository.save(bill))
    } catch (e: DataIntegrityViolationException) {
      throw ResourceNotFoundException("Patient", patientId)
    }
  }

}
