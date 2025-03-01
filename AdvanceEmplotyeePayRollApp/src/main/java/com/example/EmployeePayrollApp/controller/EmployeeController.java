package com.example.EmployeePayrollApp.controller;

import com.example.EmployeePayrollApp.dto.EmployeeDTO;
import com.example.EmployeePayrollApp.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Received request to create employee: {}", employeeDTO);
        EmployeeDTO savedEmployee = employeeService.createEmployee(employeeDTO);
        log.info("Employee created successfully with ID {}: {}", savedEmployee.getId(), savedEmployee);
        return ResponseEntity.ok(savedEmployee);
    }

    // Get All Employees
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        log.info("Fetching all employees");
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        log.info("Retrieved {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        log.info("Fetching employee with ID: {}", id);
        Optional<EmployeeDTO> employeeDTO = employeeService.getEmployeeById(id);

        if (employeeDTO.isPresent()) {
            log.info("Employee found: {}", employeeDTO.get());
            return ResponseEntity.ok(employeeDTO.get());
        } else {
            log.warn("Employee with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Update Employee
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        log.info("Updating employee with ID: {}", id);
        try {
            EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
            log.info("Employee with ID {} updated successfully", id);
            return ResponseEntity.ok(updatedEmployee);
        } catch (RuntimeException e) {
            log.error("Failed to update. Employee with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Delete Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("Deleting employee with ID: {}", id);
        boolean deleted = employeeService.deleteEmployee(id);

        if (deleted) {
            log.info("Employee with ID {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Delete failed. Employee with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
