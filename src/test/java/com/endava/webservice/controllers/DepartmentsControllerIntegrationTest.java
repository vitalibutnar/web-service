package com.endava.webservice.controllers;

import com.endava.webservice.entities.Department;
import com.endava.webservice.repositories.DepartmentRepository;
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
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class DepartmentsControllerIntegrationTest {
    private final String url = "http://localhost:%s/departments%s";
    private Department department1;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DepartmentRepository departmentRepository;

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    public void setUp() {
        department1 = Department.builder()
                .departmentId(1).departmentName("Developers").managerId(null)
                .location("Chisinau").locationId(100L).build();
    }

    @Test
    @Rollback
    public void getAll_AllDepartments() throws URISyntaxException, JSONException, JsonProcessingException {
        department1 = departmentRepository.save(department1);
        Department department2 = departmentRepository.save(
                Department.builder()
                        .departmentId(2).departmentName("AM").managerId(null)
                        .location("Chisinau").locationId(100L).build()
        );
        Department department3 = departmentRepository.save(
                Department.builder()
                        .departmentId(3).departmentName("DevOps").managerId(null)
                        .location("Chisinau").locationId(100L).build()
        );

        ResponseEntity<String> response = restTemplate.getForEntity(new URI(String.format(url, port, "")), String.class);

        List<Department> expected = asList(department1, department2, department3);

        JSONAssert.assertEquals(objectMapper.writeValueAsString(expected), response.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    @Rollback
    public void getAll_statusCodeOk() throws URISyntaxException {
        department1 = departmentRepository.save(department1);

        ResponseEntity<String> response = restTemplate.getForEntity(new URI(String.format(url, port, "")), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Rollback
    public void getById_Department_idIsCorrect() throws URISyntaxException {
        department1 = departmentRepository.save(department1);

        ResponseEntity<Department> response = restTemplate.getForEntity(new URI(String.format(url, port, "/1")), Department.class);

        assertThat(response.getBody()).isEqualTo(department1);
    }

    @Test
    @Rollback
    public void getById_badRequest_idIsNotCorrect() throws URISyntaxException {
        department1 = departmentRepository.save(department1);

        ResponseEntity<Department> response = restTemplate.getForEntity(new URI(String.format(url, port, "/99")), Department.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    public void updateById_Department_idAndDataIsCorrect() throws URISyntaxException {
        department1 = departmentRepository.save(department1);
        department1.setDepartmentName("Developers");

        ResponseEntity<Department> response = restTemplate.exchange(new RequestEntity<>(
                department1, HttpMethod.PUT, new URI(String.format(url, port, "/1"))), Department.class);

        assertThat(response.getBody()).isEqualTo(department1);
    }

    @Test
    @Rollback
    public void updateById_badRequest_idIsNotCorrect() throws URISyntaxException {
        department1 = departmentRepository.save(department1);
        department1.setDepartmentName("Developers");

        ResponseEntity<Department> response = restTemplate.exchange(new RequestEntity<>(
                department1, HttpMethod.PUT, new URI(String.format(url, port, "/12"))), Department.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    public void updateById_badRequest_dataIsNotCorrect() throws URISyntaxException {
        department1 = departmentRepository.save(department1);
        department1.setDepartmentName(null);

        ResponseEntity<Department> response = restTemplate.exchange(new RequestEntity<>(
                department1, HttpMethod.PUT, new URI(String.format(url, port, "/1"))), Department.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    public void add_department_validData() throws URISyntaxException {
        department1 = departmentRepository.save(department1);

        ResponseEntity<Department> response = restTemplate.exchange(new RequestEntity<>(
                department1, HttpMethod.POST, new URI(String.format(url, port, ""))), Department.class);

        assertThat(response.getBody()).isEqualTo(department1);
    }

    @Test
    @Rollback
    public void add_badRequest_invalidData() throws URISyntaxException {
        department1 = departmentRepository.save(department1);
        department1.setLocation("");

        ResponseEntity<Department> response = restTemplate.exchange(new RequestEntity<>(
                department1, HttpMethod.POST, new URI(String.format(url, port, ""))), Department.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}