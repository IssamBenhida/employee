package com.app.employee.controller;

import com.app.employee.model.Employee;
import com.app.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(employees);
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getEmployeeById(@PathVariable int id, @RequestParam(name = "lang", required = false) String lang) {
        Locale locale = (lang != null) ? Locale.forLanguageTag(lang) : Locale.getDefault();
        Employee employee = employeeRepository.findById(id);
        if (employee != null) {
            return ResponseEntity.ok(employee.toString());
        } else {
            String message = messageSource.getMessage("employee.notfound", new Object[]{id}, locale);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee, @RequestParam(name = "lang", required = false) String lang) {
        Locale locale = (lang != null) ? Locale.forLanguageTag(lang) : Locale.getDefault();
        Employee savedEmployee = employeeRepository.save(employee);
        String message = messageSource.getMessage("employee.created", new Object[]{savedEmployee.getId()}, locale);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody Employee employee, @RequestParam(name = "lang", required = false) String lang) {
        Locale locale = (lang != null) ? Locale.forLanguageTag(lang) : Locale.getDefault();
        return employeeRepository.findById(id).map(
                existingEmployee -> {
                    existingEmployee.setName(employee.getName());
                    existingEmployee.setLastname(employee.getLastname());
                    existingEmployee.setEmail(employee.getEmail());
                    employeeRepository.save(existingEmployee);
                    String message = messageSource.getMessage("employee.updated", new Object[]{id}, locale);
                    return ResponseEntity.ok(message);
                }).orElseGet(() -> {
            String message = messageSource.getMessage("employee.notfound", new Object[]{id}, locale);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id, @RequestParam(name = "lang", required = false) String lang) {
        Locale locale = (lang != null) ? Locale.forLanguageTag(lang) : Locale.getDefault();
        return employeeRepository.findById(id).map(employee -> {
            employeeRepository.delete(employee);
            String message = messageSource.getMessage("employee.deleted", new Object[]{id}, locale);
            return ResponseEntity.ok(message);
        }).orElseGet(() -> {
            String message = messageSource.getMessage("employee.notfound", new Object[]{id}, locale);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        });
    }
}
