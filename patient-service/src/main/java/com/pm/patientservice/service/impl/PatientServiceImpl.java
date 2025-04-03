package com.pm.patientservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.entity.PatientEntity;
import com.pm.patientservice.exception.EmailAlreadyExistException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.repository.PatientJpaRepository;
import com.pm.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientJpaRepository patientJpaRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);
    private final KafkaProducer kafkaProducer;

    @Override
    public List<PatientResponseDTO> getAllPatients() {
        return patientJpaRepository.findAll().stream()
                .map(this::mapToPatientDTO)
                .toList();
    }

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        try {
            if (patientJpaRepository.existsByEmail(patientRequestDTO.getEmail())) {
                throw new EmailAlreadyExistException("Patient with email " + patientRequestDTO.getEmail() + " already exists");
            }
            PatientEntity patientEntity = mapToEntity(patientRequestDTO);
            PatientEntity savedPatient = patientJpaRepository.save(patientEntity);
            log.info("Successfully created patient with ID: {}", savedPatient.getId());
            billingServiceGrpcClient.createBillingAccount(savedPatient.getId().toString(),
                    savedPatient.getName(), savedPatient.getEmail());
            kafkaProducer.sendEvent(savedPatient);
            return mapToPatientDTO(savedPatient);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd format");
        } catch (DataIntegrityViolationException e) {
            log.error("Database constraint violation: {}", e.getMessage());
            throw new RuntimeException("Database error occurred while creating patient");
        } catch (EmailAlreadyExistException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create patient", e);
            throw new RuntimeException("Failed to create patient: " + e.getMessage());
        }
    }

    @Override
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) throws PatientNotFoundException {
        try {
            PatientEntity patientEntity = patientJpaRepository.findById(id)
                    .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

            // Check if email is being changed and new email already exists
            if (!patientEntity.getEmail().equals(patientRequestDTO.getEmail())
                    && patientJpaRepository.existsByEmail(patientRequestDTO.getEmail())) {
                throw new EmailAlreadyExistException("Patient with email " + patientRequestDTO.getEmail() + " already exists");
            }

            patientEntity.setName(patientRequestDTO.getName());
            patientEntity.setEmail(patientRequestDTO.getEmail());
            patientEntity.setPhone(patientRequestDTO.getPhone());
            patientEntity.setAddress(patientRequestDTO.getAddress());
            patientEntity.setBirthDate(LocalDate.parse(patientRequestDTO.getBirthDate()));
            patientEntity.setGender(patientRequestDTO.getGender());

            PatientEntity updatedPatient = patientJpaRepository.save(patientEntity);
            log.info("Successfully updated patient with ID: {}", updatedPatient.getId());
            return mapToPatientDTO(updatedPatient);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd format");
        } catch (PatientNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update patient with ID: {}", id, e);
            throw new RuntimeException("Failed to update patient: " + e.getMessage());
        }
    }

    public Boolean removePatient(UUID id) {
        try {
            PatientEntity patientEntity = patientJpaRepository.findById(id)
                    .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));
            patientJpaRepository.delete(patientEntity);
            log.info("Successfully deleted patient with ID: {}", id);
            return true;
        } catch (PatientNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete patient with ID: {}", id, e);
            throw new RuntimeException("Failed to delete patient: " + e.getMessage());
        }
    }

    private PatientResponseDTO mapToPatientDTO(PatientEntity patient) {
        return new PatientResponseDTO(
                patient.getId().toString(),
                patient.getName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getAddress(),
                patient.getBirthDate().toString(),
                patient.getGender()
        );
    }

    private PatientEntity mapToEntity(PatientRequestDTO patientRequestDTO) {
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setName(patientRequestDTO.getName());
        patientEntity.setEmail(patientRequestDTO.getEmail());
        patientEntity.setPhone(patientRequestDTO.getPhone());
        patientEntity.setAddress(patientRequestDTO.getAddress());
        patientEntity.setBirthDate(LocalDate.parse(patientRequestDTO.getBirthDate()));
        patientEntity.setRegistrationDate(LocalDate.parse(patientRequestDTO.getRegistrationDate()));
        patientEntity.setGender(patientRequestDTO.getGender());
        return patientEntity;
    }
}