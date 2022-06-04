package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.Clinic

data class ClinicDto(val id: Int? = null, val name: String) {

  constructor(clinic: Clinic) : this(clinic.id, clinic.name)

}
