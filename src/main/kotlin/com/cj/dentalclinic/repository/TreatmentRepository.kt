package com.cj.dentalclinic.repository

import com.cj.dentalclinic.entity.Treatment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TreatmentRepository : JpaRepository<Treatment, Int> {

  @Query("SELECT t FROM Treatment t WHERE t.clinic.id = ?1")
  fun findAllByClinicId(clinicId: Int): List<Treatment>

  @Query("SELECT t FROM Treatment t JOIN FETCH t.clinic WHERE t.id = ?1")
  fun findTreatmentById(treatmentId: Int): Optional<Treatment>

}
