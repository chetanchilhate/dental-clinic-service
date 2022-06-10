package com.cj.dentalclinic.controller

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.entity.Treatment
import com.cj.dentalclinic.repository.ClinicRepository
import com.cj.dentalclinic.repository.TreatmentRepository
import com.cj.dentalclinic.service.TreatmentService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.unmockkAll
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.*

const val TREATMENT_BASE_URI = "/api/v1/treatments"

@WebMvcTest(TreatmentController::class)
@Import(TreatmentService::class)
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

    @AfterEach
    internal fun tearDown() {
      unmockkAll()
    }

    @Test
    internal fun `should return 201 status and json of clinic with generated id and given name`() {

      val newTreatmentId = dataStore.newTreatmentId()

      val clinicId = dataStore.existingClinic().id!!

      every { clinicRepository.getReferenceById(clinicId) } returns dataStore.existingClinic()

      every { treatmentRepository.save(ofType(Treatment::class)) } returns dataStore.saveTreatment(newTreatment)

      mockMvc.post("$CLINIC_BASE_URI/{clinicId}/treatments", clinicId) {

        contentType = APPLICATION_JSON

        content = """{"name":"${newTreatment.name}","fee": ${newTreatment.fee}}"""

      }.andExpect {

        status { isCreated() }

        content { json("""{"id":$newTreatmentId,"name":"${newTreatment.name}","fee": ${newTreatment.fee}}""") }
      }
    }

    @Test
    internal fun `given clinic id should return 404 status and a json of ErrorResponse`() {

      val clinicId = 4

      every { clinicRepository.getReferenceById(clinicId) } returns dataStore.existingClinic()

      every { treatmentRepository.save(ofType(Treatment::class)) }
        .throws(DataIntegrityViolationException("Cannot add or update a child row: a foreign key constraint fails"))

      mockMvc.post("$CLINIC_BASE_URI/{clinicId}/treatments", clinicId) {

        contentType = APPLICATION_JSON

        content = """{"name":"${newTreatment.name}","fee": ${newTreatment.fee}}"""

      }.andExpect {
        status { isNotFound() }
        content { json("""{"code": NOT_FOUND,"message":"No Clinic found with id : $clinicId"}""") }
      }
    }

    @Test
    internal fun `should return 400 status and json of ErrorResponse given treatment name is blank`() {

      mockMvc.post("$CLINIC_BASE_URI/{clinicId}/treatments", 1) {

        contentType = APPLICATION_JSON

        content = """{"name":" ","fee":${newTreatment.fee}}"""

      }.andExpect {

        status { isBadRequest() }

        content { content { contentType(APPLICATION_JSON) } }

      }.andExpect {

        jsonPath("$.code") { value("BAD_REQUEST") }

        jsonPath("$.message") { containsString("treatment name is mandatory") }

      }

    }

    @Test
    internal fun `should return 400 status and json of ErrorResponse given treatment fee is less than 100`() {

      mockMvc.post("$CLINIC_BASE_URI/{clinicId}/treatments", 1) {

        contentType = APPLICATION_JSON

        content = """{"name":"${newTreatment.name}","fee":10.00}"""

      }.andExpect {

        status { isBadRequest() }

        content { content { contentType(APPLICATION_JSON) } }

      }.andExpect {

        jsonPath("$.code") { value("BAD_REQUEST") }

        jsonPath("$.message") { containsString("treatment fee must be greater or equal to 100.00") }

      }

    }

  }

  @Nested
  @DisplayName("updateTreatment(id: Int, treatment: TreatmentDto)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  internal inner class UpdateTreatment {

    private val id = 3

    private val updatedTreatment = dataStore.findTreatmentsById(id).get()

    @BeforeEach
    internal fun setUp() {
      clearMocks(clinicRepository)
    }

    @Test
    internal fun `should return 201 status and json of updated treatment when treatment exists`() {

      every { treatmentRepository.existsById(id) } returns true

      every { treatmentRepository.findById(id) } returns dataStore.findTreatmentsById(id)

      every { treatmentRepository.save(updatedTreatment) } returns updatedTreatment

      mockMvc.put("$TREATMENT_BASE_URI/{id}", id) {

        contentType = APPLICATION_JSON

        content = """{"id":$id,"name":"${updatedTreatment.name}","fee": ${updatedTreatment.fee}}}"""

      }.andExpect {

        status { isCreated() }

        content { json( """{"id":$id,"name":"${updatedTreatment.name}","fee": ${updatedTreatment.fee}}}""") }
      }
    }

    @Test
    internal fun `should return 404 status and json of ErrorResponse when treatment does not exists`() {

      every { treatmentRepository.existsById(id) } returns false

      mockMvc.put("$TREATMENT_BASE_URI/$id") {

        contentType = APPLICATION_JSON

        content = """{"id":$id,"name":"${updatedTreatment.name}","fee": ${updatedTreatment.fee}}}"""

      }.andExpect {

        status { isNotFound() }

        content { json("""{"code": NOT_FOUND,"message":"No Treatment found with id : $id"}""") }
      }
    }

    @Test
    internal fun `should return 400 status and json of ErrorResponse given treatment name is blank`() {

      mockMvc.put("$TREATMENT_BASE_URI/$id") {

        contentType = APPLICATION_JSON

        content = """{"id":$id,"name":" ","fee":${updatedTreatment.fee}}"""

      }.andExpect {

        status { isBadRequest() }

        content { content { contentType(APPLICATION_JSON) } }

      }.andExpect {

        jsonPath("$.code") { value("BAD_REQUEST") }

        jsonPath("$.message") { containsString("treatment name is mandatory") }

      }

    }

    @Test
    internal fun `should return 400 status and json of ErrorResponse given treatment fee is less than 100`() {

      mockMvc.put("$TREATMENT_BASE_URI/$id") {

        contentType = APPLICATION_JSON

        content = """{"id":$id,"name":"${updatedTreatment.name}","fee":10.00}"""

      }.andExpect {

        status { isBadRequest() }

        content { content { contentType(APPLICATION_JSON) } }

      }.andExpect {

        jsonPath("$.code") { value("BAD_REQUEST") }

        jsonPath("$.message") { containsString("treatment fee must be greater or equal to 100.00") }

      }

    }

  }

  @Nested
  @DisplayName("deleteTreatment(id: Int)")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class DeleteTreatment {

    private val id = 4

    @BeforeEach
    internal fun setUp() {
      clearMocks(treatmentRepository)
    }

    @Test
    internal fun `should return 200 status and empty json when treatment exists`() {

      every { treatmentRepository.existsById(id) } returns true

      justRun { treatmentRepository.deleteById(id) }

      mockMvc.delete("$TREATMENT_BASE_URI/{id}", id)
        .andExpect {

          status { isOk() }

          content { json("{}") }
        }
    }

    @Test
    internal fun `should return 404 status and json of ErrorResponse when treatment does not exists`() {

      every { treatmentRepository.existsById(id) } returns false

      mockMvc.delete("$TREATMENT_BASE_URI/{id}", id)
        .andExpect {

          status { isNotFound() }

          content { json("""{"code": NOT_FOUND,"message":"No Treatment found with id : $id"}""") }
        }
    }

  }

}
