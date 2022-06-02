package com.cj.dentalclinic.service

import com.cj.dentalclinic.repository.ClinicRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class ClinicServiceTest {

  private val clinicRepository: ClinicRepository = mockk(relaxed = true)

  private val clinicService = ClinicService(clinicRepository)

  @Test
  fun `should call ClinicService to getAll clinics`() {

    clinicService.getAllClinics()

    verify(exactly = 1) { clinicRepository.findAll() }

  }

}
