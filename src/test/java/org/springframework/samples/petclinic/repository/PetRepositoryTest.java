package org.springframework.samples.petclinic.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"postgresql","spring-data-jpa"})
@Testcontainers
@Sql(value = "classpath:db/postgresql/initDB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:test/insert-pet.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = "classpath:test/delete-pet.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PetRepositoryTest {

    @Autowired
    PetRepository springDataPetRepository;

    @Autowired
    VisitRepository visitRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"))
        .withUsername("testUser")
        .withPassword("testSecret")
        .withDatabaseName("testDatabase");


    @Test
    void findPetTypes() {
        List<PetType> resultado = springDataPetRepository.findPetTypes();
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        assertEquals("cat", resultado.get(0).getName());

    }

    @Test
    void delete() {
        Pet pet = springDataPetRepository.findById(1);
        List<Visit> visitas = pet.getVisits();
        springDataPetRepository.delete(pet);
        assertNull(springDataPetRepository.findById(1));
        assertNull(visitRepository.findById(1));

    }
}
