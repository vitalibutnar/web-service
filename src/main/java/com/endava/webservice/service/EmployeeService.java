package com.endava.webservice.service;

import com.endava.webservice.entities.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAll();

    Employee getById(Long id);

    Employee save(Employee employee);

    Employee update(Employee employee);
}
