package com.endava.webservice.repositories.impl;

import com.endava.webservice.entities.Department;
import com.endava.webservice.exeption.HibernateTransactionException;
import com.endava.webservice.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final SessionFactory sessionFactory;

    @Override
    public List<Department> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM departments", Department.class).list();
        } catch (Exception exception) {
            throw new HibernateTransactionException(
                    String.format("Could not execute transaction by {%s} reason", exception.getMessage()));
        }
    }

    @Override
    public Optional<Department> findById(long id) {
        Optional<Department> result = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            result = Optional.of(session.get(Department.class, id));
        } catch (Exception ignored) {
        }
        return result;
    }

    @Override
    public Department save(Department department) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(department);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateTransactionException(
                    String.format("Could not create department  by {%s} reason", exception.getMessage()));
        }
        return department;
    }
}