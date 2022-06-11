package com.cj.dentalclinic.service

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.dto.ClinicDto
import com.cj.dentalclinic.entity.Clinic
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.ClinicRepository
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.Assumptions.assumeThat
import org.junit.jupiter.api.*

internal class ClinicServiceTest {

  private val dataStore = ClinicDataStore()

  private val clinicRepository: ClinicRepository = mockk(relaxed = true)

  private val clinicService = ClinicService(clinicRepository)

  @Nested
  @DisplayName("getClinics()")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetClinics {

    @Test
    internal fun `should call ClinicRepository to findAll clinics`() {

      clinicService.getAllClinics()

      verify(exactly = 1) { clinicRepository.findAll() }

    }

  }

  @Nested
  @DisplayName("getClinicById(clinicId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetClinicById {

    private val existingClinicId = dataStore.validId

    @BeforeEach
    internal fun setup() {
      every { clinicRepository.findById(existingClinicId) } returns dataStore.findClinicById(existingClinicId)
    }

    @AfterEach
    internal fun tearDown() {
      clearMocks(clinicRepository)
    }

    @Test
    internal fun `should return ClinicDto with given id with a name`() {

      val clinicDto = clinicService.getClinicById(existingClinicId)

      assertThat(clinicDto.id).isEqualTo(existingClinicId)

      assertThat(clinicDto.name).isNotBlank

    }

    @Test
    internal fun `should call ClinicRepository to find clinic by id`() {

      clinicService.getClinicById(existingClinicId)

      verify(exactly = 1) { clinicRepository.findById(existingClinicId) }

    }

    @Test
    internal fun `should throw ResourceNotFoundException when no Clinic found with given id`() {

      val nonExistingClinicId = dataStore.invalidId

      every { clinicRepository.findById(nonExistingClinicId) } returns dataStore.findClinicById(nonExistingClinicId)

      assertThatThrownBy { clinicService.getClinicById(nonExistingClinicId) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Clinic found with id : $nonExistingClinicId")
    }

  }

  @Nested
  @DisplayName("createClinic(clinic: ClinicDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class CreateClinic {

    private val newClinic = dataStore.newClinic()

    @BeforeEach
    internal fun setUp() {
      every { clinicRepository.save(ofType(Clinic::class)) } returns dataStore.saveClinic(newClinic)
    }

    @Test
    internal fun `should call ClinicRepository to save new Clinic`() {

      clinicService.createClinic(ClinicDto(newClinic))

      verify(exactly = 1) { clinicRepository.save(ofType(Clinic::class))  }

    }

    @Test
    internal fun `should return saved Clinic with generated id and given name`() {

      assumeThat(newClinic.id).isNull()

      val clinicDto = clinicService.createClinic(ClinicDto(newClinic))

      assertThat(clinicDto.id).isNotNull

      assertThat(clinicDto.name).isEqualTo(newClinic.name)

    }

  }

  @Nested
  @DisplayName("updateClinic(clinic: ClinicDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class UpdateClinic {

    private val existingClinicId = dataStore.validId

    private val updatedClinic = dataStore.createClinic(existingClinicId)

    @BeforeEach
    internal fun setUp() {
      clearMocks(clinicRepository)
    }

    @Test
    internal fun `should call ClinicRepository to update Clinic when Clinic exists`() {

      every { clinicRepository.existsById(existingClinicId) } returns true

      every { clinicRepository.save(updatedClinic) } returns updatedClinic

      clinicService.updateClinic(existingClinicId, ClinicDto(updatedClinic))

      verify(exactly = 1) { clinicRepository.save(updatedClinic) }

    }

    @Test
    internal fun `should throw ResourceNotFoundException when Clinic does not exists`() {

      verify(exactly = 0) { clinicRepository.save(updatedClinic) }

      assertThatThrownBy { clinicService.updateClinic(existingClinicId, ClinicDto(updatedClinic)) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Clinic found with id : $existingClinicId")

    }

  }

  @Nested
  @DisplayName("deleteClinic(clinicId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteClinic {

    @Test
    internal fun `should call ClinicRepository to delete Clinic by id when Clinic exists`() {

      val existingClinicId = dataStore.validId

      every { clinicRepository.existsById(existingClinicId) } returns true

      clinicService.deleteClinic(existingClinicId)

      verify(exactly = 1) { clinicRepository.deleteById(existingClinicId) }

    }

    @Test
    internal fun `should throw ResourceNotFoundException when Clinic does not exists`() {

      val nonExistingClinicId = dataStore.invalidId

      verify(exactly = 0) { clinicRepository.deleteById(nonExistingClinicId) }

      assertThatThrownBy { clinicService.deleteClinic(nonExistingClinicId) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Clinic found with id : $nonExistingClinicId")

    }

  }

}
