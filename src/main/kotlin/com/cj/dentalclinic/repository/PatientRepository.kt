package com.cj.dentalclinic.repository

import com.cj.dentalclinic.entity.Patient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PatientRepository : JpaRepository<Patient, Int> {

  @Query("SELECT p FROM Patient p WHERE p.doctor.id = ?1")
  fun findPatientsByDoctorId(doctorId: Int): List<Patient>

}
