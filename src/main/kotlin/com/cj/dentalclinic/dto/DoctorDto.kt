package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.Doctor
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class DoctorDto(

  val id: Int? = null,

  @field:Email(message = "{error.doctor.email.invalid}")
  val email: String,

  @field:NotBlank(message = "{doctor first name is mandatory}")
  val firstName: String,

  val middleName: String = "",

  @field:NotBlank(message = "{doctor last name is mandatory}")
  val lastName: String,

  @field:NotBlank(message = "{doctor qualification is mandatory}")
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
