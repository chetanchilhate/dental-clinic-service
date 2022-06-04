package com.cj.dentalclinic.entity

import com.cj.dentalclinic.dto.ClinicDto
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "t_clinic")
data class Clinic(@Id @GeneratedValue(strategy = IDENTITY) val id: Int? = null, val name: String) {

  constructor(clinicDto: ClinicDto) : this(clinicDto.id, clinicDto.name)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as Clinic

    return if (id != null) id == other.id else name == other.name
  }

  override fun hashCode(): Int = javaClass.hashCode()

  @Override
  override fun toString(): String {
    return this::class.simpleName + "(id = $id , name = $name )"
  }

}
