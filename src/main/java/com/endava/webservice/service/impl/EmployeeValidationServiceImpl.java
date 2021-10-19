package com.endava.webservice.service.impl;

import com.endava.webservice.entities.Employee;
import com.endava.webservice.exeption.IllegalFieldValueException;
import com.endava.webservice.repositories.EmployeeRepository;
import com.endava.webservice.service.EmployeeValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeValidationServiceImpl implements EmployeeValidationService {

    private final EmployeeRepository repository;

    private void validateEmail(Employee employee) {
        List<Employee> employees = repository.findAllByEmail(employee.getEmail());
        if ((employees.size() > 1) || (employee.getEmployeeId() == 0 && employees.size() > 0)
                || (employees.size() == 1 && employees.get(0).getEmployeeId() != employee.getEmployeeId()))
            throw new IllegalFieldValueException("Employee email must be unique");
    }

    private void validatePhoneNumber(Employee employee) {
        List<Employee> employees = repository.findAllByPhoneNumber(employee.getPhoneNumber());
        if ((employees.size() > 1) || (employee.getEmployeeId() == 0 && employees.size() > 0)
                || (employees.size() == 1 && employees.get(0).getEmployeeId() != employee.getEmployeeId()))
            throw new IllegalFieldValueException("Employee phone number must be unique");
    }

    private void validateSalary(Employee employee) {
        if (employee.getSalary() < 1)
            throw new IllegalFieldValueException("Employee salary must be >= 1");
    }

    @Override
    public void validateEmployee(Employee employee) {
        validateEmail(employee);
        validatePhoneNumber(employee);
        validateSalary(employee);
    }
}