package com.cj.dentalclinic.entity

import java.time.LocalDateTime

open class BillDetailView(
  val clinicName: String,
  val doctorFullName: String,
  val doctorEmail: String,
  val doctorQualification: String,
  val patientFullName: String,
  val patientAge: Byte,
  val patientSex: String,
  val patientProblem: String,
  val billNo: Int,
  val billDateTime: LocalDateTime,
  val billTotal: Double
) {

}


