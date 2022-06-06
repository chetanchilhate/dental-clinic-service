package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.Treatment

data class TreatmentDto(val id: Int? = null, val name: String, val fee: Double) {

  constructor(treatment: Treatment) : this(treatment.id, treatment.name, treatment.fee)

}
