package com.endava.webservice.controllers;

import com.endava.webservice.entities.Department;
import com.endava.webservice.service.DepartmentService;
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
@RequestMapping("departments")
@RequiredArgsConstructor
public class DepartmentsController {
    private final DepartmentService departmentService;

    @GetMapping
    List<Department> getAll() {
        return departmentService.findAll();
    }

    @GetMapping("{id}")
    Department getById(@PathVariable long id) {
        return departmentService.getById(id);
    }

    @PutMapping("{id}")
    Department updateById(@PathVariable long id, @Valid @RequestBody Department department) {
        department.setDepartmentId(id);
        return departmentService.update(department);
    }

    @PostMapping
    Department add(@Valid @RequestBody Department department) {
        return departmentService.save(department);
    }
}