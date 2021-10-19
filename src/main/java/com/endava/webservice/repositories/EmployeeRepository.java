package com.endava.webservice.repositories;

import com.endava.webservice.entities.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    List<Employee> findAll();

    Optional<Employee> findById(long id);

    Employee save(Employee employee);

    List<Employee> findAllByEmail(String email);

    List<Employee> findAllByPhoneNumber(String phoneNumber);
}
