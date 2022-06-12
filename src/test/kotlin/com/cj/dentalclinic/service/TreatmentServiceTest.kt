package com.cj.dentalclinic.service

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.dto.TreatmentDto
import com.cj.dentalclinic.entity.Treatment
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.ClinicRepository
import com.cj.dentalclinic.repository.TreatmentRepository
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.Assumptions.assumeThat
import org.junit.jupiter.api.*
import org.springframework.dao.DataIntegrityViolationException

internal class TreatmentServiceTest {

  private val dataStore = ClinicDataStore()

  private val clinicRepository: ClinicRepository = mockk(relaxed = true)

  private val treatmentRepository: TreatmentRepository = mockk(relaxed = true)

  private val treatmentService = TreatmentService(treatmentRepository, clinicRepository)

  @Nested
  @DisplayName("getTreatmentsByClinicId(clinicId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetTreatments {

    @Test
    internal fun `should call TreatmentsRepository to findAll treatments by clinic id`() {

      val clinicId = dataStore.validId

      treatmentService.getAllTreatments(clinicId)

      verify(exactly = 1) { treatmentRepository.findAllByClinicId(clinicId) }

    }

  }

  @Nested
  @DisplayName("getTreatmentId(treatmentId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetTreatmentById {

    private val existingTreatmentId = dataStore.validId

    @BeforeEach
    internal fun setup() {
      every { treatmentRepository.findById(existingTreatmentId) } returns dataStore.findTreatmentById(
        existingTreatmentId
      )
    }

    @AfterEach
    internal fun tearDown() {
      clearMocks(treatmentRepository)
    }

    @Test
    internal fun `should return TreatmentDto with given id, not blank name and fee greater or equal to 100`() {

      val treatmentDto = treatmentService.getTreatmentById(existingTreatmentId)

      assertThat(treatmentDto.id).isEqualTo(existingTreatmentId)

      assertThat(treatmentDto.name).isNotBlank

      assertThat(treatmentDto.fee).isGreaterThanOrEqualTo(100.00)

    }

    @Test
    internal fun `should call TreatmentRepository to find Treatment by id`() {

      treatmentService.getTreatmentById(existingTreatmentId)

      verify(exactly = 1) { treatmentRepository.findById(existingTreatmentId) }

    }

    @Test
    internal fun `should throw ResourceNotFoundException when no Treatment found with given id`() {

      val nonExistingTreatmentId = dataStore.invalidId

      every { treatmentRepository.findById(nonExistingTreatmentId) } returns dataStore.findTreatmentById(
        nonExistingTreatmentId
      )

      assertThatThrownBy { treatmentService.getTreatmentById(nonExistingTreatmentId) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Treatment found with id : $nonExistingTreatmentId")
    }

  }

  @Nested
  @DisplayName("addTreatment(treatmentDto: TreatmentDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class AddTreatment {

    private val clinicId = dataStore.validId

    private val newTreatment = dataStore.newTreatment()

    @BeforeEach
    internal fun setUp() {

      every { clinicRepository.getReferenceById(clinicId) } returns dataStore.findClinicById(clinicId).get()

      every { treatmentRepository.save(ofType(Treatment::class)) } returns dataStore.saveTreatment(newTreatment)
    }

    @AfterEach
    internal fun tearDown() {
      clearMocks(treatmentRepository, clinicRepository)
    }

    @Test
    internal fun `should call ClinicRepository to get Clinic Reference by id`() {

      treatmentService.addTreatment(clinicId, TreatmentDto(newTreatment))

      verify(exactly = 1) { clinicRepository.getReferenceById(clinicId) }

    }

    @Test
    internal fun `should call TreatmentRepository to save new Treatment`() {

      treatmentService.addTreatment(clinicId, TreatmentDto(newTreatment))

      verify(exactly = 1) { treatmentRepository.save(ofType(Treatment::class)) }

    }

    @Test
    internal fun `should return saved Treatment with generated id, given name and fee`() {

      assumeThat(newTreatment.id).isNull()

      val treatmentDto = treatmentService.addTreatment(clinicId, TreatmentDto(newTreatment))

      assertThat(treatmentDto.id).isNotNull

      assertThat(treatmentDto.name).isEqualTo(newTreatment.name)

      assertThat(treatmentDto.fee).isEqualTo(newTreatment.fee)

    }

    @Test
    internal fun `should throw ResourceNotFoundException when TreatmentRepository save throw DataIntegrityViolationException`() {

      every { treatmentRepository.save(ofType(Treatment::class)) }.throws(DataIntegrityViolationException("SQL Exception"))

      assertThatThrownBy { treatmentService.addTreatment(clinicId, TreatmentDto(newTreatment)) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Clinic found with id : $clinicId")

    }

  }

  @Nested
  @DisplayName("updateTreatment(treatmentDto: TreatmentDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class UpdateTreatment {

    private val treatmentId = dataStore.validId

    private val updatedTreatment = dataStore.createTreatment(treatmentId)

    @BeforeEach
    internal fun setup() {

      every { treatmentRepository.existsById(treatmentId) } returns dataStore.treatmentExistById(treatmentId)

      every { treatmentRepository.findById(treatmentId) } returns
          dataStore.findTreatmentById(treatmentId)

      every { treatmentRepository.save(updatedTreatment) } returns updatedTreatment
    }

    @AfterEach
    internal fun tearDown() {
      clearMocks(treatmentRepository)
    }

    @Test
    internal fun `should call TreatmentRepository existsById for given treatment id`() {

      treatmentService.updateTreatment(treatmentId, TreatmentDto(updatedTreatment))

      verify(exactly = 1) { treatmentRepository.existsById(treatmentId) }

    }

    @Test
    internal fun `should call TreatmentRepository to find treatment for given treatment id`() {

      treatmentService.updateTreatment(treatmentId, TreatmentDto(updatedTreatment))

      verify(exactly = 1) { treatmentRepository.findById(treatmentId) }

    }

    @Test
    internal fun `should call TreatmentRepository to save treatment if treatment exist for given treatment id`() {

      treatmentService.updateTreatment(treatmentId, TreatmentDto(updatedTreatment))

      verify(exactly = 1) { treatmentRepository.save(updatedTreatment) }

    }

    @Test
    internal fun `should return updated TreatmentDto when update treatment is invoked`() {

      val updatedTreatmentDto = treatmentService.updateTreatment(treatmentId, TreatmentDto(updatedTreatment))

      assertThat(updatedTreatmentDto).isEqualTo(TreatmentDto(updatedTreatment))

    }

    @Test
    internal fun `should throw ResourceNotFoundException when Treatment does not exists`() {

      val nonExistingTreatmentId = dataStore.invalidId

      every { treatmentRepository.existsById(nonExistingTreatmentId) } returns false

      verify { treatmentRepository.save(updatedTreatment) wasNot Called }

      assertThatThrownBy { treatmentService.updateTreatment(nonExistingTreatmentId, TreatmentDto(updatedTreatment)) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Treatment found with id : $nonExistingTreatmentId")

    }

  }

  @Nested
  @DisplayName("deleteTreatment(treatmentId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteClinic {

    @Test
    internal fun `should call TreatmentRepository to delete Treatment by id when Treatment exists`() {

      val existingTreatmentId = dataStore.validId

      every { treatmentRepository.existsById(existingTreatmentId) } returns dataStore.treatmentExistById(
        existingTreatmentId
      )

      treatmentService.deleteTreatment(existingTreatmentId)

      verify(exactly = 1) { treatmentRepository.deleteById(existingTreatmentId) }

    }

    @Test
    internal fun `should throw ResourceNotFoundException when Treatment does not exists`() {

      val nonExistingTreatmentId = dataStore.invalidId

      every { treatmentRepository.existsById(nonExistingTreatmentId) } returns dataStore.treatmentExistById(
        nonExistingTreatmentId
      )

      verify(exactly = 0) { treatmentRepository.deleteById(nonExistingTreatmentId) }

      assertThatThrownBy { treatmentService.deleteTreatment(nonExistingTreatmentId) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Treatment found with id : $nonExistingTreatmentId")

    }

  }

}
