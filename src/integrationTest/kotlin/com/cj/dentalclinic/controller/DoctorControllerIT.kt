package com.cj.dentalclinic.controller

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.dto.DoctorDto
import com.cj.dentalclinic.repository.ClinicRepository
import com.cj.dentalclinic.repository.DoctorRepository
import com.cj.dentalclinic.service.DoctorService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.Matchers
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
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

          content { contentType(MediaType.APPLICATION_JSON) }

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

}
