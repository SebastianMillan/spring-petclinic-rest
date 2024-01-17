package org.springframework.samples.petclinic.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"postgresql","spring-data-jpa"})//seleccionamos el perfil activo del pom
@Testcontainers
@Sql(value = "classpath:db/postgresql/initDB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:test/insert-visits.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:test/delete-visits.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VisitRepositoryTests {

    @Autowired
    VisitRepository visitRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"))
        .withUsername("testUser")
        .withPassword("testSecret")
        .withDatabaseName("testDatabase");

    @Test
    void findByPetId(){
        List<Visit> result = visitRepository.findByPetId(1);
        Assertions.assertFalse(result.isEmpty());
    }
}
