package com.endava.webservice.controllers;

import com.endava.webservice.entities.Employee;
import com.endava.webservice.repositories.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class EmployeesControllerIntegrationTest {
    private final String url = "http://localhost:%s/employees%s";
    private Employee employee1;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EmployeeRepository employeeRepository;

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    public void setUp() {
        employee1 = Employee.builder().employeeId(1).firstName("Oleg").lastName("Belov")
                .email("oleg.belov@endava.com").phoneNumber("079887766")
                .hireDate(LocalDate.now()).jobId(1).salary(1700)
                .commissionPct(null).managerId(null).departmentId(10)
                .build();
    }

    @Test
    @Rollback
    public void getAll_AllEmployees() throws URISyntaxException, JSONException, JsonProcessingException {
        employee1 = employeeRepository.save(employee1);
        Employee employee2 = employeeRepository.save(Employee.builder().employeeId(2).firstName("Oleg").lastName("Cernei")
                .email("oleg.cernei@endava.com").phoneNumber("079776655")
                .hireDate(LocalDate.now()).jobId(1).salary(700)
                .commissionPct(null).managerId(null).departmentId(10)
                .build());
        Employee employee3 = employeeRepository.save(Employee.builder().employeeId(3).firstName("Irina").lastName("Fiodorov")
                .email("irina.fiodorov@endava.com").phoneNumber("079665544")
                .hireDate(LocalDate.now()).jobId(1).salary(700)
                .commissionPct(null).managerId(null).departmentId(10)
                .build());

        ResponseEntity<String> response = restTemplate.getForEntity(new URI(String.format(url, port, "")), String.class);

        List<Employee> expected = asList(employee1, employee2, employee3);

        JSONAssert.assertEquals(objectMapper.writeValueAsString(expected), response.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    @Rollback
    public void getAll_statusCodeOk() throws URISyntaxException {
        employee1 = employeeRepository.save(employee1);

        ResponseEntity<String> response = restTemplate.getForEntity(new URI(String.format(url, port, "")), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Rollback
    public void getById_Employee_idIsCorrect() throws URISyntaxException {
        employee1 = employeeRepository.save(employee1);

        ResponseEntity<Employee> response = restTemplate.getForEntity(new URI(String.format(url, port, "/1")), Employee.class);

        assertThat(response.getBody()).isEqualTo(employee1);
    }

    @Test
    @Rollback
    public void getById_badRequest_idIsNotCorrect() throws URISyntaxException {
        employee1 = employeeRepository.save(employee1);

        ResponseEntity<Employee> response = restTemplate.getForEntity(new URI(String.format(url, port, "/99")), Employee.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    public void updateById_Employee_idAndDataIsCorrect() throws URISyntaxException {
        employee1 = employeeRepository.save(employee1);
        employee1.setFirstName("Vasili");

        ResponseEntity<Employee> response = restTemplate.exchange(new RequestEntity<>(
                employee1, HttpMethod.PUT, new URI(String.format(url, port, "/1"))), Employee.class);

        assertThat(response.getBody()).isEqualTo(employee1);
    }

    @Test
    @Rollback
    public void updateById_badRequest_idIsNotCorrect() throws URISyntaxException {
        employee1 = employeeRepository.save(employee1);
        employee1.setSalary(2000);

        ResponseEntity<Employee> response = restTemplate.exchange(new RequestEntity<>(
                employee1, HttpMethod.PUT, new URI(String.format(url, port, "/12"))), Employee.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    public void updateById_badRequest_dataIsNotCorrect() throws URISyntaxException {
        employee1 = employeeRepository.save(employee1);
        employee1.setEmail("");

        ResponseEntity<Employee> response = restTemplate.exchange(new RequestEntity<>(
                employee1, HttpMethod.PUT, new URI(String.format(url, port, "/1"))), Employee.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    public void updateById_badRequestWithMessageNotUniquePN_dataIsNotCorrect() throws URISyntaxException {
        String notUniquePhoneNumber = "079776655";
        employee1 = employeeRepository.save(employee1);
        employeeRepository.save(Employee.builder().employeeId(2).firstName("Oleg").lastName("Cernei")
                .email("oleg.cernei@endava.com").phoneNumber(notUniquePhoneNumber)
                .hireDate(LocalDate.now()).jobId(1).salary(700)
                .commissionPct(null).managerId(null).departmentId(10)
                .build());
        employee1.setPhoneNumber(notUniquePhoneNumber);

        ResponseEntity<Map> response = restTemplate.exchange(new RequestEntity<>(
                employee1, HttpMethod.PUT, new URI(String.format(url, port, "/1"))), Map.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.getBody().get("reason")).isEqualTo("Employee phone number must be unique")
        );
    }

    @Test
    @Rollback
    public void add_badRequestWithMessageBadSalary_dataIsNotCorrect() throws URISyntaxException {
        employee1.setSalary(0.001);

        ResponseEntity<Map> response = restTemplate.exchange(new RequestEntity<>(
                employee1, HttpMethod.POST, new URI(String.format(url, port, ""))), Map.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.getBody().get("reason")).isEqualTo("Employee salary must be >= 1")
        );
    }

    @Test
    @Rollback
    public void add_Employee_validData() throws URISyntaxException {
        employee1 = employeeRepository.save(employee1);

        ResponseEntity<Employee> response = restTemplate.exchange(new RequestEntity<>(
                employee1, HttpMethod.POST, new URI(String.format(url, port, ""))), Employee.class);

        assertThat(response.getBody()).isEqualTo(employee1);
    }

    @Test
    @Rollback
    public void add_badRequest_invalidData() throws URISyntaxException {
        employee1 = employeeRepository.save(employee1);
        employee1.setPhoneNumber("");

        ResponseEntity<Employee> response = restTemplate.exchange(new RequestEntity<>(
                employee1, HttpMethod.POST, new URI(String.format(url, port, ""))), Employee.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}