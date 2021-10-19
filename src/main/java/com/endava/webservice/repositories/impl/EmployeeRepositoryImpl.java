package com.endava.webservice.repositories.impl;

import com.endava.webservice.entities.Employee;
import com.endava.webservice.exeption.HibernateTransactionException;
import com.endava.webservice.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final SessionFactory sessionFactory;

    @Override
    public List<Employee> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM employees", Employee.class).list();
        } catch (Exception exception) {
            throw new HibernateTransactionException(
                    String.format("Could not execute transaction by {%s} reason", exception.getMessage()));
        }
    }

    @Override
    public Optional<Employee> findById(long id) {
        Optional<Employee> result = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            result = Optional.of(session.get(Employee.class, id));
        } catch (Exception ignored) {
        }
        return result;
    }

    @Override
    public Employee save(Employee employee) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(employee);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateTransactionException(
                    String.format("Could not create employee  by {%s} reason", exception.getMessage()));
        }
        return employee;
    }

    @Override
    public List<Employee> findAllByEmail(String email) {
        Session session = sessionFactory.openSession();
        return session.createQuery(String.format("FROM employees WHERE email = '%s'", email), Employee.class).list();
    }

    @Override
    public List<Employee> findAllByPhoneNumber(String phoneNumber) {
        Session session = sessionFactory.openSession();
        return session.createQuery(String.format("FROM employees WHERE phone_number = '%s'", phoneNumber), Employee.class).list();
    }
}