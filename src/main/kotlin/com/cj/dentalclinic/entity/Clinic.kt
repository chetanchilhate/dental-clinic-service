package com.cj.dentalclinic.entity

import com.cj.dentalclinic.dto.ClinicDto
import org.hibernate.Hibernate
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@DynamicUpdate
@Table(name = "t_clinic")
class Clinic(

  @Id
  @GeneratedValue(strategy = IDENTITY)
  val id: Int? = null,

  @Column(nullable = false)
  val name: String

) {

  constructor(clinicDto: ClinicDto) : this(clinicDto.id, clinicDto.name)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as Clinic

    return id != null && id == other.id
  }

  override fun hashCode(): Int = javaClass.hashCode()

}
