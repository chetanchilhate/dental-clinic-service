package com.cj.dentalclinic.service

import com.cj.dentalclinic.dto.PatientDto
import com.cj.dentalclinic.entity.Patient
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.DoctorRepository
import com.cj.dentalclinic.repository.PatientRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PatientService(private val patientRepository: PatientRepository, private val doctorRepository: DoctorRepository) {

  fun getAllPatientsByDoctorId(doctorId: Int): List<PatientDto> = patientRepository
    .findPatientsByDoctorId(doctorId)
    .map { PatientDto(it) }
    .toList()

  fun getPatientById(patientId: Int): PatientDto = patientRepository
    .findById(patientId)
    .map { PatientDto(it) }
    .orElseThrow { ResourceNotFoundException("Patient", patientId) }

  fun addPatient(doctorId: Int, patientDto: PatientDto): PatientDto {
    val doctor = doctorRepository.getReferenceById(doctorId)
    return  try {
      PatientDto(patientRepository.save(Patient(patientDto, doctor)))
    } catch (e: DataIntegrityViolationException) {
      throw ResourceNotFoundException("Doctor", doctorId)
    }
  }

  fun updatePatient(patientId: Int, patientDto: PatientDto): PatientDto {
    if (patientRepository.existsById(patientId).not()) {
      throw ResourceNotFoundException("Patient", patientId)
    }
    val existingPatient = patientRepository.findById(patientId).get()
    return PatientDto(patientRepository.save(existingPatient.update(patientDto)))
  }

  fun deletePatient(patientId: Int) {
    if (patientRepository.existsById(patientId)) patientRepository.deleteById(patientId)
    else throw ResourceNotFoundException("Patient", patientId)
  }

}
