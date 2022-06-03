package com.cj.dentalclinic.repository

import com.cj.dentalclinic.entity.Clinic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClinicRepository : JpaRepository<Clinic, Int>
