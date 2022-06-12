package com.cj.dentalclinic.repository

import com.cj.dentalclinic.entity.Treatment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TreatmentRepository : JpaRepository<Treatment, Int> {

  @Query("SELECT t FROM Treatment t WHERE t.clinic.id = ?1")
  fun findAllByClinicId(clinicId: Int): List<Treatment>

}
