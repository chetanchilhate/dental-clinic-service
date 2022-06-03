package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.Clinic

data class ClinicDto(var id: Int?, val name: String) {

  constructor(clinic: Clinic) : this(clinic.id, clinic.name)

}
