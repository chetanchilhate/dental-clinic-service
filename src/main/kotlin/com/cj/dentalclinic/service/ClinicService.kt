package com.cj.dentalclinic.service

import com.cj.dentalclinic.dto.ClinicDto
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.ClinicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ClinicService(@Autowired val clinicRepository: ClinicRepository) {

  fun getAllClinics(): List<ClinicDto> = clinicRepository
    .findAll()
    .stream()
    .map { t -> ClinicDto(t) }
    .toList()

  fun getClinicById(clinicId: Int): ClinicDto = clinicRepository
    .findById(clinicId)
    .map { t -> ClinicDto(t) }
    .orElseThrow { ResourceNotFoundException("Clinic", clinicId) }

}
