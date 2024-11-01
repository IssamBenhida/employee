package com.app.employee.repository;

import com.app.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findById(int id);

    boolean existsByEmail(String email);
}
