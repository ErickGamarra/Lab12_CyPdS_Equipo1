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

    public static Owner owner(long id, String firstName, String lastName, String address, String city, String telephone) {
        Owner owner = new Owner();
        owner.setId(id);
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setAddress(address);
        owner.setCity(city);
        owner.setTelephone(telephone);
        return owner;
    }
}
