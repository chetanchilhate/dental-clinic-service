package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.Doctor

data class DoctorDto(

  val id: Int? = null,

  val email: String,

  val firstName: String,

  val middleName: String,

  val lastName: String,

  val qualification: String

) {

  constructor(doctor: Doctor) : this(
    doctor.id,
    doctor.email,
    doctor.firstName,
    doctor.middleName,
    doctor.lastName,
    doctor.qualification
  )

}
