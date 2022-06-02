package com.cj.dentalclinic.controller

import com.cj.dentalclinic.service.ClinicService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

const val CLINIC_BASE_URI = "/api/v1/clinics"

@WebMvcTest(ClinicController::class)
@Import(ClinicService::class)
internal class ClinicControllerITest(@Autowired val mockMvc: MockMvc) {

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