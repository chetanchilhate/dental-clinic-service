package com.cj.dentalclinic.service

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.dto.DoctorDto
import com.cj.dentalclinic.entity.Doctor
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.ClinicRepository
import com.cj.dentalclinic.repository.DoctorRepository
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.Assumptions.assumeThat
import org.junit.jupiter.api.*
import org.springframework.dao.DataIntegrityViolationException


internal class DoctorServiceTest {

  private val dataStore = ClinicDataStore()

  private val doctorRepository: DoctorRepository = mockk(relaxed = true)

  private val clinicRepository: ClinicRepository = mockk(relaxed = true)

  private val doctorService = DoctorService(doctorRepository, clinicRepository)

  @Nested
  @DisplayName("getDoctorsByClinicId(clinicId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetDoctors {

    private val clinicId = dataStore.parentIdWithData

    @AfterEach
    internal fun tearDown() {
      clearMocks(doctorRepository)
    }

    @Test
    internal fun `should call DoctorRepository to findAll doctors by clinic id`() {

      doctorService.getAllDoctors(clinicId)

      verify(exactly = 1) { doctorRepository.findAllByClinicId(clinicId) }

    }

    @Test
    internal fun `should return a list of doctors for given clinic id`() {

      every { doctorRepository.findAllByClinicId(clinicId) } returns dataStore.findAllDoctorsByClinicId(clinicId)

      assertThat(doctorService.getAllDoctors(clinicId)).isNotEmpty

    }

  }

  @Nested
  @DisplayName("getDoctorId(doctorId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetDoctorById {

    private val existingDoctorId = dataStore.validId


    @BeforeEach
    internal fun setup() {
      every { doctorRepository.findById(ofType(Int::class)) } answers { dataStore.findDoctorById(firstArg()) }
    }

    @Test
    internal fun `should return DoctorDto with given id with doctor details`() {

      val existingDoctor = dataStore.findDoctorById(existingDoctorId).get()

      val doctorDto = doctorService.getDoctorById(existingDoctorId)

      assertThat(doctorDto).isEqualTo(DoctorDto(existingDoctor))
    }

    @Test
    internal fun `should call DoctorRepository to find Doctor by id`() {

      doctorService.getDoctorById(existingDoctorId)

      verify(exactly = 1) { doctorRepository.findById(existingDoctorId) }

    }

    @Test
    internal fun `should throw ResourceNotFoundException when no Doctor found with given id`() {

      val nonExistingDoctorId = dataStore.invalidId

      assertThatThrownBy { doctorService.getDoctorById(nonExistingDoctorId) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Doctor found with id : $nonExistingDoctorId")
    }

  }

  @Nested
  @DisplayName("addDoctor(doctorDto: DoctorDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class AddDoctor {

    private val clinicId = dataStore.validId

    private val clinic = dataStore.findClinicById(clinicId).get()

    private val newDoctor = dataStore.newDoctor()

    private val savedDoctor = dataStore.saveDoctor(newDoctor)

    @BeforeEach
    internal fun setUp() {

      every { clinicRepository.getReferenceById(clinicId) } returns clinic

      every { doctorRepository.save(ofType(Doctor::class)) } returns savedDoctor
    }

    @AfterEach
    internal fun tearDown() {
      clearMocks(doctorRepository, clinicRepository)
    }

    @Test
    internal fun `should call ClinicRepository to get Clinic Reference by id`() {

      doctorService.addDoctor(clinicId, DoctorDto(newDoctor))

      verify(exactly = 1) { clinicRepository.getReferenceById(clinicId) }

    }

    @Test
    internal fun `should call DoctorRepository to save new Doctor`() {

      doctorService.addDoctor(clinicId, DoctorDto(newDoctor))

      verify(exactly = 1) { doctorRepository.save(ofType(Doctor::class)) }

    }

    @Test
    internal fun `should return saved Doctor with generated id, given DoctorDto`() {

      assumeThat(newDoctor.id).isNull()

      val doctorDto = doctorService.addDoctor(clinicId, DoctorDto(newDoctor))

      assertThat(doctorDto).isEqualTo(DoctorDto(savedDoctor))

    }

    @Test
    internal fun `should throw ResourceNotFoundException when DoctorRepository save throw DataIntegrityViolationException`() {

      clearMocks(doctorRepository)

      every { doctorRepository.save(ofType(Doctor::class)) }.throws(DataIntegrityViolationException("SQL Exception"))

      assertThatThrownBy { doctorService.addDoctor(clinicId, DoctorDto(newDoctor)) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Clinic found with id : $clinicId")

    }

  }

  @Nested
  @DisplayName("updateDoctor(doctorDto: DoctorDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class UpdateDoctor {

    private val existingDoctorId = dataStore.validId

    private val updatedDoctor = dataStore.createDoctor(existingDoctorId)

    @BeforeEach
    internal fun setup() {

      every { doctorRepository.existsById(ofType(Int::class)) } answers { dataStore.doctorExistById(firstArg()) }

      every { doctorRepository.findById(existingDoctorId) } returns dataStore.findDoctorById(existingDoctorId)

      every { doctorRepository.save(updatedDoctor) } returns dataStore.saveDoctor(updatedDoctor)
    }

    @AfterEach
    internal fun tearDown() {
      clearMocks(doctorRepository)
    }

    @Test
    internal fun `should call DoctorRepository existsById for given doctor id`() {

      doctorService.updateDoctor(existingDoctorId, DoctorDto(updatedDoctor))

      verify(exactly = 1) { doctorRepository.existsById(existingDoctorId) }

    }

    @Test
    internal fun `should call DoctorRepository to find doctor for given doctor id`() {

      doctorService.updateDoctor(existingDoctorId, DoctorDto(updatedDoctor))

      verify(exactly = 1) { doctorRepository.findById(existingDoctorId) }

    }

    @Test
    internal fun `should call DoctorRepository to save doctor if doctor exist for given doctor id`() {

      doctorService.updateDoctor(existingDoctorId, DoctorDto(updatedDoctor))

      verify(exactly = 1) { doctorRepository.save(updatedDoctor) }

    }

    @Test
    internal fun `should return doctor dto with updated doctor details`() {

      val updateDoctorDto = doctorService.updateDoctor(existingDoctorId, DoctorDto(updatedDoctor))

      assertThat(updateDoctorDto).isEqualTo(DoctorDto(updatedDoctor))

    }

    @Test
    internal fun `should throw ResourceNotFoundException when Doctor does not exists`() {

      val nonExistingDoctorId = dataStore.invalidId

      verify(exactly = 0) { doctorRepository.save(updatedDoctor) }

      assertThatThrownBy { doctorService.updateDoctor(nonExistingDoctorId, DoctorDto(updatedDoctor)) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Doctor found with id : $nonExistingDoctorId")

    }

  }

  @Nested
  @DisplayName("deleteDoctor(doctorId: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteClinic {

    @BeforeEach
    internal fun setup() {
      every { doctorRepository.existsById(ofType(Int::class)) } answers { dataStore.doctorExistById(firstArg()) }
    }

    @Test
    internal fun `should call DoctorRepository to delete Doctor by id when Doctor exists`() {

      val existingDoctorId = dataStore.validId

      doctorService.deleteDoctor(existingDoctorId)

      verify(exactly = 1) { doctorRepository.deleteById(existingDoctorId) }

    }

    @Test
    internal fun `should throw ResourceNotFoundException when Doctor does not exists`() {

      val nonExistingDoctorId = dataStore.invalidId

      verify(exactly = 0) { doctorRepository.deleteById(nonExistingDoctorId) }

      assertThatThrownBy { doctorService.deleteDoctor(nonExistingDoctorId) }
        .isInstanceOf(ResourceNotFoundException::class.java)
        .hasMessage("No Doctor found with id : $nonExistingDoctorId")

    }

  }

}
