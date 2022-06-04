package com.cj.dentalclinic.controller

import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.service.ClinicService
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
  fun `should return Clinic with given id with a name`() {

    val clinicId = 2

    val clinicDto = clinicController.getClinicById(clinicId)

    assertThat(clinicDto.id).isEqualTo(clinicId)

    assertThat(clinicDto.name).isNotBlank

  }

  @Test
  fun `should throw ResourceNotFoundException when no Clinic found with given id`() {

    val clinicId = 4

    assertThatThrownBy { clinicController.getClinicById(clinicId) }
      .isInstanceOf(ResourceNotFoundException::class.java)
      .hasMessage("No Clinic found with id : $clinicId")
  }


  @Test
  fun `should call ClinicService to get Clinic with given id`() {

    val clinicId = 2

    clinicController.getClinicById(clinicId)

    verify(exactly = 1) { clinicService.getClinicById(clinicId) }
  }

}
