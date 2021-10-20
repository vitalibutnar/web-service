package com.endava.webservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "departments")
@Table(name = "departments")
public class Department {
    @Id
    @SequenceGenerator(
            name = "departments_seq",
            sequenceName = "departments_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "departments_seq"
    )
    @Column(name = "department_id")
    private long departmentId;

    @NotBlank(message = "Department name can not be null/empty/blank")
    @Column(nullable = false, name = "department_name")
    private String departmentName;

    @Column(name = "manager_id")
    private Long managerId;

    @NotBlank(message = "Department location can not be null/empty/blank")
    @Column(nullable = false, name = "location")
    private String location;

    @Column(nullable = false, name = "location_id")
    private Long locationId;
}
