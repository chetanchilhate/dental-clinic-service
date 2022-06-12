package com.cj.dentalclinic.controller

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.dto.DoctorDto
import com.cj.dentalclinic.entity.Doctor
import com.cj.dentalclinic.repository.ClinicRepository
import com.cj.dentalclinic.repository.DoctorRepository
import com.cj.dentalclinic.service.DoctorService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.unmockkAll
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.*
import java.util.Optional.ofNullable

const val DOCTOR_BASE_URI = "/api/v1/doctors"

@WebMvcTest(DoctorController::class)
@Import(DoctorService::class)
class DoctorControllerIT(@Autowired val mockMvc: MockMvc) {

  private val dataStore = ClinicDataStore()

  @MockkBean
  private lateinit var clinicRepository: ClinicRepository

  @MockkBean
  private lateinit var doctorRepository: DoctorRepository

  private val invalidDoctorJSON = """
      |{
        |"email":" ",
        |"firstName":" ",
        |"middleName":" ",
        |"lastName":" ",
        |"qualification": " "
      |}""".trimMargin()

  @Nested
  @DisplayName("getAllDoctorsByClinicId")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetAllDoctorsByClinicId {

    private val clinicId = dataStore.parentIdWithData

    private val doctors = dataStore.findAllDoctorsByClinicId(clinicId)

    private val size = doctors.size

    @BeforeEach
    internal fun setup() {
      every { doctorRepository.findAllByClinicId(clinicId) } returns doctors
    }

    @Test
    internal fun `should return 200 status code`() {
      mockMvc.get("$CLINIC_BASE_URI/{clinicId}/doctors", clinicId)
        .andDo { print() }
        .andExpect {
          status { isOk() }
        }
    }

    @Test
    internal fun `should return json array of all the doctors for given clinic id`() {
      mockMvc.get("$CLINIC_BASE_URI/$clinicId/doctors")
        .andExpect {

          content { contentType(APPLICATION_JSON) }

        }.andExpect {

          jsonPath("$") { isArray() }

          jsonPath("$") { value(Matchers.hasSize<DoctorDto>(size)) }

          jsonPath("$[0].id") { value(lessThanOrEqualTo(dataStore.maxId)) }

          jsonPath("$[0].email") { isNotEmpty() }

          jsonPath("$[0].firstName") { isNotEmpty() }

          jsonPath("$[0].lastName") { isNotEmpty() }

          jsonPath("$[0].qualification") { isNotEmpty() }

        }
    }

  }

  @Nested
  @DisplayName("getDoctorById(id: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetDoctorById {

    @Test
    internal fun `given id should return 200 status and a json of Doctor`() {

      val id = dataStore.validId

      val existingDoctor = dataStore.findDoctorById(id).get()

      every { doctorRepository.findById(id) } returns ofNullable(existingDoctor)

      mockMvc.get("$DOCTOR_BASE_URI/$id")
        .andExpect {
          status { isOk() }
          content { json("""{
            |"id":$id,
            |"email":"${existingDoctor.email}",
            |"firstName":"${existingDoctor.firstName}",
            |"middleName":"${existingDoctor.middleName}",
            |"lastName":"${existingDoctor.lastName}",
            |"qualification": "${existingDoctor.qualification}"
            |}""".trimMargin()) }
        }
    }

    @Test
    internal fun `given id should return 404 status and a json of ErrorResponse`() {

      val id = dataStore.invalidId

      every { doctorRepository.findById(id) } returns dataStore.findDoctorById(id)

      mockMvc.get("$DOCTOR_BASE_URI/$id")
        .andExpect {
          status { isNotFound() }
          content { json("""{"code": NOT_FOUND,"message":"No Doctor found with id : $id"}""") }
        }
    }

  }

  @Nested
  @DisplayName("addDoctorToClinic(clinicId: Int, doctorDto: DoctorDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class AddDoctorToClinic {

    private val newDoctor = dataStore.newDoctor()

    private val clinicId = dataStore.validId

    private val newDoctorJSON = """
      |{
        |"email":"${newDoctor.email}",
        |"firstName":"${newDoctor.firstName}",
        |"middleName":"${newDoctor.middleName}",
        |"lastName":"${newDoctor.lastName}",
        |"qualification": "${newDoctor.qualification}"
      |}""".trimMargin()

    @AfterEach
    internal fun tearDown() {
      unmockkAll()
    }

    @Test
    internal fun `should return 201 status and json of clinic with generated id and given name`() {


      every { clinicRepository.getReferenceById(clinicId) } returns dataStore.findClinicById(clinicId).get()

      every { doctorRepository.save(ofType(Doctor::class)) } returns dataStore.saveDoctor(newDoctor)

      mockMvc.post("$CLINIC_BASE_URI/{clinicId}/doctors", clinicId) {

        contentType = APPLICATION_JSON

        content = newDoctorJSON

      }.andExpect {

        status { isCreated() }

        content { contentType(APPLICATION_JSON) }

      }.andExpect {

        jsonPath("$.id") { isNumber() }

        jsonPath("$.email") { isNotEmpty() }

        jsonPath("$.firstName") { isNotEmpty() }

        jsonPath("$.lastName") { isNotEmpty() }

        jsonPath("$.qualification") { isNotEmpty() }

      }
    }

    @Test
    internal fun `given clinic id should return 404 status and a json of ErrorResponse`() {

      val clinicId = dataStore.invalidId

      every { clinicRepository.getReferenceById(clinicId) } returns dataStore.createClinic(clinicId)

      every { doctorRepository.save(ofType(Doctor::class)) }
        .throws(DataIntegrityViolationException("Cannot add or update a child row: a foreign key constraint fails"))

      mockMvc.post("$CLINIC_BASE_URI/{clinicId}/doctors", clinicId) {

        contentType = APPLICATION_JSON

        content = newDoctorJSON

      }.andExpect {
        status { isNotFound() }
        content { json("""{"code": NOT_FOUND,"message":"No Clinic found with id : $clinicId"}""") }
      }
    }

    @Test
    internal fun `should return 400 status and json of ErrorResponse given any invalid field in request`() {

      mockMvc.post("$CLINIC_BASE_URI/{clinicId}/doctors", dataStore.invalidId) {

        contentType = APPLICATION_JSON

        content = invalidDoctorJSON

      }.andExpect {

        status { isBadRequest() }

        content { content { contentType(APPLICATION_JSON) } }

      }.andExpect {

        jsonPath("$.code") { value("BAD_REQUEST") }

        jsonPath("$.message") { value(containsString("doctor email is invalid")) }

        jsonPath("$.message") { value(containsString("doctor first name is mandatory")) }

        jsonPath("$.message") { value(containsString("doctor last name is mandatory")) }

        jsonPath("$.message") { value(containsString("doctor qualification is mandatory")) }

      }

    }

  }

  @Nested
  @DisplayName("updateDoctor(id: Int, doctor: DoctorDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  internal inner class UpdateDoctor {

    private val id = dataStore.validId

    private val updatedDoctor = dataStore.createDoctor(id)

    private val updatedDoctorJSON = """
      |{
        |"id":${updatedDoctor.id},
        |"email":"${updatedDoctor.email}",
        |"firstName":"${updatedDoctor.firstName}",
        |"middleName":"${updatedDoctor.middleName}",
        |"lastName":"${updatedDoctor.lastName}",
        |"qualification": "${updatedDoctor.qualification}"
      |}""".trimMargin()

    @BeforeEach
    internal fun setUp() {
      clearMocks(clinicRepository)
    }

    @Test
    internal fun `should return 201 status and json of updated doctor when doctor exists`() {

      every { doctorRepository.existsById(id) } returns true

      every { doctorRepository.findById(id) } returns dataStore.findDoctorById(id)

      every { doctorRepository.save(updatedDoctor) } returns dataStore.saveDoctor(updatedDoctor)

      mockMvc.put("$DOCTOR_BASE_URI/{id}", id) {

        contentType = APPLICATION_JSON

        content = updatedDoctorJSON

      }.andExpect {

        status { isCreated() }

        content { json(updatedDoctorJSON) }
      }
    }

    @Test
    internal fun `should return 404 status and json of ErrorResponse when doctor does not exists`() {

      every { doctorRepository.existsById(id) } returns false

      mockMvc.put("$DOCTOR_BASE_URI/$id") {

        contentType = APPLICATION_JSON

        content = updatedDoctorJSON

      }.andExpect {

        status { isNotFound() }

        content { json("""{"code": NOT_FOUND,"message":"No Doctor found with id : $id"}""") }
      }
    }

    @Test
    internal fun `should return 400 status and json of ErrorResponse given any invalid field in request`() {

      mockMvc.put("$DOCTOR_BASE_URI/$id") {

        contentType = APPLICATION_JSON

        content = invalidDoctorJSON

      }.andExpect {

        status { isBadRequest() }

        content { content { contentType(APPLICATION_JSON) } }

      }.andExpect {

        jsonPath("$.code") { value("BAD_REQUEST") }

        jsonPath("$.message") { value(containsString("doctor email is invalid")) }

        jsonPath("$.message") { value(containsString("doctor first name is mandatory")) }

        jsonPath("$.message") { value(containsString("doctor last name is mandatory")) }

        jsonPath("$.message") { value(containsString("doctor qualification is mandatory")) }

      }

    }

  }

  @Nested
  @DisplayName("deleteDoctor(id: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteDoctor {

    private val id = dataStore.validId

    @BeforeEach
    internal fun setUp() {
      clearMocks(doctorRepository)
    }

    @Test
    internal fun `should return 200 status and empty json when doctor exists`() {

      every { doctorRepository.existsById(id) } returns true

      justRun { doctorRepository.deleteById(id) }

      mockMvc.delete("$DOCTOR_BASE_URI/{id}", id)
        .andExpect {

          status { isOk() }

          content { json("{}") }
        }
    }

    @Test
    internal fun `should return 404 status and json of ErrorResponse when doctor does not exists`() {

      every { doctorRepository.existsById(id) } returns false

      mockMvc.delete("$DOCTOR_BASE_URI/{id}", id)
        .andExpect {

          status { isNotFound() }

          content { json("""{"code": NOT_FOUND,"message":"No Doctor found with id : $id"}""") }
        }
    }

  }

}
