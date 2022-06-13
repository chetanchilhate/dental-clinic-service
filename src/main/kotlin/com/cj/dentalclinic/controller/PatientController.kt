package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.EmptyResponse
import com.cj.dentalclinic.dto.PatientDto
import com.cj.dentalclinic.service.PatientService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class PatientController(private val patientService: PatientService) {


  @GetMapping("/doctors/{doctorId}/patients")
  fun getAllPatientsByDoctorId(@PathVariable("doctorId") doctorId: Int) = patientService.getAllPatientsByDoctorId(doctorId)

  @GetMapping("/patients/{id}")
  fun getPatientById(@PathVariable("id") patientId: Int) = patientService.getPatientById(patientId)

  @PostMapping("/doctors/{doctorId}/patients")
  @ResponseStatus(CREATED)
  fun addPatient(@PathVariable("doctorId") doctorId: Int, @Valid @RequestBody patientDto: PatientDto) = patientService.addPatient(doctorId, patientDto)

  @PutMapping("/patients/{id}")
  @ResponseStatus(CREATED)
  fun updatePatient(@PathVariable("id") patientId: Int, @Valid @RequestBody patientDto: PatientDto) = patientService.updatePatient(patientId, patientDto)

  @DeleteMapping("/patients/{id}")
  fun deletePatient(@PathVariable("id") patientId: Int): EmptyResponse {
    patientService.deletePatient(patientId)
    return EmptyResponse()
  }

}
