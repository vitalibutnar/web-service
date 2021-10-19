package com.endava.webservice.service;

import com.endava.webservice.entities.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> findAll();

    Department getById(Long id);

    Department save(Department department);

    Department update(Department department);
}
