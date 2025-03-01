package com.example.EmployeePayrollApp.controller;

import com.example.EmployeePayrollApp.dto.EmployeeDTO;
import com.example.EmployeePayrollApp.entity.Employee;
import com.example.EmployeePayrollApp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired  // Injecting EmployeeService
    private EmployeeService employeeService;

    // Create Employee
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.saveEmployee(employeeDTO));
    }

    // Get All Employees
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Employee>> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    // Update Employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return updatedEmployee != null ? ResponseEntity.ok(updatedEmployee) : ResponseEntity.notFound().build();
    }

    // Delete Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteEmployee(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
