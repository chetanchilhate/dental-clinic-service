package com.cj.dentalclinic.controller

import com.cj.dentalclinic.service.BillingService
import com.cj.dentalclinic.service.PDFGenerator
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/export-pdf")
class PdfController(private val billingService: BillingService) {

  @GetMapping("/bill-no/{billId}")
  fun generatePdfByBillId(@PathVariable("billId") billId: Int): ResponseEntity<InputStreamResource> {

    val billDetailDto = billingService.getBillDetailByBillId(billId)

    val pdfByteInputStream = PDFGenerator.invoicePDFReport(billDetailDto)

    val headers = HttpHeaders()
    headers.add("Content-Disposition", "inline;filename=invoice_$billId.pdf")

    return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
      .body(InputStreamResource(pdfByteInputStream))
  }

}
