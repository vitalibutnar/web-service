package com.endava.webservice.controllers;

import com.endava.webservice.entities.Employee;
import com.endava.webservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
public class EmployeesController {
    private final EmployeeService employeeService;

    @GetMapping
    List<Employee> getAll() {
        return employeeService.getAll();
    }

    @GetMapping("{id}")
    Employee getById(@PathVariable long id) {
        return employeeService.getById(id);
    }

    @PutMapping("{id}")
    Employee updateById(@PathVariable long id, @Valid @RequestBody Employee employee) {
        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    @PostMapping
    Employee add(@Valid @RequestBody Employee employee) {
        return employeeService.save(employee);
    }
}