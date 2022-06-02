package com.cj.dentalclinic.service

import com.cj.dentalclinic.dto.Clinic
import com.cj.dentalclinic.repository.ClinicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ClinicService(@Autowired val clinicRepository: ClinicRepository) {

  fun getAllClinics(): List<Clinic> = clinicRepository.findAll()

}
