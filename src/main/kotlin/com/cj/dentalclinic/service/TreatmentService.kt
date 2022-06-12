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
    .map { TreatmentDto(it) }
    .toList()

  fun getTreatmentById(treatmentId: Int): TreatmentDto = treatmentRepository
    .findById(treatmentId)
    .map { TreatmentDto(it) }
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

    if (treatmentRepository.existsById(treatmentId).not()) {
      throw ResourceNotFoundException("Treatment", treatmentId)
    }

    val existingTreatment = treatmentRepository.findById(treatmentId).get()

    return TreatmentDto(treatmentRepository.save(existingTreatment.update(treatmentDto)))
  }

  fun deleteTreatment(treatmentId: Int) =
    if (treatmentRepository.existsById(treatmentId))
      treatmentRepository.deleteById(treatmentId)
    else throw ResourceNotFoundException("Treatment", treatmentId)

}
