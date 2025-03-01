package com.example.EmployeePayrollApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private long id;

    @NotBlank(message = "Name is required and cannot be empty") // Required field
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters and spaces") // Only letters & spaces
    private String name;
    private double salary;
    private String roll;
}
