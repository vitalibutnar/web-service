package com.endava.webservice.repositories.sessionFactory;

import com.endava.webservice.entities.Department;
import com.endava.webservice.entities.Employee;
import com.endava.webservice.exeption.HibernateInitializationException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@org.springframework.context.annotation.Configuration
@EnableTransactionManagement
public class HibernateSessionFactory {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.max-active}")
    private String poolSize;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String dllAuto;

    @Bean
    public Properties properties() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, driver);
        properties.put(Environment.URL, url);
        properties.put(Environment.USER, username);
        properties.put(Environment.PASS, password);
        properties.put(Environment.POOL_SIZE, poolSize);
        properties.put(Environment.HBM2DDL_AUTO, dllAuto);
        return properties;
    }

    @Bean
    public Configuration configuration(Properties properties) {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(Department.class);
        configuration.addAnnotatedClass(Employee.class);
        return configuration;
    }

    @Bean
    public ServiceRegistry serviceRegistry(Configuration configuration) {
        return new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
    }

    @Bean
    public SessionFactory sessionFactory(Configuration configuration, ServiceRegistry serviceRegistry) {
        SessionFactory sessionFactory;
        try {
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception exception) {
            throw new HibernateInitializationException(
                    String.format("Could not create session factory by {%s} reason", exception.getMessage()));
        }
        return sessionFactory;
    }
}
