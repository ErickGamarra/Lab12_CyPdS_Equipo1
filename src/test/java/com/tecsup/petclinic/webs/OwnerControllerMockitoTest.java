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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class   OwnerControllerMockitoTest {
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
}
