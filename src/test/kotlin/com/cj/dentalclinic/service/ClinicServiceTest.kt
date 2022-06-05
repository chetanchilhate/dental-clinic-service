package com.cj.dentalclinic.service

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
import org.junit.jupiter.api.*
import java.util.Optional.empty
import java.util.Optional.of

internal class ClinicServiceTest {

  private val clinicRepository: ClinicRepository = mockk(relaxed = true)

  private val clinicService = ClinicService(clinicRepository)

  @Nested
  @DisplayName("getClinics()")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetClinics {

    @Test
    fun `should call ClinicRepository to findAll clinics`() {

      clinicService.getAllClinics()

      verify(exactly = 1) { clinicRepository.findAll() }

    }

  }

  @Nested
  @DisplayName("getClinicById(clinicId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetClinicById {

    private val clinicIdWithClinic = 2

    private val clinicIdWithOutClinic = 4

    @BeforeEach
    fun setup() {

      every { clinicRepository.findById(clinicIdWithClinic) } returns
          of(Clinic(clinicIdWithClinic, "Smart Dental Clinic"))

      every { clinicRepository.findById(clinicIdWithOutClinic) } returns empty()

    }

    @Test
    fun `should return Clinic with given id with a name`() {

      val clinicDto = clinicService.getClinicById(clinicIdWithClinic)

      assertThat(clinicDto.id).isEqualTo(clinicIdWithClinic)

      assertThat(clinicDto.name).isNotBlank

    }

    @Test
    fun `should throw ResourceNotFoundException when no Clinic found with given id`() {

      assertThatThrownBy { clinicService.getClinicById(clinicIdWithOutClinic) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Clinic found with id : $clinicIdWithOutClinic")
    }

    @Test
    fun `should call ClinicRepository to find clinic by id`() {

      clinicService.getClinicById(clinicIdWithClinic)

      verify(exactly = 1) { clinicRepository.findById(clinicIdWithClinic) }

    }

  }

  @Nested
  @DisplayName("createClinic(clinic: ClinicDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class CreateClinic {

    private val newClinicId = 4

    private val newClinic = Clinic(name = "Sujata Dental Clinic")

    @BeforeEach
    internal fun setUp() {
      every { clinicRepository.save(newClinic) } returns newClinic.copy(newClinicId)
    }

    @Test
    fun `should call ClinicRepository to save new Clinic`() {

      clinicService.createClinic(ClinicDto(newClinic))

      verify(exactly = 1) { clinicRepository.save(newClinic) }

    }

    @Test
    fun `should return saved Clinic with generated id and given name`() {

      val clinicDto = clinicService.createClinic(ClinicDto(newClinic))

      assertThat(clinicDto.id).isEqualTo(newClinicId)

      assertThat(clinicDto.name).isEqualTo(newClinic.name)

    }

  }

  @Nested
  @DisplayName("updateClinic(clinic: ClinicDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class UpdateClinic {

    private val updatedClinic = Clinic(2, "Sujata Dental Clinic")

    private val existingClinicId = 3

    @BeforeEach
    internal fun setUp() {
      clearMocks(clinicRepository)
    }

    @Test
    fun `should call ClinicRepository to update Clinic when Clinic exists`() {

      every { clinicRepository.existsById(existingClinicId) } returns true

      every { clinicRepository.save(updatedClinic) } returns updatedClinic

      clinicService.updateClinic(existingClinicId, ClinicDto(updatedClinic))

      verify(exactly = 1) { clinicRepository.save(updatedClinic) }

    }

    @Test
    fun `should throw ResourceNotFoundException when Clinic does not exists`() {

      verify(exactly = 0) { clinicRepository.save(updatedClinic) }

      assertThatThrownBy { clinicService.updateClinic(existingClinicId, ClinicDto(updatedClinic)) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Clinic found with id : $existingClinicId")

    }

  }

  @Nested
  @DisplayName("deleteClinic(clinic: ClinicDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteClinic {

    @Test
    fun `should call ClinicRepository to delete Clinic by id when Clinic exists`() {

      val existingClinicId = 4

      every { clinicRepository.existsById(existingClinicId) } returns true

      clinicService.deleteClinic(existingClinicId)

      verify(exactly = 1) { clinicRepository.deleteById(existingClinicId) }

    }

    @Test
    fun `should throw ResourceNotFoundException when Clinic does not exists`() {

      val newClinicId = 3

      every { clinicRepository.existsById(newClinicId) } returns false

      verify(exactly = 0) { clinicRepository.deleteById(newClinicId) }

      assertThatThrownBy { clinicService.deleteClinic(newClinicId) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Clinic found with id : $newClinicId")

    }

  }

}
