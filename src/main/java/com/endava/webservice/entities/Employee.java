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
import java.time.LocalDate;

@Data
@Builder
@Entity(name = "Employees")
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @SequenceGenerator(
            name = "employees_seq",
            sequenceName = "employees_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "employees_seq"
    )
    private long employeeId;

    @NotBlank(message = "Employee firstname can not be null/empty/blank")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Employee lastname can not be null/empty/blank")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Employee email can not be null/empty/blank")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Employee phone number can not be null/empty/blank")
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private LocalDate hireDate;

    private long jobId;

    private double salary;

    private Double commissionPct;

    private Long managerId;

    private long departmentId;

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
