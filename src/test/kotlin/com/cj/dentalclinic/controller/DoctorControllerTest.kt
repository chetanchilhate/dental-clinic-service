package com.cj.dentalclinic.controller

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.dto.DoctorDto
import com.cj.dentalclinic.service.DoctorService
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class DoctorControllerTest {

  private val dataStore = ClinicDataStore()

  private val doctorService: DoctorService = mockk(relaxed = true)

  private val doctorController = DoctorController(doctorService)

  private val clinicId = dataStore.validId

  private val doctorId = dataStore.validId

  private val doctorDto = DoctorDto(dataStore.newDoctor())

  @Test
  internal fun `should call DoctorService to get all the doctors for given clinic id`() {

    doctorController.getAllDoctors(clinicId)

    verify(exactly = 1) { doctorService.getAllDoctors(clinicId) }

  }

  @Test
  internal fun `should call DoctorService to get doctor for given doctor id`() {

    doctorController.getDoctorById(doctorId)

    verify(exactly = 1) { doctorService.getDoctorById(doctorId) }

  }

  @Test
  internal fun `should call DoctorService to add doctor for given clinicId and doctor`() {

    doctorController.addDoctor(clinicId, doctorDto)

    verify(exactly = 1) { doctorService.addDoctor(clinicId, doctorDto) }

  }

  @Test
  internal fun `should call DoctorService to update doctor for given id and doctor`() {

    doctorController.updateDoctor(doctorId, doctorDto)

    verify(exactly = 1) { doctorService.updateDoctor(doctorId, doctorDto) }

  }

  @Test
  internal fun `should call DoctorService to delete doctor for given doctor id`() {

    doctorController.deleteDoctor(doctorId)

    verify(exactly = 1) { doctorService.deleteDoctor(doctorId) }

  }

}
