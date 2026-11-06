package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.services.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService service;

    @GetMapping
    public List<Owner> findAll(@RequestParam(name = "lastName", required = false) String lastName) {
        if (lastName != null && !lastName.isBlank()) {
            return service.findByLastName(lastName);
        }
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> findById(@PathVariable Long id) {
        return ResponseEntity.of(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Owner> create(@RequestBody Owner owner) {
        if (owner.getLastName() == null || owner.getLastName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Owner created = service.create(owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
