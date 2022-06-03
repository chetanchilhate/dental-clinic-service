package com.cj.dentalclinic.repository

import com.cj.dentalclinic.dto.ClinicDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClinicRepository : JpaRepository<ClinicDto, Int>
