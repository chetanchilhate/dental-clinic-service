package com.cj.dentalclinic.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ClinicServiceTest {

  private val clinicService = ClinicService()

  @Test
  fun `should return list of at least 2 Clinics`() {

    val clinics = clinicService.getAllClinics()

    assertThat(clinics.size).isGreaterThanOrEqualTo(2)

  }

}