package com.endava.webservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Departments")
public class Department {
    @Id
    @GeneratedValue
    private long departmentId;

    @NotNull(message = "Department name can not be null")
    @NotEmpty(message = "Department name can not be empty")
    @NotBlank(message = "Department name can not be blank")
    @Column(nullable = false)
    private String departmentName;

    private Long managerId;

    @NotNull(message = "Department location can not be null")
    @NotEmpty(message = "Department location can not be empty")
    @NotBlank(message = "Department location can not be blank")
    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Long locationId;
}
