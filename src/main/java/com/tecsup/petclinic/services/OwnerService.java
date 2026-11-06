package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.repositories.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository repository;

    public List<Owner> findAll() {
        return repository.findAll(Sort.by("id")); // Mantiene el listado en orden de ID.
    }

    public Optional<Owner> findById(Long id) {
        return repository.findById(id);
    }
}
