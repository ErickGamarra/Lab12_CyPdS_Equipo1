package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.services.OwnerService;
import com.tecsup.petclinic.util.OwnerTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.tecsup.petclinic.util.OwnerTestData.owner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OwnerControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerService service;

    @Test
    void testFindAllOwners() throws Exception {
        when(service.findAll()).thenReturn(List.of(owner(1, "Franklin"), owner(2, "Davis")));
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testFindOwnerOK() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(owner(1, "Franklin")));
        mockMvc.perform(get("/owners/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("Franklin"));
    }

    @Test
    void testFindOwnerKO() throws Exception {
        when(service.findById(666L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/owners/666"))
                .andExpect(status().isNotFound());
    }

    // Integrante B — Creación
    // Prueba 1: testCreateOwner — POST /owners -> 201 Created y jsonPath de los campos enviados
    @Test
    void testCreateOwner() throws Exception {
        Owner newOwner = OwnerTestData.owner(100L, "Carlos", "Santana", "Av. Principal 123", "Lima", "987654321");
        when(service.create(any(Owner.class))).thenReturn(newOwner);

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
                .andExpect(jsonPath("$.id").value(100))
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
        when(service.findByLastName("Davis")).thenReturn(List.of(
                OwnerTestData.owner(2L, "Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749"),
                OwnerTestData.owner(4L, "Harold", "Davis", "563 Friendly St.", "Windsor", "6085553198")
        ));

        mockMvc.perform(get("/owners").param("lastName", "Davis"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].lastName").value("Davis"))
                .andExpect(jsonPath("$[1].lastName").value("Davis"));
    }

    // Integrante C — Actualización y borrado
    // Prueba 1: testUpdateOwner — PUT /owners/{id} -> 200 OK con el dueño actualizado
    @Test
    void testUpdateOwner() throws Exception {
        Owner updated = OwnerTestData.owner(7L, "Mario", "Paredes Soto", "Av. Ejercito 900", "Cusco", "955000111");
        when(service.update(eq(7L), any(Owner.class))).thenReturn(Optional.of(updated));

        String updatedJson = """
            {
                "firstName": "Mario",
                "lastName": "Paredes Soto",
                "address": "Av. Ejercito 900",
                "city": "Cusco",
                "telephone": "955000111"
            }
            """;

        mockMvc.perform(put("/owners/7")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.lastName").value("Paredes Soto"))
                .andExpect(jsonPath("$.city").value("Cusco"));

        verify(service).update(eq(7L), any(Owner.class));
    }

    // Prueba 2: testDeleteOwner — DELETE /owners/{id} -> 200 OK
    @Test
    void testDeleteOwner() throws Exception {
        when(service.delete(7L)).thenReturn(true);

        mockMvc.perform(delete("/owners/7"))
                .andExpect(status().isOk());

        verify(service).delete(7L);
    }

    // Prueba 3: testDeleteOwnerKO — DELETE /owners/1000 -> 404 Not Found
    @Test
    void testDeleteOwnerKO() throws Exception {
        when(service.delete(1000L)).thenReturn(false);

        mockMvc.perform(delete("/owners/1000"))
                .andExpect(status().isNotFound());
    }
}
