package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.Clinic
import javax.validation.constraints.NotBlank

data class ClinicDto(

  val id: Int? = null,

  @field:NotBlank(message = "{error.clinic.name.mandatory}")
  val name: String

) {

  constructor(clinic: Clinic) : this(clinic.id, clinic.name)

}
