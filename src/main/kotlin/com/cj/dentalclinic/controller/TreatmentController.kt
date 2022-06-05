package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.EmptyResponse
import com.cj.dentalclinic.dto.TreatmentDto
import com.cj.dentalclinic.service.TreatmentService

class TreatmentController(private val treatmentService: TreatmentService) {

  fun getAllTreatments(clinicId: Int) = treatmentService.getAllTreatments(clinicId)

  fun getTreatmentById(treatmentId: Int) = treatmentService.getTreatmentById(treatmentId)

  fun addTreatment(treatmentDto: TreatmentDto) = treatmentService.addTreatment(treatmentDto)

  fun updateTreatment(treatmentId: Int, treatmentDto: TreatmentDto) = treatmentService.updateTreatment(treatmentId, treatmentDto)

  fun deleteTreatment(treatmentId: Int): EmptyResponse {
    treatmentService.deleteTreatment(treatmentId)
    return  EmptyResponse()
  }

}
