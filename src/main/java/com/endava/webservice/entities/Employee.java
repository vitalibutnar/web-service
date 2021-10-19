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
import java.time.LocalDate;

@Data
@Entity(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
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
    @Column(name = "employee_id")
    private long employeeId;

    @NotBlank(message = "Employee firstname can not be null/empty/blank")
    @Column(nullable = false, name = "first_name")
    private String firstName;

    @NotBlank(message = "Employee lastName can not be null/empty/blank")
    @Column(nullable = false, name = "last_name")
    private String lastName;

    @NotBlank(message = "Employee email can not be null/empty/blank")
    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @NotBlank(message = "Employee phoneNumber can not be null/empty/blank")
    @Column(nullable = false, unique = true, name = "phone_number")
    private String phoneNumber;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "job_id")
    private long jobId;

    @Column(name = "salary")
    private double salary;

    @Column(name = "commission_pct")
    private Double commissionPct;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "department_id")
    private long departmentId;

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
