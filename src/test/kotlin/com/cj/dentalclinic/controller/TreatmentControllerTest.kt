package com.cj.dentalclinic.controller

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.dto.TreatmentDto
import com.cj.dentalclinic.service.TreatmentService
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class TreatmentControllerTest {

  private val dataStore = ClinicDataStore()

  private val treatmentService: TreatmentService = mockk(relaxed = true)

  private val treatmentController = TreatmentController(treatmentService)

  private val clinicId = 1

  private val treatmentId = 1

  private val treatmentDto = TreatmentDto(dataStore.newTreatment())

  @Test
  internal fun `should call TreatmentService to get all the treatments for given clinic id`() {

    treatmentController.getAllTreatments(clinicId)

    verify(exactly = 1) { treatmentService.getAllTreatments(clinicId) }

  }

  @Test
  internal fun `should call TreatmentService to get treatment for given treatment id`() {

    treatmentController.getTreatmentById(treatmentId)

    verify(exactly = 1) { treatmentService.getTreatmentById(treatmentId) }

  }

  @Test
  internal fun `should call TreatmentService to add treatment for given clinicId and treatment`() {

    treatmentController.addTreatment(clinicId, treatmentDto)

    verify(exactly = 1) { treatmentService.addTreatment(clinicId, treatmentDto) }

  }

  @Test
  internal fun `should call TreatmentService to update treatment for given id and treatment`() {

    treatmentController.updateTreatment(treatmentId, treatmentDto)

    verify(exactly = 1) { treatmentService.updateTreatment(treatmentId,treatmentDto) }

  }

  @Test
  internal fun `should call TreatmentService to delete treatment for given treatment id`() {

    treatmentController.deleteTreatment(treatmentId)

    verify(exactly = 1) { treatmentService.deleteTreatment(treatmentId) }

  }

}
