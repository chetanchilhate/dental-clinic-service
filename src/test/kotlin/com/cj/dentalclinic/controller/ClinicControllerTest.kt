package com.cj.dentalclinic.controller

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.dto.ClinicDto
import com.cj.dentalclinic.service.ClinicService
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class ClinicControllerTest {

  private val dataStore = ClinicDataStore()

  private val clinicService: ClinicService = mockk(relaxed = true)

  private val clinicController = ClinicController(clinicService)

  private val clinicId = dataStore.validId

  private val clinicDto = ClinicDto(dataStore.newClinic())

  @Test
  internal fun `should call ClinicService to get all clinics`() {

    clinicController.getAllClinics()

    verify(exactly = 1) { clinicService.getAllClinics() }

  }

  @Test
  internal fun `should call ClinicService to get Clinic with given id`() {

    clinicController.getClinicById(this.clinicId)

    verify(exactly = 1) { clinicService.getClinicById(clinicId) }

  }

  @Test
  internal fun `should call ClinicService to create Clinic with given ClinicDto`() {

    clinicController.createClinic(clinicDto)

    verify(exactly = 1) { clinicService.createClinic(clinicDto) }

  }

  @Test
  internal fun `should call ClinicService to update Clinic with given id and ClinicDto`() {

    val clinicDto = ClinicDto(dataStore.createClinic(clinicId))

    clinicController.updateClinic(clinicId, clinicDto)

    verify(exactly = 1) { clinicService.updateClinic(clinicId, clinicDto) }

  }

  @Test
  internal fun `should call ClinicService to delete Clinic with given id`() {

    clinicController.deleteClinic(clinicId)

    verify(exactly = 1) { clinicService.deleteClinic(clinicId) }

  }

}
