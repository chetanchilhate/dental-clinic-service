package com.cj.dentalclinic.entity

import com.cj.dentalclinic.dto.BillLineDto
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
@Table(name = "t_bill_lines")
class BillLine(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Int? = null,

  @Column(nullable = false, updatable = false)
  val treatment: String,

  @Column(nullable = false, updatable = false)
  val fee: Double,

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "bill_id", nullable = false, updatable = false)
  private val bill: Bill

) {

  constructor(billLineDto: BillLineDto, bill: Bill) : this(
    treatment = billLineDto.treatment,
    fee = billLineDto.fee,
    bill = bill
  )

}
