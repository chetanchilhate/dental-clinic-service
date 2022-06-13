package com.cj.dentalclinic

import com.cj.dentalclinic.dto.Sex
import com.cj.dentalclinic.entity.Clinic
import com.cj.dentalclinic.entity.Doctor
import com.cj.dentalclinic.entity.Patient
import com.cj.dentalclinic.entity.Treatment
import io.github.serpro69.kfaker.Faker
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

const val DENTAL_CLINIC_SUFFIX = "Dental Clinic"

class ClinicDataStore {

  val dcSuffix = DENTAL_CLINIC_SUFFIX

  private val faker = Faker()

  private val random = Random()

  private val maxParentId = 4

  val maxId = 9

  val validId = random.nextInt(1, maxId)

  val invalidId = random.nextInt(maxId + 1, 100)

  val parentIdWithData = random.nextInt(1, maxParentId)

  val clinics = mutableListOf<Clinic>()

  init {
    for (id in 1..maxId) {
      clinics.add(createClinic(id))
    }
  }

  fun createClinic(id: Int?) = Clinic(id, "${faker.name.femaleFirstName()} $DENTAL_CLINIC_SUFFIX")

  fun newClinic() = createClinic(null)

  fun saveClinic(clinic: Clinic) = Clinic(randomIfNull(clinic.id), clinic.name)

  fun findClinicById(clinicId: Int): Optional<Clinic> = clinics
    .stream()
    .filter { it.id == clinicId }
    .findFirst()

  fun clinicExistById(clinicId: Int): Boolean = clinics
    .stream()
    .anyMatch { it.id == clinicId }

/**
 * =====================================================================================================================
 */

  private val treatments = mutableListOf<Treatment>()

  val minTreatmentFee = 100.0

  init {
    for (id in 1..maxId) {
      treatments.add(createTreatment(id))
    }
  }

  fun createTreatment(id: Int?) =
    Treatment(id = id, name = faker.restaurant.name(), fee = randomFee(), clinic = clinics[calculateParentIndex(id)])

  private fun randomFee() = (random.nextDouble(minTreatmentFee, 100_000.0) * 100.0).roundToInt() / 100.0

  private fun calculateParentIndex(id: Int?) = ((id ?: 1) % maxParentId)

  fun newTreatment() = createTreatment(null)

  fun saveTreatment(treatment: Treatment) =
    Treatment(randomIfNull(treatment.id), treatment.name, treatment.fee, treatment.clinic)

  private fun randomIfNull(id: Int?) = id ?: abs(random.nextInt())

  fun findAllTreatmentsByClinicId(clinicId: Int): List<Treatment> = treatments
    .stream()
    .filter { it.clinic.id == clinicId }
    .toList()

  fun findTreatmentById(treatmentId: Int): Optional<Treatment> = treatments
    .stream()
    .filter { it.id == treatmentId }
    .findFirst()

  fun treatmentExistById(treatmentId: Int): Boolean = treatments
    .stream()
    .anyMatch { it.id == treatmentId }

/**
 * =====================================================================================================================
 */

  private val doctors = mutableListOf<Doctor>()

  init {
    for (id in 1..maxId) {
      doctors.add(createDoctor(id))
    }
  }

  fun createDoctor(id: Int?): Doctor {
    val firstName = faker.name.firstName()
    val lastName = faker.name.lastName()
    return Doctor(
      id = id, email = faker.internet.email((lastName + firstName).lowercase()),
      firstName = firstName, middleName = "", lastName = lastName,
      qualification = faker.educator.degree(), clinic = clinics[calculateParentIndex(id)]
    )
  }

  fun newDoctor() = createDoctor(null)

  fun saveDoctor(doctor: Doctor) = Doctor(
    randomIfNull(doctor.id),
    doctor.email,
    doctor.firstName,
    doctor.middleName,
    doctor.lastName,
    doctor.qualification,
    doctor.clinic
  )

  fun findAllDoctorsByClinicId(clinicId: Int): List<Doctor> = doctors
    .stream()
    .filter { it.clinic.id == clinicId }
    .toList()

  fun findDoctorById(doctorId: Int): Optional<Doctor> = doctors
    .stream()
    .filter { it.id == doctorId }
    .findFirst()

  fun doctorExistById(doctorId: Int): Boolean = doctors
    .stream()
    .anyMatch { it.id == doctorId }

/**
 * =====================================================================================================================
 */

  private val patients = mutableListOf<Patient>()

  init {
    for (id in 1..maxId) {
      patients.add(createPatient(id))
    }
  }

  fun createPatient(id: Int?): Patient {
    return Patient(
      id,
      faker.name.firstName(),
      "",
      faker.name.lastName(),
      randomAge(),
      Sex.values()[randomOrdinal()],
      faker.phoneNumber.cellPhone(),
      faker.lorem.supplemental(),
      doctors[calculateParentIndex(id)]
    )
  }

  private fun randomAge() = random.nextInt(11, 100).toByte()

  private fun randomOrdinal() = random.nextInt(0, 2)

  fun newPatient() = createPatient(null)

  fun savePatient(patient: Patient) = Patient(
    randomIfNull(patient.id),
    patient.firstName,
    patient.middleName,
    patient.lastName,
    patient.age,
    patient.sex,
    patient.mobile,
    patient.problem,
    patient.doctor
  )

  fun findAllPatientsByDoctorId(doctorId: Int): List<Patient> = patients
    .stream()
    .filter { it.doctor.id == doctorId }
    .toList()

  fun findPatientById(patientId: Int): Optional<Patient> = patients
    .stream()
    .filter { it.id == patientId }
    .findFirst()

  fun patientExistById(patientId: Int): Boolean = patients
    .stream()
    .anyMatch { it.id == patientId }

}
