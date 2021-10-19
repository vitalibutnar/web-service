package com.endava.webservice.repositories.impl;

import com.endava.webservice.entities.Department;
import com.endava.webservice.repositories.DepartmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepositoryImpl extends JpaRepository<Department, Long>, DepartmentRepository {
}
