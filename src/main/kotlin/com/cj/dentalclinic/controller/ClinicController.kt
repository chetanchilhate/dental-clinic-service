package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.ClinicDto
import com.cj.dentalclinic.service.ClinicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/clinics")
class ClinicController(@Autowired private val clinicService: ClinicService) {

  @GetMapping
  fun getAllClinics(): List<ClinicDto> = clinicService.getAllClinics()

  @GetMapping("/{id}")
  fun getClinicById(@PathVariable("id") clinicId: Int): ClinicDto = clinicService.getClinicById(clinicId)

}
