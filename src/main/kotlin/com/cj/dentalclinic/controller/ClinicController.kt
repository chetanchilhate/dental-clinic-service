package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.ClinicDto
import com.cj.dentalclinic.service.ClinicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/clinics")
class ClinicController(@Autowired private val clinicService: ClinicService) {

  @GetMapping
  fun getAllClinics(): List<ClinicDto> = clinicService.getAllClinics()

  @GetMapping("/{id}")
  fun getClinicById(@PathVariable("id") clinicId: Int): ClinicDto = clinicService.getClinicById(clinicId)

  @PostMapping
  @ResponseStatus( HttpStatus.CREATED )
  fun createClinic(@RequestBody newClinic: ClinicDto): ClinicDto = clinicService.createClinic(ClinicDto(name = newClinic.name))

  fun updateClinic(id: Int, clinic: ClinicDto): ClinicDto = clinicService.updateClinic(id, clinic)

}
