package com.endava.webservice.service.impl;


import com.endava.webservice.entities.Employee;
import com.endava.webservice.exeption.NoDataFoundException;
import com.endava.webservice.repositories.EmployeeRepository;
import com.endava.webservice.service.EmployeeService;
import com.endava.webservice.service.EmployeeValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;
    private final EmployeeValidationService validationService;

    @Override
    public List<Employee> getAll() {
        return repository.findAll();
    }

    @Override
    public Employee getById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NoDataFoundException(String.format("Could not find an employee with specified id[%d]", id)));
    }

    @Override
    public Employee save(Employee employee) {
        validationService.validateEmployee(employee);
        return repository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        validationService.validateEmployee(employee);
        this.getById(employee.getEmployeeId());
        return repository.save(employee);
    }
}