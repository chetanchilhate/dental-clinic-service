package com.cj.dentalclinic

import com.cj.dentalclinic.entity.Clinic
import com.cj.dentalclinic.entity.Treatment
import java.util.*

class ClinicDataStore {

  private val clinics = listOf(
    Clinic(1, "Sharda Dental Clinic"),
    Clinic(2, "Smart Dental Clinic"),
    Clinic(3, "Sonal Dental Clinic")
  )

  private val treatments = listOf(
    Treatment(1, "Root Canal", 4500.00, clinics[0]),
    Treatment(2, "Regular Checkup", 500.00, clinics[0]),
    Treatment(3, "Regular Checkup", 100.00, clinics[1])
  )

  fun findAllClinics() = clinics

  fun findClinicById(clinicId: Int): Optional<Clinic> = clinics.stream().filter { t -> t.id == clinicId }.findFirst()

  fun newClinicId() = 4

  fun newClinic() = Clinic(name = "Sky Dental Clinic")

  fun saveClinic(clinic: Clinic) = clinic.copy(id = newClinicId())

  fun findAllTreatmentsByClinicId(clinicId: Int): List<Treatment> = treatments.stream().filter{ t -> t.clinic.id == clinicId }.toList()

  fun findTreatmentsById(treatmentId: Int): Optional<Treatment> = treatments.stream().filter{ t -> t.id == treatmentId }.findFirst()

  fun newTreatmentId() = 4

  fun newTreatment() = Treatment(name = "Cleaning", fee = 500.00, clinic =  clinics[0])

  fun saveTreatment(treatment: Treatment) = treatment.copy(id = newClinicId())

}