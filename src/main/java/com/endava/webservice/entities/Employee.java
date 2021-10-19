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
import java.time.LocalDate;

@Data
@Entity(name = "Employees")
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue
    private long employeeId;

    @NotNull(message = "Employee firstname can not be null")
    @NotEmpty(message = "Employee firstname can not be empty")
    @NotBlank(message = "Employee firstname can not be blank")
    @Column(nullable = false)
    private String firstName;

    @NotNull(message = "Employee lastname can not be null")
    @NotEmpty(message = "Employee lastname can not be empty")
    @NotBlank(message = "Employee lastname can not be blank")
    @Column(nullable = false)
    private String lastName;


    @NotNull(message = "Employee email can not be null")
    @NotEmpty(message = "Employee email can not be empty")
    @NotBlank(message = "Employee email can not be blank")
    @Column(nullable = false, unique = true)
    private String email;


    @NotNull(message = "Employee phone number can not be null")
    @NotEmpty(message = "Employee phone number can not be empty")
    @NotBlank(message = "Employee phone number can not be blank")
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
