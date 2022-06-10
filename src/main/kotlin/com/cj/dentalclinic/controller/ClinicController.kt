package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.ClinicDto
import com.cj.dentalclinic.dto.EmptyResponse
import com.cj.dentalclinic.service.ClinicService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/clinics")
class ClinicController(private val clinicService: ClinicService) {

  @GetMapping
  fun getAllClinics() = clinicService.getAllClinics()

  @GetMapping("/{id}")
  fun getClinicById(@PathVariable("id") clinicId: Int) = clinicService.getClinicById(clinicId)

  @PostMapping
  @ResponseStatus(CREATED)
  fun createClinic(@Valid @RequestBody newClinic: ClinicDto) = clinicService.createClinic(ClinicDto(name = newClinic.name))

  @PutMapping("/{id}")
  @ResponseStatus(CREATED)
  fun updateClinic(@PathVariable("id") clinicId: Int, @Valid @RequestBody clinic: ClinicDto) =
    clinicService.updateClinic(clinicId, clinic.copy(id = clinicId))

  @DeleteMapping("/{id}")
  fun deleteClinic(@PathVariable("id") clinicId: Int): EmptyResponse {
    clinicService.deleteClinic(clinicId)
    return EmptyResponse()
  }

}
