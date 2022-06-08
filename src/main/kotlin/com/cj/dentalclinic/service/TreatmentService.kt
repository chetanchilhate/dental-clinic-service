package com.cj.dentalclinic.service

import com.cj.dentalclinic.dto.TreatmentDto
import com.cj.dentalclinic.entity.Treatment
import com.cj.dentalclinic.exception.ResourceNotFoundException
import com.cj.dentalclinic.repository.ClinicRepository
import com.cj.dentalclinic.repository.TreatmentRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TreatmentService(
  private val treatmentRepository: TreatmentRepository,
  private val clinicRepository: ClinicRepository
) {

  fun getAllTreatments(clinicId: Int): List<TreatmentDto> = treatmentRepository
    .findAllByClinicId(clinicId)
    .map { t -> TreatmentDto(t) }
    .toList()

  fun getTreatmentById(treatmentId: Int): TreatmentDto = treatmentRepository
    .findById(treatmentId)
    .map { t -> TreatmentDto(t) }
    .orElseThrow { ResourceNotFoundException("Treatment", treatmentId) }

  fun addTreatment(clinicId: Int, treatmentDto: TreatmentDto): TreatmentDto {
    val clinic = clinicRepository.getReferenceById(clinicId)
    val newTreatment = Treatment(name = treatmentDto.name, fee = treatmentDto.fee, clinic = clinic)
    return try {
      TreatmentDto(treatmentRepository.save(newTreatment))
    } catch (e: DataIntegrityViolationException) {
      throw ResourceNotFoundException("Clinic", clinicId)
    }
  }

  fun updateTreatment(treatmentId: Int, treatmentDto: TreatmentDto): TreatmentDto {

    val existingTreatment = treatmentRepository.findById(treatmentId)

    if (existingTreatment.isEmpty) {
      throw ResourceNotFoundException("Treatment", treatmentId)
    }

    val updatedTreatment = existingTreatment.get()
    updatedTreatment.name = treatmentDto.name
    updatedTreatment.fee = treatmentDto.fee

    return TreatmentDto(treatmentRepository.save(updatedTreatment))
  }

  fun deleteTreatment(treatmentId: Int) =
    if (treatmentRepository.existsById(treatmentId))
      treatmentRepository.deleteById(treatmentId)
    else throw ResourceNotFoundException("Treatment", treatmentId)

}
