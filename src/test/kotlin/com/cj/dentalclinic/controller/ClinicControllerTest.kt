package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.ClinicDto
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.service.ClinicService
import io.mockk.mockk
import io.mockk.verify
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
  fun `should call ClinicService to get Clinic with given id`() {

    val clinicId = 2

    clinicController.getClinicById(clinicId)

    verify(exactly = 1) { clinicService.getClinicById(clinicId) }

  }

  @Test
  fun `should call ClinicService to create Clinic with given ClinicDto`() {

    val clinicDto = ClinicDto(name = "Sujata Dental Clinic")

    clinicController.createClinic(clinicDto)

    verify(exactly = 1) { clinicService.createClinic(clinicDto) }

  }

  @Test
  fun `should call ClinicService to update Clinic with given ClinicDto`() {

    val clinicDto = ClinicDto(2, "Sujata Dental Clinic")

    clinicController.updateClinic(clinicDto)

    verify(exactly = 1) { clinicService.updateClinic(clinicDto) }

  }

  @Test
  fun `updateClinic should throw ResourceNotFoundException when given id is null`() {

    val clinicDto = ClinicDto(name = "Sujata Dental Clinic")

    assertThatThrownBy { clinicController.updateClinic(clinicDto) }
      .isInstanceOf(ResourceNotFoundException::class.java)
      .hasMessage("No Clinic found with id : ${clinicDto.id}")

  }

}
