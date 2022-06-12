package com.cj.dentalclinic.entity

import com.cj.dentalclinic.dto.Sex
import org.hibernate.Hibernate
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "t_patients")
class Patient(

  @Id
  @GeneratedValue(strategy = IDENTITY)
  val id: Int?,

  @Column(nullable = false)
  var firstName: String,

  @Column(nullable = false)
  var middleName: String,

  @Column(nullable = false)
  var lastName: String,

  @Column(nullable = false)
  var age: Byte,

  @Column(nullable = false, columnDefinition="ENUM ('MALE', 'FEMALE', 'OTHER')")
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

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as Patient

    return id != null && id == other.id
  }

  override fun hashCode(): Int = javaClass.hashCode()

}
