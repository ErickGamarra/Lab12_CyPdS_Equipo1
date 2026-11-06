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
        return repository.findAll(Sort.by("id"));
    }

    public Optional<Owner> findById(Long id) {
        return repository.findById(id);
    }

    public Owner create(Owner owner) {
        return repository.save(owner);
    }

    public List<Owner> findByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    public Optional<Owner> update(Long id, Owner owner) {
        return repository.findById(id).map(existing -> {
            existing.setFirstName(owner.getFirstName());
            existing.setLastName(owner.getLastName());
            existing.setAddress(owner.getAddress());
            existing.setCity(owner.getCity());
            existing.setTelephone(owner.getTelephone());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
