package com.tecsup.petclinic.webs;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    // Integrante C — Actualización y borrado
    // Crea un dueño con POST y devuelve el id generado por la base de datos H2
    private int createOwnerAndGetId(String firstName, String lastName) throws Exception {
        String json = """
            {
                "firstName": "%s",
                "lastName": "%s",
                "address": "Calle Los Pinos 45",
                "city": "Arequipa",
                "telephone": "912345678"
            }
            """.formatted(firstName, lastName);

        MvcResult result = mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    // Prueba 1: testUpdateOwner — POST + PUT /owners/{id} -> 200 OK, luego GET confirma el cambio
    @Test
    void testUpdateOwner() throws Exception {
        int id = createOwnerAndGetId("Mario", "Paredes");

        String updatedJson = """
            {
                "firstName": "Mario",
                "lastName": "Paredes Soto",
                "address": "Av. Ejercito 900",
                "city": "Cusco",
                "telephone": "955000111"
            }
            """;

        mockMvc.perform(put("/owners/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.lastName").value("Paredes Soto"))
                .andExpect(jsonPath("$.city").value("Cusco"));

        mockMvc.perform(get("/owners/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Paredes Soto"))
                .andExpect(jsonPath("$.address").value("Av. Ejercito 900"))
                .andExpect(jsonPath("$.city").value("Cusco"))
                .andExpect(jsonPath("$.telephone").value("955000111"));
    }

    // Prueba 2: testDeleteOwner — POST + DELETE /owners/{id} -> 200 OK
    @Test
    void testDeleteOwner() throws Exception {
        int id = createOwnerAndGetId("Lucia", "Ramos");

        mockMvc.perform(delete("/owners/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/owners/" + id))
                .andExpect(status().isNotFound());
    }

    // Prueba 3: testDeleteOwnerKO — DELETE /owners/1000 -> 404 Not Found
    @Test
    void testDeleteOwnerKO() throws Exception {
        mockMvc.perform(delete("/owners/1000"))
                .andExpect(status().isNotFound());
    }
}
