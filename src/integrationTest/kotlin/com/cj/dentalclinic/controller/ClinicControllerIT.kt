package com.cj.dentalclinic.controller

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.entity.Clinic
import com.cj.dentalclinic.repository.ClinicRepository
import com.cj.dentalclinic.service.ClinicService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import org.hamcrest.Matchers
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.*

const val CLINIC_BASE_URI = "/api/v1/clinics"

@WebMvcTest(ClinicController::class)
@Import(ClinicService::class)
internal class ClinicControllerIT(@Autowired private val mockMvc: MockMvc) {

  private val dataStore = ClinicDataStore()

  @MockkBean
  private lateinit var clinicRepository: ClinicRepository

  @Nested
  @DisplayName("getAllClinics")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetAllClinics {

    @BeforeEach
    internal fun setup() {
      every { clinicRepository.findAll() } returns dataStore.findAllClinics()
    }

    @Test
    internal fun `should return 200 status code`() {
      mockMvc.get(CLINIC_BASE_URI)
        .andExpect {
          status { isOk() }
        }
    }

    @Test
    internal fun `should return json array of all the clinics`() {
      mockMvc.get(CLINIC_BASE_URI)
        .andExpect {
          content { json("""[
            |{"id":1,"name":"Sharda Dental Clinic"},
            |{"id":2,"name":"Smart Dental Clinic"},
            |{"id":3,"name":"Sonal Dental Clinic"}]""".trimMargin()) }
        }
    }

  }

  @Nested
  @DisplayName("getClinicById(id: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetClinicById {

    @Test
    internal fun `given id should return 200 status and a json of Clinic`() {

      val id = 2

      every { clinicRepository.findById(id) } returns dataStore.findClinicById(id)

      mockMvc.get("$CLINIC_BASE_URI/$id")
        .andExpect {
          status { isOk() }
          content { json("""{"id":$id,"name":"Smart Dental Clinic"}""") }
        }
    }

    @Test
    internal fun `given id should return 404 status and a json of ErrorResponse`() {

      val id = 4

      every { clinicRepository.findById(id) } returns dataStore.findClinicById(id)

      mockMvc.get("$CLINIC_BASE_URI/$id")
        .andExpect {
          status { isNotFound() }
          content { json("""{"code": NOT_FOUND,"message":"No Clinic found with id : $id"}""") }
        }
    }

  }

  @Nested
  @DisplayName("createClinic(newClinic: ClinicDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class CreateClinic {

    @Test
    internal fun `should return 201 status and json of clinic with generated id and given name`() {

      val newClinicId = dataStore.newClinicId()

      val newClinic = dataStore.newClinic()

      every { clinicRepository.save(ofType(Clinic::class)) } returns dataStore.saveClinic(newClinic)

      mockMvc.post(CLINIC_BASE_URI) {

        contentType = APPLICATION_JSON

        content = """{"name":"${newClinic.name}"}"""

      }.andExpect {

          status { isCreated() }

          content { json("""{"id":$newClinicId,"name":"${newClinic.name}"}""") }
        }
    }

    @Test
    internal fun `should return 400 status and json of ErrorResponse given clinic name is blank`() {

      mockMvc.post(CLINIC_BASE_URI) {

        contentType = APPLICATION_JSON

        content = """{"name":"           \n     "}"""

      }.andExpect {

        status { isBadRequest() }

        content { content { contentType(APPLICATION_JSON) } }

      }.andExpect {

        jsonPath("$.code") { value("BAD_REQUEST") }

        jsonPath("$.message") { Matchers.containsString("clinic name is mandatory") }

      }

    }

  }

  @Nested
  @DisplayName("updateClinic(id: Int, clinic: ClinicDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class UpdateClinic {

    private val id = 4

    private val updatedClinic = Clinic(id, "Sujata Dental Clinic")

    @BeforeEach
    internal fun setUp() {
      clearMocks(clinicRepository)
    }

    @Test
    internal fun `should return 201 status and json of updated clinic when clinic exists`() {

      every { clinicRepository.existsById(id) } returns true

      every { clinicRepository.save(updatedClinic) } returns updatedClinic

      mockMvc.put("$CLINIC_BASE_URI/$id") {

        contentType = APPLICATION_JSON

        content = """{"id":$id,"name":"${updatedClinic.name}"}"""

      }.andExpect {

        status { isCreated() }

        content { json("""{"id":$id,"name":"${updatedClinic.name}"}""") }
      }
    }

    @Test
    internal fun `should return 404 status and json of ErrorResponse when clinic does not exists`() {

      every { clinicRepository.existsById(id) } returns false

      mockMvc.put("$CLINIC_BASE_URI/$id") {

        contentType = APPLICATION_JSON

        content = """{"id":$id,"name":"${updatedClinic.name}"}"""

      }.andExpect {

        status { isNotFound() }

        content { json("""{"code": NOT_FOUND,"message":"No Clinic found with id : $id"}""") }
      }
    }

    @Test
    internal fun `should return 400 status and json of ErrorResponse given clinic name is blank`() {

      mockMvc.put("$CLINIC_BASE_URI/$id") {

        contentType = APPLICATION_JSON

        content = """{"id":$id,"name":" \t\r "}"""

      }.andExpect {

        status { isBadRequest() }

        content { content { contentType(APPLICATION_JSON) } }

      }.andExpect {

        jsonPath("$.code") { value("BAD_REQUEST") }

        jsonPath("$.message") { Matchers.containsString("clinic name is mandatory") }

      }

    }

  }

  @Nested
  @DisplayName("deleteClinic(id: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteClinic {

    private val id = 4

    @BeforeEach
    internal fun setUp() {
      clearMocks(clinicRepository)
    }

    @Test
    internal fun `should return 200 status and empty json when clinic exists`() {

      every { clinicRepository.existsById(id) } returns true

      justRun { clinicRepository.deleteById(id) }

      mockMvc.delete("$CLINIC_BASE_URI/$id")
        .andExpect {

        status { isOk() }

        content { json("{}") }
      }
    }

    @Test
    internal fun `should return 404 status and json of ErrorResponse when clinic does not exists`() {

      every { clinicRepository.existsById(id) } returns false

      mockMvc.delete("$CLINIC_BASE_URI/$id")
        .andExpect {

        status { isNotFound() }

        content { json("""{"code": NOT_FOUND,"message":"No Clinic found with id : $id"}""") }
      }
    }

  }

}
