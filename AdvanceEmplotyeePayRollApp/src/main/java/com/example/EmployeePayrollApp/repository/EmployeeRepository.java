package com.example.EmployeePayrollApp.repository;

import com.example.EmployeePayrollApp.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
