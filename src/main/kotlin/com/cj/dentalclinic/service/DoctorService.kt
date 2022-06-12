package com.cj.dentalclinic.service

import com.cj.dentalclinic.dto.DoctorDto
import com.cj.dentalclinic.entity.Doctor
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.ClinicRepository
import com.cj.dentalclinic.repository.DoctorRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DoctorService(private val doctorRepository: DoctorRepository, private val clinicRepository: ClinicRepository) {

  fun getAllDoctors(clinicId: Int): List<DoctorDto> = doctorRepository
    .findAllByClinicId(clinicId)
    .map { DoctorDto(it) }
    .toList()

  fun getDoctorById(doctorId: Int): DoctorDto = doctorRepository
    .findById(doctorId)
    .map { DoctorDto(it) }
    .orElseThrow { ResourceNotFoundException("Doctor", doctorId) }

  fun addDoctor(clinicId: Int, doctorDto: DoctorDto): DoctorDto {

    val clinic = clinicRepository.getReferenceById(clinicId)

    return try {

      val newDoctor = Doctor(doctorDto, clinic)

      DoctorDto(doctorRepository.save(newDoctor))

    } catch (e: DataIntegrityViolationException) {

      throw ResourceNotFoundException("Clinic", clinicId)
    }

  }

  fun updateDoctor(doctorId: Int, doctorDto: DoctorDto): DoctorDto {

    if (doctorRepository.existsById(doctorId).not()) {
      throw ResourceNotFoundException("Doctor", doctorId)
    }

    val existingDoctor = doctorRepository.findById(doctorId).get()

    return DoctorDto(doctorRepository.save(existingDoctor.update(doctorDto)))
  }

  fun deleteDoctor(doctorId: Int) {
    if (doctorRepository.existsById(doctorId)) doctorRepository.deleteById(doctorId)
    else throw ResourceNotFoundException("Doctor", doctorId)
  }

}
