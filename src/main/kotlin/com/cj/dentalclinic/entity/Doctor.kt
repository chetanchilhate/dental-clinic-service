package com.cj.dentalclinic.entity

import com.cj.dentalclinic.dto.DoctorDto
import org.hibernate.Hibernate
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.FetchType.LAZY
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@DynamicUpdate
@Table(name = "t_doctors")
class Doctor (

  @Id
  @GeneratedValue(strategy = IDENTITY)
  val id: Int? = null,

  @Column(nullable = false)
  var email: String,

  @Column(nullable = false)
  var firstName: String,

  @Column(nullable = false)
  var middleName: String,

  @Column(nullable = false)
  var lastName: String,

  @Column(nullable = false)
  var qualification: String,

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "clinic_id", nullable = false, updatable = false)
  val clinic: Clinic

) {

  constructor(doctorDto: DoctorDto, clinic: Clinic) : this(
    email = doctorDto.email,
    firstName = doctorDto.firstName,
    middleName = doctorDto.middleName,
    lastName = doctorDto.lastName,
    qualification = doctorDto.qualification,
    clinic = clinic
  )

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as Treatment

    return id != null && id == other.id
  }

  override fun hashCode(): Int = javaClass.hashCode()

}
