package com.example.EmployeePayrollApp.service;

import com.example.EmployeePayrollApp.dto.EmployeeDTO;
import com.example.EmployeePayrollApp.entity.Employee;
import com.example.EmployeePayrollApp.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j  // Enables logging
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Convert DTO to Entity
    private Employee mapToEntity(EmployeeDTO dto) {
        return new Employee(null, dto.getName(), dto.getSalary());
    }

    //Convert Entity to DTO
    private EmployeeDTO mapToDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(),employee.getName(), employee.getSalary());
    }

    //Get all Employees
    public List<EmployeeDTO> getAllEmployees() {
        log.info("Fetching all employees from database");
        List<EmployeeDTO> employees = employeeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} employees", employees.size());
        return employees;
    }

    // Get Employee by ID
    public Optional<Employee> getEmployeeById(Long id) {
        log.info("Fetching employee with ID: {}", id);
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            log.info("Employee found: {}", employee.get());
        } else {
            log.warn("Employee with ID {} not found", id);
        }
        return employee;
    }

    // Create Employee (same as save)
    public Employee createEmployee(Employee employee) {
        log.info("Creating new employee: {}", employee);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());
        return savedEmployee;
    }

    // Update Employee
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        log.info("Updating employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(updatedEmployee.getName());
                    employee.setSalary(updatedEmployee.getSalary());
                    Employee savedEmployee = employeeRepository.save(employee);
                    log.info("Employee with ID {} updated successfully", id);
                    return savedEmployee;
                })
                .orElseThrow(() -> {
                    log.error("Employee with ID {} not found for update", id);
                    return new RuntimeException("Employee not found with id: " + id);
                });
    }

    // Delete Employee
    public boolean deleteEmployee(Long id) {
        log.info("Received request to delete employee with ID: {}", id);
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            log.info("Employee with ID {} deleted successfully", id);
            return true;
        } else {
            log.warn("Employee with ID {} not found for deletion", id);
            return false;
        }
    }

    // Save Employee
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        log.info("Saving employee: {}", employeeDTO);
        Employee savedEmployee = employeeRepository.save(mapToEntity(employeeDTO));
        log.info("Employee saved successfully with ID: {}", savedEmployee.getId());
        return mapToDTO(savedEmployee);
    }
}
