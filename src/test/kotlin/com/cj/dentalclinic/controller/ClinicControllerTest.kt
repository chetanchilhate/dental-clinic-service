package com.cj.dentalclinic.controller

import com.cj.dentalclinic.service.ClinicService
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class ClinicControllerTest {

  private val clinicService: ClinicService = mockk(relaxed = true)

  private val clinicController = ClinicController(clinicService)

  @Test
  fun `should call ClinicService to get all clinics`() {

    clinicController.getAllClinics()

    verify(exactly = 1) { clinicService.getAllClinics() }

  }

  @Test
  fun `should call ClinicService to get Clinic with given id`() {

    val clinicId = 2

    clinicController.getClinicById(clinicId)

    verify(exactly = 1) { clinicService.getClinicById(clinicId) }

  }

}
