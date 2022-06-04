package com.cj.dentalclinic.service

import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.ClinicRepository
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ClinicServiceTest {

  private val clinicRepository: ClinicRepository = mockk(relaxed = true)

  private val clinicService = ClinicService(clinicRepository)

  @Test
  fun `should call ClinicService to getAll clinics`() {

    clinicService.getAllClinics()

    verify(exactly = 1) { clinicRepository.findAll() }

  }

  @Test
  fun `should return Clinic with given id with a name`() {

    val clinicId = 2

    val clinicDto = clinicService.getClinicById(clinicId)

    Assertions.assertThat(clinicDto.id).isEqualTo(clinicId)

    Assertions.assertThat(clinicDto.name).isNotBlank

  }

  @Test
  fun `should throw ResourceNotFoundException when no Clinic found with given id`() {

    val clinicId = 4

    Assertions.assertThatThrownBy { clinicService.getClinicById(clinicId) }
      .isInstanceOf(ResourceNotFoundException::class.java)
      .hasMessage("No Clinic found with id : $clinicId")
  }

}
