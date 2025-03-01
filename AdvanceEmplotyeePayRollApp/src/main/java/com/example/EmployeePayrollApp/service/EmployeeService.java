package com.example.EmployeePayrollApp.service;

import com.example.EmployeePayrollApp.dto.EmployeeDTO;
import com.example.EmployeePayrollApp.entity.Employee;
import com.example.EmployeePayrollApp.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    // Convert DTO to Entity using ModelMapper
    public Employee mapToEntity(EmployeeDTO dto) {
        return modelMapper.map(dto, Employee.class);
    }

    // Convert Entity to DTO using ModelMapper
    public EmployeeDTO mapToDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    // Get all Employees
    public List<EmployeeDTO> getAllEmployees() {
        log.info("Fetching all employees from database");
        List<EmployeeDTO> employees = employeeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} employees", employees.size());
        return employees;
    }

    // Get Employee by ID
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        log.info("Fetching employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(this::mapToDTO);
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setSalary(employeeDTO.getSalary());

        Employee savedEmployee = employeeRepository.save(employee);

        // Return DTO with ID
        return new EmployeeDTO(savedEmployee.getId(), savedEmployee.getName(), savedEmployee.getSalary());
    }

    // Update Employee
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO updatedEmployeeDTO) {
        log.info("Updating employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    modelMapper.map(updatedEmployeeDTO, existingEmployee); // Efficient update
                    Employee savedEmployee = employeeRepository.save(existingEmployee);
                    log.info("Employee with ID {} updated successfully", id);
                    return mapToDTO(savedEmployee);
                })
                .orElseThrow(() -> {
                    log.error("Failed to update. Employee with ID {} not found", id);
                    return new RuntimeException("Employee not found with id: " + id);
                });
    }

    // Delete Employee
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            log.info("Deleting employee with ID: {}", id);
            employeeRepository.deleteById(id);
            log.info("Employee with ID {} deleted successfully", id);
            return true;
        } else {
            log.warn("Delete failed. Employee with ID {} not found", id);
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
