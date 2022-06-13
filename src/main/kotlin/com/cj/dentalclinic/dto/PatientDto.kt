package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.Patient

data class PatientDto(

  val id: Int? = null,

  val firstName: String,

  val middleName: String = "",

  val lastName: String,

  val age: Byte,

  val sex: Sex,

  val mobile: String = "",

  val problem: String = "",
) {

  constructor(patient: Patient) : this(
    id = patient.id,
    firstName = patient.firstName,
    middleName = patient.middleName,
    lastName = patient.lastName,
    age = patient.age,
    sex = patient.sex,
    mobile = patient.mobile,
    problem = patient.problem
  )

}
