package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.Clinic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ClinicControllerTest {

  private val clinicController = ClinicController()

  @Test
  fun `ClinicController should return list of Clinics when getAllClinics is invoked`() {

    val clinics:List<Clinic>  = clinicController.getAllClinics()

    assertEquals(2, clinics.size)

  }

}
