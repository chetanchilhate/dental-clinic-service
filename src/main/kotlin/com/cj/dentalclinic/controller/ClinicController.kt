package com.cj.dentalclinic.controller

import com.cj.dentalclinic.dto.Clinic

class ClinicController {

  fun getAllClinics(): List<Clinic> {
    return listOf(Clinic(1, "Sharda Dental Clinic"), Clinic(2, "Smart Dental Clinic"))
  }

}