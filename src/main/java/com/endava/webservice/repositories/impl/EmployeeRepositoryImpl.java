package com.endava.webservice.repositories.impl;

import com.endava.webservice.entities.Employee;
import com.endava.webservice.repositories.EmployeeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepositoryImpl extends JpaRepository<Employee, Long>, EmployeeRepository {
}