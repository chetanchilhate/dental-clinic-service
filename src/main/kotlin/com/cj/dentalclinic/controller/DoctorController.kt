package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.DoctorDto
import com.cj.dentalclinic.service.DoctorService

class DoctorController(private val doctorService: DoctorService) {

  fun getAllDoctors(clinicId: Int) = doctorService.getAllDoctors(clinicId)

  fun getDoctorById(doctorId: Int) = doctorService.getDoctorById(doctorId)

  fun addDoctor(clinicId: Int, doctorDto: DoctorDto) = doctorService.addDoctor(clinicId, doctorDto)

  fun updateDoctor(doctorId: Int, doctorDto: DoctorDto) = doctorService.updateDoctor(doctorId, doctorDto)

  fun deleteDoctor(doctorId: Int) = doctorService.deleteDoctor(doctorId)

}
