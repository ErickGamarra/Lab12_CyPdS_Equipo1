package com.tecsup.petclinic.webs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.tecsup.petclinic.services.OwnerService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static com.tecsup.petclinic.util.OwnerTestData.owner;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OwnerControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerService service; // El controlador recibe resultados preparados en cada prueba.

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
}
