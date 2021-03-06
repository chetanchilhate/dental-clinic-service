package com.cj.dentalclinic.service

import com.cj.dentalclinic.dto.ClinicDto
import com.cj.dentalclinic.entity.Clinic
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.ClinicRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ClinicService(private val clinicRepository: ClinicRepository) {

  fun getAllClinics(): List<ClinicDto> = clinicRepository
    .findAll()
    .map { ClinicDto(it) }
    .toList()

  fun getClinicById(clinicId: Int): ClinicDto = clinicRepository
    .findById(clinicId)
    .map { ClinicDto(it) }
    .orElseThrow { ResourceNotFoundException("Clinic", clinicId) }

  fun createClinic(newClinic: ClinicDto) = ClinicDto(clinicRepository.save(Clinic(newClinic)))

  fun updateClinic(clinicId: Int, updatedClinic: ClinicDto) =
    if (clinicRepository.existsById(clinicId)) ClinicDto(clinicRepository.save(Clinic(updatedClinic)))
    else throw ResourceNotFoundException("Clinic", clinicId)

  fun deleteClinic(clinicId: Int) =
    if (clinicRepository.existsById(clinicId)) clinicRepository.deleteById(clinicId)
    else throw ResourceNotFoundException("Clinic", clinicId)

}
