package com.endava.webservice.repositories.impl;

import com.endava.webservice.entities.Department;
import com.endava.webservice.exeption.DataBaseConnectionException;
import com.endava.webservice.repositories.DepartmentRepository;
import com.endava.webservice.repositories.databaseConnection.DatabaseConnectionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DatabaseConnectionManager connectionManager;

    @Override
    @Transactional
    public List<Department> findAll() {
        String query = "SELECT * FROM departments";
        List<Department> departments = new ArrayList<>();
        try (PreparedStatement statement = connectionManager.getPreparedStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                departments.add(departmentBuilder(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataBaseConnectionException(String.format("Could not complete transaction by {%s} reason", exception.getMessage()));
        }
        return departments;
    }

    @Override
    @Transactional
    public Optional<Department> findById(long id) {
        String query = String.format("SELECT * FROM departments WHERE department_id = %d", id);
        Optional<Department> result = Optional.empty();
        try (PreparedStatement statement = connectionManager.getPreparedStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result = Optional.of(departmentBuilder(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataBaseConnectionException(String.format("Could not complete transaction by {%s} reason", exception.getMessage()));
        }
        return result;
    }

    @Override
    @Transactional
    public Department save(Department department) {
        return department.getDepartmentId() == 0
                ? saveNew(department)
                : update(department);
    }

    private Department saveNew(Department department) {
        ResultSet resultSet = null;
        String query = "INSERT INTO " +
                "departments (department_name, manager_id, location, location_id) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connectionManager
                .getPreparedStatement(query, new String[]{"department_id"})) {
            prepareStatement(department, statement);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            while (resultSet.next())
                department.setDepartmentId(resultSet.getLong(1));
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
        return department;
    }

    private Department update(Department department) {
        String query = "UPDATE departments " +
                "SET department_name = ?, manager_id = ?, location = ?, location_id = ? " +
                "WHERE department_id = ?";
        try (PreparedStatement statement = connectionManager.getPreparedStatement(query)) {
            prepareStatement(department, statement);
            statement.setLong(5, department.getDepartmentId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataBaseConnectionException(String.format("Could not complete transaction by {%s} reason", exception.getMessage()));
        }
        return department;
    }

    private void prepareStatement(Department department, PreparedStatement statement) throws SQLException {
        statement.setString(1, department.getDepartmentName());
        statement.setObject(2, department.getManagerId());
        statement.setString(3, department.getLocation());
        statement.setLong(4, department.getLocationId());
    }

    private Department departmentBuilder(ResultSet resultSet) throws SQLException {
        long managerId;
        return Department.builder()
                .departmentId(resultSet.getLong("department_id"))
                .departmentName(resultSet.getString("department_name"))
                .managerId((managerId = resultSet.getLong("manager_id")) == 0 ? null : managerId)
                .location(resultSet.getString("location"))
                .locationId(resultSet.getLong("location_id"))
                .build();
    }
}
