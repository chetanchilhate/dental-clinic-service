package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.TreatmentDto
import com.cj.dentalclinic.service.TreatmentService
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class TreatmentControllerTest {

  private val treatmentService: TreatmentService = mockk(relaxed = true)

  private val treatmentController = TreatmentController(treatmentService)


  @Test
  fun `should call TreatmentService to get all the treatments for given clinic id`() {

    val clinicId = 1

    treatmentController.getAllTreatments(clinicId)

    verify(exactly = 1) { treatmentService.getAllTreatments(clinicId) }

  }

  @Test
  fun `should call TreatmentService to get treatment for given treatment id`() {

    val treatmentId = 1

    treatmentController.getTreatmentById(treatmentId)

    verify(exactly = 1) { treatmentService.getTreatmentById(treatmentId) }

  }

  @Test
  fun `should call TreatmentService to add treatment for given treatment`() {

    val treatmentDto = TreatmentDto(name = "Filling", fee = 500.00)

    treatmentController.addTreatment(treatmentDto)

    verify(exactly = 1) { treatmentService.addTreatment(treatmentDto) }

  }

  @Test
  fun `should call TreatmentService to update treatment for given id and treatment`() {

    val treatmentId = 1

    val treatmentDto = TreatmentDto(name = "Filling", fee = 250.00)

    treatmentController.updateTreatment(treatmentId, treatmentDto)

    verify(exactly = 1) { treatmentService.updateTreatment(treatmentId,treatmentDto) }

  }

  @Test
  fun `should call TreatmentService to delete treatment for given treatment id`() {

    val treatmentId = 1

    treatmentController.deleteTreatment(treatmentId)

    verify(exactly = 1) { treatmentService.deleteTreatment(treatmentId) }

  }

}
