package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.DoctorDto
import com.cj.dentalclinic.service.DoctorService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class DoctorController(private val doctorService: DoctorService) {

  @GetMapping("/clinics/{clinicId}/doctors")
  fun getAllDoctors(@PathVariable("clinicId") clinicId: Int) = doctorService.getAllDoctors(clinicId)

  @GetMapping("/doctors/{id}")
  fun getDoctorById(@PathVariable("id") doctorId: Int) = doctorService.getDoctorById(doctorId)

  @PostMapping("/clinics/{clinicId}/doctors")
  @ResponseStatus(CREATED)
  fun addDoctor(@PathVariable("clinicId") clinicId: Int, @Valid @RequestBody doctorDto: DoctorDto) = doctorService.addDoctor(clinicId, doctorDto)

  @PutMapping("/doctors/{id}")
  @ResponseStatus(CREATED)
  fun updateDoctor(@PathVariable("id") doctorId: Int, @Valid @RequestBody doctorDto: DoctorDto) = doctorService.updateDoctor(doctorId, doctorDto)

  fun deleteDoctor(doctorId: Int) = doctorService.deleteDoctor(doctorId)

}
