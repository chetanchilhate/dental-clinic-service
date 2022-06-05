package com.cj.dentalclinic.entity

import org.hibernate.Hibernate
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "t_treatment")
data class Treatment(

  @Id
  @GeneratedValue(strategy = IDENTITY)
  val id: Int?,

  @Column(nullable = false)
  val name: String,

  val fee: Double = 0.00,

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "clinic_id", nullable = false)
  val clinic: Clinic?

) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as Treatment

    return if (id != null) id == other.id else name == other.name
  }

  override fun hashCode(): Int = javaClass.hashCode()

  @Override
  override fun toString(): String {
    return this::class.simpleName + "(id = $id , name = $name , fee = $fee , clinic = $clinic )"
  }

}
