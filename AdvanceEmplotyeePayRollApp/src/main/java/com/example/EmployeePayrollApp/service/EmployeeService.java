package com.example.EmployeePayrollApp.service;

import com.example.EmployeePayrollApp.dto.EmployeeDTO;
import com.example.EmployeePayrollApp.entity.Employee;
import com.example.EmployeePayrollApp.exception.EmployeeNotFoundException;
import com.example.EmployeePayrollApp.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isEmpty()) {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found");
        }

        return employee.map(this::mapToDTO);
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee savedEmployee = employeeRepository.save(new Employee(
                null,  //Ensuring ID is null so Hibernate treats it as a new entity
                employeeDTO.getName(),
                employeeDTO.getSalary(),
                employeeDTO.getRoll()
        ));
        return new EmployeeDTO(savedEmployee.getId(), savedEmployee.getName(), savedEmployee.getSalary(),savedEmployee.getRoll());
    }

    //update data
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with ID " + id + " not found"));

        // Update existing employee fields (DO NOT create a new object)
        employee.setName(employeeDTO.getName());
        employee.setRoll(employeeDTO.getRoll());
        employee.setSalary(employeeDTO.getSalary());

        // Save updated entity
        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeDTO(savedEmployee.getId(), savedEmployee.getName(), savedEmployee.getSalary(),savedEmployee.getRoll() );
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
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found");
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
