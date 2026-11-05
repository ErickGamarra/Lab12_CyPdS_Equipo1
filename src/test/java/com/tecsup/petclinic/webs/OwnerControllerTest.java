package com.tecsup.petclinic.webs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFindAllOwners() throws Exception {
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testFindOwnerOK() throws Exception {
        mockMvc.perform(get("/owners/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("Franklin"));
    }

    @Test
    void testFindOwnerKO() throws Exception {
        mockMvc.perform(get("/owners/666"))
                .andExpect(status().isNotFound());
    }

    // Integrante B — Creación
    // Prueba 1: testCreateOwner — POST /owners -> 201 Created y jsonPath de los campos enviados
    @Test
    void testCreateOwner() throws Exception {
        String json = """
            {
                "firstName": "Carlos",
                "lastName": "Santana",
                "address": "Av. Principal 123",
                "city": "Lima",
                "telephone": "987654321"
            }
            """;

        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Carlos"))
                .andExpect(jsonPath("$.lastName").value("Santana"))
                .andExpect(jsonPath("$.address").value("Av. Principal 123"))
                .andExpect(jsonPath("$.city").value("Lima"))
                .andExpect(jsonPath("$.telephone").value("987654321"));
    }

    // Prueba 2: testCreateOwner_BadRequest — cuerpo inválido (sin lastName) -> 400 Bad Request
    @Test
    void testCreateOwner_BadRequest() throws Exception {
        String jsonWithoutLastName = """
            {
                "firstName": "Carlos",
                "address": "Av. Principal 123",
                "city": "Lima",
                "telephone": "987654321"
            }
            """;

        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithoutLastName))
                .andExpect(status().isBadRequest());
    }

    // Prueba 3: testFindByLastName — GET /owners?lastName=Davis -> 2 resultados
    @Test
    void testFindByLastName() throws Exception {
        mockMvc.perform(get("/owners").param("lastName", "Davis"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].lastName").value("Davis"))
                .andExpect(jsonPath("$[1].lastName").value("Davis"));
    }
}
