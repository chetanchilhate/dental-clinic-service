package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.EmptyResponse
import com.cj.dentalclinic.dto.TreatmentDto
import com.cj.dentalclinic.service.TreatmentService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class TreatmentController(private val treatmentService: TreatmentService) {

  @GetMapping("/clinics/{clinicId}/treatments")
  fun getAllTreatments(@PathVariable("clinicId") clinicId: Int) = treatmentService.getAllTreatments(clinicId)

  @GetMapping("/treatments/{id}")
  fun getTreatmentById(@PathVariable("id") treatmentId: Int) = treatmentService.getTreatmentById(treatmentId)

  @PostMapping("/clinics/{clinicId}/treatments")
  @ResponseStatus(CREATED)
  fun addTreatment(@PathVariable("clinicId") clinicId: Int, @Valid @RequestBody treatmentDto: TreatmentDto) =
    treatmentService.addTreatment(clinicId, treatmentDto)

  @PutMapping("/treatments/{id}")
  @ResponseStatus(CREATED)
  fun updateTreatment(@PathVariable("id") treatmentId: Int, @Valid @RequestBody treatmentDto: TreatmentDto) =
    treatmentService.updateTreatment(treatmentId, treatmentDto)

  @DeleteMapping("/treatments/{id}")
  fun deleteTreatment(@PathVariable("id") treatmentId: Int): EmptyResponse {
    treatmentService.deleteTreatment(treatmentId)
    return EmptyResponse()
  }

}
