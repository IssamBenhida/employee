package com.app.employee.controller;

import com.app.employee.model.Employee;
import com.app.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeRepository.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeRepository.findById(id).map(
                newemployee -> {
                    newemployee.setName(employee.getName());
                    newemployee.setLastname(employee.getLastname());
                    newemployee.setEmail(employee.getEmail());
                    return ResponseEntity.ok(employeeRepository.save(newemployee));
                }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id, Locale locale) {
        return employeeRepository.findById(id).map(employee -> {
            employeeRepository.delete(employee);
            String message = messageSource.getMessage("employee.deleted", null, locale);
            return ResponseEntity.ok(message);
        }).orElseGet(() -> {
            String message = messageSource.getMessage("employee.notfound", null, locale);
            return ResponseEntity.status(404).body(message);
        });
    }
}
