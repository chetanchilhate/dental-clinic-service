package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.EmptyResponse
import com.cj.dentalclinic.dto.TreatmentDto
import com.cj.dentalclinic.service.TreatmentService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class TreatmentController(private val treatmentService: TreatmentService) {

  @GetMapping("/clinics/{clinicId}/treatments")
  fun getAllTreatments(@PathVariable("clinicId") clinicId: Int) = treatmentService.getAllTreatments(clinicId)

  @GetMapping("/treatments/{id}")
  fun getTreatmentById(@PathVariable("id") treatmentId: Int) = treatmentService.getTreatmentById(treatmentId)

  fun addTreatment(clinicId: Int, treatmentDto: TreatmentDto) = treatmentService.addTreatment(clinicId, treatmentDto)

  fun updateTreatment(treatmentId: Int, treatmentDto: TreatmentDto) = treatmentService.updateTreatment(treatmentId, treatmentDto)

  fun deleteTreatment(treatmentId: Int): EmptyResponse {
    treatmentService.deleteTreatment(treatmentId)
    return  EmptyResponse()
  }

}
