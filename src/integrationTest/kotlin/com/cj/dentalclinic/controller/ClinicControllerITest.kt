package com.cj.dentalclinic.controller

import com.cj.dentalclinic.entity.Clinic
import com.cj.dentalclinic.repository.ClinicRepository
import com.cj.dentalclinic.service.ClinicService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

const val CLINIC_BASE_URI = "/api/v1/clinics"

@WebMvcTest(ClinicController::class)
@Import(ClinicService::class)
internal class ClinicControllerITest(@Autowired val mockMvc: MockMvc) {

  private val clinics = listOf(Clinic(1, "Sharda Dental Clinic"), Clinic(2, "Smart Dental Clinic"))

  @MockkBean
  private lateinit var clinicRepository: ClinicRepository

  @Nested
  @DisplayName("getAllClinics")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetAllClinics {

    @BeforeEach
    fun setup() {
      every { clinicRepository.findAll() } returns clinics
    }

    @Test
    fun `should return 200 status code`() {
      mockMvc.get(CLINIC_BASE_URI)
        .andExpect {
          status { isOk() }
        }
    }

    @Test
    fun `should return json array of all the clinics`() {
      mockMvc.get(CLINIC_BASE_URI)
        .andExpect {
          content { json("""[{"id":1,"name":"Sharda Dental Clinic"},{"id":2,"name":"Smart Dental Clinic"}]""") }
        }
    }

  }

  @Nested
  @DisplayName("getClinicById(id: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetClinicById {

    @Test
    fun `given id should return 200 status and a json of Clinic`() {

      val id = 2

      every { clinicRepository.findById(id) } returns findClinicById(id)

      mockMvc.get("$CLINIC_BASE_URI/$id")
        .andExpect {
          status { isOk() }
          content { json("""{"id":$id,"name":"Smart Dental Clinic"}""") }
        }
    }

    @Test
    fun `given id should return 404 status and a json of ErrorResponse`() {

      val id = 4

      every { clinicRepository.findById(id) } returns findClinicById(id)

      mockMvc.get("$CLINIC_BASE_URI/$id")
        .andExpect {
          status { isNotFound() }
          content { json("""{"code": NOT_FOUND,"message":"No Clinic found with id : $id"}""") }
        }
    }

    private fun findClinicById(id: Int) = clinics.stream().filter { t -> t.id == id }.findFirst()

  }

  @Nested
  @DisplayName("createClinic(newClinic: ClinicDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class CreateClinic {

    @Test
    fun `should return 201 status and json of clinic with generated id and given name`() {

      val newClinicId = 4

      val newClinic = Clinic(name = "Sujata Dental Clinic")

      every { clinicRepository.save(newClinic) } returns newClinic.copy(newClinicId)

      mockMvc.post(CLINIC_BASE_URI) {

        contentType = MediaType.APPLICATION_JSON

        content = """{"name":"${newClinic.name}"}"""

      }.andExpect {

          status { isCreated() }

          content { json("""{"id":$newClinicId,"name":"${newClinic.name}"}""") }
        }
    }

  }

}
