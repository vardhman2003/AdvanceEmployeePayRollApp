package com.example.EmployeePayrollApp.controller;

import com.example.EmployeePayrollApp.dto.EmployeeDTO;
import com.example.EmployeePayrollApp.entity.Employee;
import com.example.EmployeePayrollApp.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j  //Enables logging
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //Create Employee
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Received request to create employee: {}", employeeDTO);
        EmployeeDTO savedEmployee = employeeService.saveEmployee(employeeDTO);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());
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
    public ResponseEntity<Optional<Employee>> getEmployeeById(@PathVariable Long id) {
        log.info("Fetching employee with ID: {}", id);
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            log.info("Employee found: {}", employee.get());
            return ResponseEntity.ok(employee);
        } else {
            log.warn("Employee with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    //Update Employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        log.info("Updating employee with ID: {}", id);
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        if (updatedEmployee != null) {
            log.info("Employee with ID {} updated successfully", id);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            log.warn("Employee with ID {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    //Delete Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("Received request to delete employee with ID: {}", id);
        boolean deleted = employeeService.deleteEmployee(id);
        if (deleted) {
            log.info("Employee with ID {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Employee with ID {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}
