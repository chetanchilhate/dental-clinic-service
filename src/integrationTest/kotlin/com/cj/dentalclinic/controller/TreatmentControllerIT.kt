package com.cj.dentalclinic.controller

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.repository.ClinicRepository
import com.cj.dentalclinic.repository.TreatmentRepository
import com.cj.dentalclinic.service.ClinicService
import com.cj.dentalclinic.service.TreatmentService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

const val TREATMENT_BASE_URI = "/api/v1/treatments"

@WebMvcTest(TreatmentController::class)
@Import(TreatmentService::class,ClinicService::class)
internal class TreatmentControllerIT(@Autowired val mockMvc: MockMvc) {

  private val dataStore = ClinicDataStore()

  @MockkBean
  private lateinit var clinicRepository: ClinicRepository

  @MockkBean
  private lateinit var treatmentRepository: TreatmentRepository


  @Nested
  @DisplayName("getAllTreatmentsByClinicId")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetAllTreatmentsByClinicId {

    private val clinicId = 1

    @BeforeEach
    internal fun setup() {
      every { treatmentRepository.findAllByClinicId(clinicId) } returns dataStore.findAllTreatmentsByClinicId(clinicId)
    }

    @Test
    internal fun `should return 200 status code`() {
      mockMvc.get("$CLINIC_BASE_URI/{clinicId}/treatments", clinicId)
        .andExpect {
          status { isOk() }
        }
    }

    @Test
    internal fun `should return json array of all the treatments for given clinic id`() {
      mockMvc.get("$CLINIC_BASE_URI/$clinicId/treatments")
        .andExpect {
          content { json("""[
            |{"id":1,"name":"Root Canal","fee":4500.0},
            |{"id":2,"name":"Regular Checkup","fee":500.0}]""".trimMargin()) }
        }
    }

  }

  @Nested
  @DisplayName("getTreatmentById(id: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetTreatmentById {

    @Test
    internal fun `given id should return 200 status and a json of Treatment`() {

      val id = 2

      every { treatmentRepository.findById(id) } returns dataStore.findTreatmentsById(id)

      mockMvc.get("$TREATMENT_BASE_URI/$id")
        .andExpect {
          status { isOk() }
          content { json("""{"id":$id,"name":"Regular Checkup","fee":500.0}""") }
        }
    }

    @Test
    internal fun `given id should return 404 status and a json of ErrorResponse`() {

      val id = 4

      every { treatmentRepository.findById(id) } returns dataStore.findTreatmentsById(id)

      mockMvc.get("$TREATMENT_BASE_URI/$id")
        .andExpect {
          status { isNotFound() }
          content { json("""{"code": NOT_FOUND,"message":"No Treatment found with id : $id"}""") }
        }
    }

  }

  @Nested
  @DisplayName("addTreatmentToClinic(clinicId: Int, treatmentDto: TreatmentDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class AddTreatmentToClinic {

    private val newTreatment = dataStore.newTreatment()

    @Test
    internal fun `should return 201 status and json of clinic with generated id and given name`() {


      val newTreatmentId = dataStore.newTreatmentId()

      val existingClinic = newTreatment.clinic

      val existingClinicId = existingClinic.id!!

      every { clinicRepository.findById(existingClinicId) } returns dataStore.findClinicById(existingClinicId)

      every { treatmentRepository.save(newTreatment) } returns dataStore.saveTreatment(newTreatment)

      mockMvc.post("$CLINIC_BASE_URI/{clinicId}/treatments", existingClinicId) {

        contentType = APPLICATION_JSON

        content = """{"name":"${newTreatment.name}","fee": ${newTreatment.fee}}"""

      }.andExpect {

        status { isCreated() }

        content { json("""{"id":$newTreatmentId,"name":"${newTreatment.name}","fee": ${newTreatment.fee}}""") }
      }
    }

    @Test
    internal fun `given clinic id should return 404 status and a json of ErrorResponse`() {

      val id = 4

      every { clinicRepository.findById(id) } returns dataStore.findClinicById(id)

      mockMvc.post("$CLINIC_BASE_URI/{clinicId}/treatments", id) {

        contentType = APPLICATION_JSON

        content = """{"name":"${newTreatment.name}","fee": ${newTreatment.fee}}"""

      }.andExpect {
        status { isNotFound() }
        content { json("""{"code": NOT_FOUND,"message":"No Clinic found with id : $id"}""") }
      }
    }

  }

}
