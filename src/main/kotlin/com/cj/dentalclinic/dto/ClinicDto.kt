package com.cj.dentalclinic.dto

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "t_clinic")
data class ClinicDto(@Id var id: Int?, val name: String) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as ClinicDto

    return id != null && id == other.id
  }

  override fun hashCode(): Int = javaClass.hashCode()

  @Override
  override fun toString(): String {
    return this::class.simpleName + "(id = $id, name = $name)"
  }
}
