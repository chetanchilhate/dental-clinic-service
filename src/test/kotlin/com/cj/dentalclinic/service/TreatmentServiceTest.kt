package com.cj.dentalclinic.service

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.dto.ClinicDto
import com.cj.dentalclinic.dto.TreatmentDto
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.TreatmentRepository
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.*


internal class TreatmentServiceTest {

  private val dataStore = ClinicDataStore()

  private val clinicService: ClinicService = mockk(relaxed = true)

  private val treatmentRepository: TreatmentRepository = mockk(relaxed = true)

  private val treatmentService = TreatmentService(treatmentRepository, clinicService)

  @Nested
  @DisplayName("getTreatmentsByClinicId(clinicId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetTreatments {

    @Test
    internal fun `should call TreatmentsRepository to findAll treatments by clinic id`() {

      val clinicId = 1

      treatmentService.getAllTreatments(clinicId)

      verify(exactly = 1) { treatmentRepository.findAllByClinicId(clinicId) }

    }

  }

  @Nested
  @DisplayName("getTreatmentId(treatmentId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetTreatmentById {

    private val treatmentIdExist = 1

    private val treatmentIdNotExist = 4

    @BeforeEach
    internal fun setup() {

      every { treatmentRepository.findById(treatmentIdExist) } returns dataStore.findTreatmentsById(treatmentIdExist)

      every { treatmentRepository.findById(treatmentIdNotExist) } returns dataStore.findTreatmentsById(
        treatmentIdNotExist
      )

    }

    @AfterEach
    internal fun tearDown() {
      clearMocks(treatmentRepository)
    }

    @Test
    internal fun `should return TreatmentDto with given id, not blank name and fee greater or equal to 100`() {

      val treatmentDto = treatmentService.getTreatmentById(treatmentIdExist)

      assertThat(treatmentDto.id).isEqualTo(treatmentIdExist)

      assertThat(treatmentDto.name).isNotBlank

      assertThat(treatmentDto.fee).isGreaterThanOrEqualTo(100.00)

    }

    @Test
    internal fun `should call TreatmentRepository to find Treatment by id`() {

      treatmentService.getTreatmentById(treatmentIdExist)

      verify(exactly = 1) { treatmentRepository.findById(treatmentIdExist) }

    }

    @Test
    internal fun `should throw ResourceNotFoundException when no Treatment found with given id`() {

      assertThatThrownBy { treatmentService.getTreatmentById(treatmentIdNotExist) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Treatment found with id : $treatmentIdNotExist")
    }

  }

  @Nested
  @DisplayName("createTreatment(treatmentDto: TreatmentDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class CreateTreatment {

    private val clinicId = 1

    private val clinic = dataStore.findClinicById(clinicId).get()

    private val newTreatment = dataStore.newTreatment()

    @BeforeEach
    internal fun setUp() {

      every { clinicService.getClinicById(clinicId) } returns ClinicDto(clinic)

      every { treatmentRepository.save(newTreatment) } returns dataStore.saveTreatment(newTreatment)
    }

    @AfterEach
    internal fun tearDown() {
      clearMocks(clinicService, treatmentRepository)
    }

    @Test
    internal fun `should call ClinicService to get Clinic by id`() {

      treatmentService.addTreatment(clinicId, TreatmentDto(newTreatment))

      verify(exactly = 1) { clinicService.getClinicById(clinicId) }

    }

    @Test
    internal fun `should call TreatmentRepository to save new Treatment`() {

      treatmentService.addTreatment(clinicId, TreatmentDto(newTreatment))

      verify(exactly = 1) { treatmentRepository.save(newTreatment) }

    }

    @Test
    internal fun `should return saved Treatment with generated id, given name and fee`() {

      val treatmentDto = treatmentService.addTreatment(clinicId, TreatmentDto(newTreatment))

      assertThat(treatmentDto.id).isEqualTo(dataStore.newTreatmentId())

      assertThat(treatmentDto.name).isEqualTo(newTreatment.name)

      assertThat(treatmentDto.fee).isEqualTo(newTreatment.fee)

    }

  }

  @Nested
  @DisplayName("updateTreatment(treatmentDto: TreatmentDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class UpdateTreatment {

    private val treatmentId = 3

    private val updatedTreatment = dataStore.findTreatmentsById(treatmentId).get()

    @BeforeEach
    internal fun setup() {
      every { treatmentRepository.findTreatmentAndClinicById(treatmentId) } returns
          dataStore.findTreatmentsById(treatmentId)

      every { treatmentRepository.save(updatedTreatment) } returns updatedTreatment
    }

    @AfterEach
    internal fun tearDown() {
      clearMocks(treatmentRepository)
    }

    @Test
    internal fun `should call TreatmentRepository findTreatmentsById for given treatment id`() {

      treatmentService.updateTreatment(treatmentId, TreatmentDto(updatedTreatment))

      verify(exactly = 1) { treatmentRepository.findTreatmentAndClinicById(treatmentId) }

    }

    @Test
    internal fun `should call TreatmentRepository to save treatment if treatment exist for given treatment id`() {

      treatmentService.updateTreatment(treatmentId, TreatmentDto(updatedTreatment))

      verify(exactly = 1) { treatmentRepository.save(updatedTreatment) }

    }

    @Test
    internal fun `should throw ResourceNotFoundException when Treatment does not exists`() {

      val unknownTreatmentId = treatmentId + 10

      every { treatmentRepository.findTreatmentAndClinicById(unknownTreatmentId) } returns
          dataStore.findTreatmentsById(unknownTreatmentId)

      verify(exactly = 0) { treatmentRepository.save(updatedTreatment) }

      assertThatThrownBy { treatmentService.updateTreatment(unknownTreatmentId, TreatmentDto(updatedTreatment)) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Treatment found with id : $unknownTreatmentId")

    }

  }

  @Nested
  @DisplayName("deleteTreatment(treatmentId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteClinic {

    @Test
    internal fun `should call TreatmentRepository to delete Treatment by id when Treatment exists`() {

      val existingTreatmentId = 3

      every { treatmentRepository.existsById(existingTreatmentId) } returns true

      treatmentService.deleteTreatment(existingTreatmentId)

      verify(exactly = 1) { treatmentRepository.deleteById(existingTreatmentId) }

    }

    @Test
    internal fun `should throw ResourceNotFoundException when Treatment does not exists`() {

      val unknownTreatmentId = 10

      every { treatmentRepository.existsById(unknownTreatmentId) } returns false

      verify(exactly = 0) { treatmentRepository.deleteById(unknownTreatmentId) }

      assertThatThrownBy { treatmentService.deleteTreatment(unknownTreatmentId) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Treatment found with id : $unknownTreatmentId")

    }

  }

}
