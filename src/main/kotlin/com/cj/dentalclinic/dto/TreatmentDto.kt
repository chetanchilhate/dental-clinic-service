package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.Treatment
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class TreatmentDto(

  val id: Int? = null,

  @field:NotBlank(message = "{error.treatment.name.mandatory}")
  val name: String,

  @field:Min(100, message = "{error.treatment.fee.greater-or-equal}")
  val fee: Double

) {

  constructor(treatment: Treatment) : this(treatment.id, treatment.name, treatment.fee)

}
