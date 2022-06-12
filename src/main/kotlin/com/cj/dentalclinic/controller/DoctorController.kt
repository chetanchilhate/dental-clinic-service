package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.DoctorDto
import com.cj.dentalclinic.service.DoctorService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DoctorController(private val doctorService: DoctorService) {

  @GetMapping("/clinics/{clinicId}/doctors")
  fun getAllDoctors(@PathVariable("clinicId") clinicId: Int) = doctorService.getAllDoctors(clinicId)

  fun getDoctorById(doctorId: Int) = doctorService.getDoctorById(doctorId)

  fun addDoctor(clinicId: Int, doctorDto: DoctorDto) = doctorService.addDoctor(clinicId, doctorDto)

  fun updateDoctor(doctorId: Int, doctorDto: DoctorDto) = doctorService.updateDoctor(doctorId, doctorDto)

  fun deleteDoctor(doctorId: Int) = doctorService.deleteDoctor(doctorId)

}
