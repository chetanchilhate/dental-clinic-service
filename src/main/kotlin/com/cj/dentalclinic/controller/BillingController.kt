package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.BillLineDto
import com.cj.dentalclinic.service.BillingService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class BillingController(private val billingService: BillingService) {

  @GetMapping("/patients/{patientId}/bills")
  fun getAllBillsByPatientId(@PathVariable("patientId") patientId: Int) =
    billingService.getAllBillsByPatientId(patientId)

  @GetMapping("/bills/{billId}/bill-lines")
  fun getAllBillLinesByBillId(@PathVariable("billId") billId: Int) =
    billingService.getAllBillLinesByBillId(billId)

  @GetMapping("/bill-detail/{billId}")
  fun getBillDetailByBillId(@PathVariable("billId") billId: Int) =
    billingService.getBillDetailByBillId(billId)

  @PostMapping("/patients/{patientId}/bill-lines")
  fun createBill(@PathVariable("patientId") patientId: Int, @RequestBody billLines: List<@Valid BillLineDto>) =
    billingService.createBill(patientId, billLines)

}
