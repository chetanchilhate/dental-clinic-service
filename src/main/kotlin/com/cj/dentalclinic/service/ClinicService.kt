package com.cj.dentalclinic.service

import com.cj.dentalclinic.dto.Clinic
import org.springframework.stereotype.Service

@Service
class ClinicService {

  fun getAllClinics(): List<Clinic> = listOf(Clinic(1, "Sharda Dental Clinic"), Clinic(2, "Smart Dental Clinic"))

}
