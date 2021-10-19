package com.endava.webservice.repositories;

import com.endava.webservice.entities.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository {
    List<Department> findAll();

    Optional<Department> findById(long id);

    Department save(Department department);
}