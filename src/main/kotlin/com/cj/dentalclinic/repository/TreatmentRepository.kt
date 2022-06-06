package com.cj.dentalclinic.repository

import com.cj.dentalclinic.entity.Treatment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface TreatmentRepository : JpaRepository<Treatment, Int> {

  fun findAllByClinicId(clinicId: Int): List<Treatment> {
    TODO("Not yet implemented")
  }

  fun findTreatmentById(treatmentId: Int): Optional<Treatment> {
    TODO("Not yet implemented")
  }

}
