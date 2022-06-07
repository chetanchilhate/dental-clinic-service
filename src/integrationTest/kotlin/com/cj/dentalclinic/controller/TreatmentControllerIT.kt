package com.cj.dentalclinic.controller

import com.cj.dentalclinic.ClinicDataStore
import com.cj.dentalclinic.repository.TreatmentRepository
import com.cj.dentalclinic.service.ClinicService
import com.cj.dentalclinic.service.TreatmentService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(TreatmentController::class)
@Import(TreatmentService::class,ClinicService::class)
internal class TreatmentControllerIT(@Autowired val mockMvc: MockMvc) {

  private val dataStore = ClinicDataStore()

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

}
