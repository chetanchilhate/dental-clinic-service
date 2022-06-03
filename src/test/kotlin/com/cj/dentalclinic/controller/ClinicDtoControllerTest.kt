package com.cj.dentalclinic.controller

import com.cj.dentalclinic.service.ClinicService
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class ClinicDtoControllerTest {

  private val clinicService: ClinicService = mockk(relaxed = true)

  private val clinicController = ClinicController(clinicService)

  @Test
  fun `should call ClinicService to getAll clinics`() {

    clinicController.getAllClinics()

    verify(exactly = 1) { clinicService.getAllClinics() }

  }

}
