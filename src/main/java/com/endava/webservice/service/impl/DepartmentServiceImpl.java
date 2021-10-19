package com.endava.webservice.service.impl;

import com.endava.webservice.entities.Department;
import com.endava.webservice.exeption.NoDataFoundException;
import com.endava.webservice.repositories.DepartmentRepository;
import com.endava.webservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;

    @Override
    public List<Department> findAll() {
        return repository.findAll();
    }

    @Override
    public Department getById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NoDataFoundException(String.format("Could not find a department with specified id[%d]", id)));
    }

    @Override
    public Department save(Department department) {
        return repository.save(department);
    }

    @Override
    public Department update(Department department) {
        getById(department.getDepartmentId());
        return repository.save(department);
    }
}
