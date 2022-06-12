package com.cj.dentalclinic.entity

import com.cj.dentalclinic.dto.TreatmentDto
import org.hibernate.Hibernate
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Entity
@DynamicUpdate
@Table(name = "t_treatments")
class Treatment(

  @Id
  @GeneratedValue(strategy = IDENTITY)
  val id: Int? = null,

  @Column(nullable = false)
  var name: String,

  var fee: Double = 100.00,

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "clinic_id", nullable = false, updatable = false)
  val clinic: Clinic

) {

  fun update(treatmentDto: TreatmentDto): Treatment {
    name = treatmentDto.name
    fee = treatmentDto.fee
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as Treatment

    return id != null && id == other.id
  }

  override fun hashCode(): Int = javaClass.hashCode()

}
