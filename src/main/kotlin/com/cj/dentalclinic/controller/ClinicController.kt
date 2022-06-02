package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.Clinic
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/clinics")
class ClinicController {

  @GetMapping
  fun getAllClinics(): List<Clinic> {
    return listOf(Clinic(1, "Sharda Dental Clinic"), Clinic(2, "Smart Dental Clinic"))
  }

}