package com.cj.dentalclinic.service

import com.cj.dentalclinic.entity.Clinic
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.ClinicRepository
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

  @Test
  fun `should call ClinicRepository to findAll clinics`() {

    clinicService.getAllClinics()

    verify(exactly = 1) { clinicRepository.findAll() }

  }

  @Nested
  @DisplayName("getClinicById(clinicId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetClinicById {

    private val clinicIdWithClinic = 2

    private val clinicIdWithOutClinic = 4

    @BeforeEach
    fun setup() {

      every { clinicRepository.findById(clinicIdWithClinic) } returns of(Clinic(clinicIdWithClinic, "Smart Dental Clinic"))

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

}
