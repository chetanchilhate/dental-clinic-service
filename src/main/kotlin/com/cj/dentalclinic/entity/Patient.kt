package com.cj.dentalclinic.entity

import com.cj.dentalclinic.dto.PatientDto
import com.cj.dentalclinic.dto.Sex
import org.hibernate.Hibernate
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Entity
@DynamicUpdate
@Table(name = "t_patients")
class Patient(

  @Id
  @GeneratedValue(strategy = IDENTITY)
  val id: Int? = null,

  @Column(nullable = false)
  var firstName: String,

  @Column(nullable = false)
  var middleName: String,

  @Column(nullable = false)
  var lastName: String,

  @Column(nullable = false)
  var age: Byte,

  @Column(nullable = false, columnDefinition = "ENUM ('MALE', 'FEMALE', 'OTHER')")
  @Enumerated(EnumType.STRING)
  var sex: Sex,

  @Column(nullable = false)
  var mobile: String = "",

  @Column(nullable = false)
  var problem: String = "",

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "doctor_id", nullable = false, updatable = false)
  val doctor: Doctor

) {

  constructor(patientDto: PatientDto, doctor: Doctor) : this(
    firstName = patientDto.firstName,
    middleName = patientDto.middleName,
    lastName = patientDto.lastName,
    age = patientDto.age,
    sex = patientDto.sex,
    mobile = patientDto.mobile,
    problem = patientDto.problem,
    doctor = doctor
  )

  fun update(patientDto: PatientDto): Patient {
    firstName = patientDto.firstName
    middleName = patientDto.middleName
    lastName = patientDto.lastName
    age = patientDto.age
    sex = patientDto.sex
    mobile = patientDto.mobile
    problem = patientDto.problem
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as Patient

    return id != null && id == other.id
  }

  override fun hashCode(): Int = javaClass.hashCode()

}
