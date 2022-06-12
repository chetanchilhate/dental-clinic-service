package com.cj.dentalclinic.repository

import com.cj.dentalclinic.entity.Doctor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DoctorRepository: JpaRepository<Doctor, Int> {

  @Query("SELECT d FROM Doctor d WHERE d.clinic.id = ?1")
  fun findAllByClinicId(clinicId: Int): List<Doctor>

}
