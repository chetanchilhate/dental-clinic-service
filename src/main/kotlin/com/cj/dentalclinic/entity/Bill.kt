package com.cj.dentalclinic.entity

import com.cj.dentalclinic.dto.BillLineDto
import org.hibernate.Hibernate
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "t_bills")
class Bill(

  @Id
  @GeneratedValue(strategy = IDENTITY)
  val id: Int? = null,

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "patient_id", nullable = false, updatable = false)
  private val patient: Patient,

) {

  @Column(nullable = false, updatable = false)
  val createDateTime: LocalDateTime = LocalDateTime.now()

  @Column(nullable = false, updatable = false)
  final var total: Double = 0.0
    get() = field
    private set

  @OneToMany(mappedBy = "bill" ,cascade = [CascadeType.ALL], orphanRemoval = true)
  val billLines: MutableSet<BillLine> = mutableSetOf()

  fun addBillLines(billLinesDto: List<BillLineDto>) {
    billLinesDto.forEach{ addBillLine(it)}
    total = billLinesDto.sumOf { it.fee }
  }

  private fun addBillLine(billLineDto: BillLineDto) {
    this.billLines.add(BillLine(billLineDto, this))
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as Bill

    return id != null && id == other.id
  }

  override fun hashCode(): Int = javaClass.hashCode()

}
