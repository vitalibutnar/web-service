package com.endava.webservice.repositories.impl;

import com.endava.webservice.entities.Employee;
import com.endava.webservice.exeption.DataBaseConnectionException;
import com.endava.webservice.repositories.EmployeeRepository;
import com.endava.webservice.repositories.databaseConnection.DatabaseConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final DatabaseConnectionManager connectionManager;

    @Override
    @Transactional
    public List<Employee> findAll() {
        String query = "SELECT * FROM employees";
        return findAllByQuery(query);
    }

    @Override
    @Transactional
    public Optional<Employee> findById(long id) {
        String query = String.format("SELECT * FROM employees WHERE employee_id = %d", id);
        Optional<Employee> result = Optional.empty();
        try (PreparedStatement statement = connectionManager.getPreparedStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result = Optional.of(employeeBuilder(resultSet));
            }

        } catch (SQLException exception) {
            throw new DataBaseConnectionException(String.format("Could not complete transaction by {%s} reason", exception.getMessage()));
        }
        return result;
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        return employee.getEmployeeId() == 0
                ? saveNew(employee)
                : update(employee);
    }

    @Override
    @Transactional
    public List<Employee> findAllByEmail(String email) {
        String query = String.format("SELECT * FROM employees WHERE email = '%s'", email);
        return findAllByQuery(query);
    }

    @Override
    @Transactional
    public List<Employee> findAllByPhoneNumber(String phoneNumber) {
        String query = String.format("SELECT * FROM employees WHERE phone_number = '%s'", phoneNumber);
        return findAllByQuery(query);
    }

    private List<Employee> findAllByQuery(String query) {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement statement = connectionManager.getPreparedStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                employees.add(employeeBuilder(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataBaseConnectionException(String.format("Could not complete transaction by {%s} reason", exception.getMessage()));
        }
        return employees;
    }

    private Employee saveNew(Employee employee) {
        String query = "INSERT INTO employees (first_name, last_name, email, phone_number, hire_date, job_id, " +
                "salary, commission_pct, manager_id, department_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        ResultSet resultSet = null;
        try (PreparedStatement statement = connectionManager.getPreparedStatement(query,
                new String[]{"employee_id"})) {
            prepareStatement(employee, statement);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            while (resultSet.next())
                employee.setEmployeeId(resultSet.getLong(1));
        } catch (SQLException exception) {
            throw new DataBaseConnectionException(String.format("Could not complete transaction by {%s} reason", exception.getMessage()));
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return employee;
    }

    private Employee update(Employee employee) {
        String query = "UPDATE employees  " +
                "SET first_name = ?, last_name = ?, email = ?, phone_number = ?, hire_date = ?, job_id = ?, " +
                "salary = ?, commission_pct = ?, manager_id = ?, department_id = ? " +
                "WHERE employee_id= ?";
        try (PreparedStatement statement = connectionManager.getPreparedStatement(query)) {
            prepareStatement(employee, statement);
            statement.setLong(11, employee.getEmployeeId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataBaseConnectionException(String.format("Could not complete transaction by {%s} reason", exception.getMessage()));
        }
        return employee;
    }

    private void prepareStatement(Employee employee, PreparedStatement statement) throws SQLException {
        statement.setString(1, employee.getFirstName());
        statement.setObject(2, employee.getLastName());
        statement.setString(3, employee.getEmail().replace(".", "1"));
        statement.setString(4, employee.getPhoneNumber());
        statement.setDate(5, Date.valueOf(employee.getHireDate()));
        statement.setLong(6, employee.getJobId());
        statement.setDouble(7, employee.getSalary());
        statement.setObject(8, employee.getCommissionPct());
        statement.setObject(9, employee.getManagerId());
        statement.setLong(10, employee.getDepartmentId());
    }

    private Employee employeeBuilder(ResultSet resultSet) throws SQLException {
        long managerId;
        return Employee.builder()
                .employeeId(resultSet.getLong("employee_id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .phoneNumber(resultSet.getString("phone_number"))
                .hireDate(LocalDate.parse(resultSet.getString("hire_date").substring(0, 10)))
                .jobId(resultSet.getLong("job_id"))
                .salary(resultSet.getDouble("salary"))
                .commissionPct(resultSet.getDouble("commission_pct"))
                .managerId((managerId = resultSet.getLong("manager_id")) == 0 ? null : managerId)
                .departmentId(resultSet.getLong("department_id"))
                .build();
    }
}
