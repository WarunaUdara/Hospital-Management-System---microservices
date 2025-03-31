package com.pm.patientservice.repository;

import com.pm.patientservice.entity.PatientEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, UUID> {
    // Changed return type to Optional<PatientEntity>
    Optional<PatientEntity> findByEmail(String email);

    Boolean existsByEmail(@NotNull @Email String email);
    Boolean existsByEmailAndIdNot(@NotNull @Email String email, @NotNull UUID id);
}