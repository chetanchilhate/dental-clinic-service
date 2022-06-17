package com.cj.dentalclinic.entity

import com.cj.dentalclinic.dto.BillLineDto
import org.hibernate.Hibernate
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY


const val query = """
  SELECT tc.name                                                     AS clinicName,
         CONCAT_WS(' ', td.first_name, td.middle_name, td.last_name) AS doctorFullName,
         td.email                                                    AS doctorEmail,
         td.qualification                                            AS doctorQualification,
         CONCAT_WS(' ', tp.first_name, tp.middle_name, tp.last_name) AS patientFullName,
         tp.age                                                      AS patientAge,
         tp.sex                                                      AS patientSex,
         tp.problem                                                  AS patientProblem,
         tb.id                                                       AS billNo,
         tb.create_date_time                                         AS billDateTime,
         tb.total                                                    AS billTotal
  FROM   t_clinics tc
             INNER JOIN t_doctors td
                        ON tc.id = td.clinic_id
             INNER JOIN t_patients tp
                        ON td.id = tp.doctor_id
             INNER JOIN t_bills tb
                        ON tp.id = tb.patient_id
  WHERE  tb.id = ?1
"""

@NamedNativeQuery(name = "Bill.findBillDetailsById", query = query, resultSetMapping = "Mapping.BillDetailView")

@SqlResultSetMapping(
  name = "Mapping.BillDetailView",
  classes = [ConstructorResult(
    targetClass = BillDetailView::class,
    columns = [ColumnResult(name = "clinicName"),
      ColumnResult(name = "doctorFullName"),
      ColumnResult(name = "doctorEmail"),
      ColumnResult(name = "doctorQualification"),
      ColumnResult(name = "patientFullName"),
      ColumnResult(name = "patientAge"),
      ColumnResult(name = "patientSex", type = String::class),
      ColumnResult(name = "patientProblem"),
      ColumnResult(name = "billNo"),
      ColumnResult(name = "billDateTime", type = LocalDateTime::class),
      ColumnResult(name = "billTotal")]
  )]
)

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

  @OneToMany(mappedBy = "bill", cascade = [CascadeType.ALL], orphanRemoval = true)
  val billLines: MutableSet<BillLine> = mutableSetOf()

  fun addBillLines(billLinesDto: List<BillLineDto>) {
    billLinesDto.forEach { addBillLine(it) }
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
