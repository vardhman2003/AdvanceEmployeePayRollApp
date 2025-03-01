package com.example.EmployeePayrollApp.service;

import com.example.EmployeePayrollApp.dto.EmployeeDTO;
import com.example.EmployeePayrollApp.entity.Employee;
import com.example.EmployeePayrollApp.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //Convert DTO to Entity
    private Employee mapToEntity(EmployeeDTO dto) {
        return new Employee(null, dto.getName(), dto.getSalary());
    }

    //Convert Entity to DTO
    private EmployeeDTO mapToDTO(Employee employee) {
        return new EmployeeDTO(employee.getName(), employee.getSalary());
    }


    // Get all Employees
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get Employee by ID
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    // Create Employee (same as save)
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    //Update Employee
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(updatedEmployee.getName());
                    employee.setSalary(updatedEmployee.getSalary());
                    return employeeRepository.save(employee);
                })
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    //Delete Employee
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //Save Employee
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.save(mapToEntity(employeeDTO));
        return mapToDTO(employee);
    }
}
