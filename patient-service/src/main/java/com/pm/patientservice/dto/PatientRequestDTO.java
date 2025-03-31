package com.pm.patientservice.dto;

import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name should not exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @NotBlank(message = "BirthDate is mandatory")
    private String birthDate;

    @NotBlank(message = "Phone is mandatory")
    @Size(max = 15, message = "Phone should not exceed 15 characters")
    private String phone;

    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @NotBlank(groups = CreatePatientValidationGroup.class, message = "Registration date date is mandatory")
    private String registrationDate;
}
