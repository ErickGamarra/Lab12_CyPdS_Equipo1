package com.tecsup.petclinic.util;

import com.tecsup.petclinic.entities.Owner;

public final class OwnerTestData {
    private OwnerTestData() {
    }

    public static Owner owner(long id, String lastName) {
        Owner owner = new Owner();
        owner.setId(id);
        owner.setLastName(lastName);
        return owner;
    }
}
