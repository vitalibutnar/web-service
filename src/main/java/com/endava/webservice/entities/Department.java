package com.endava.webservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Departments")
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
    private long departmentId;

    @NotBlank(message = "Department name can not be null/empty/blank")
    @Column(nullable = false)
    private String departmentName;

    private Long managerId;

    @NotBlank(message = "Department location can not be null/empty/blank")
    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Long locationId;
}
