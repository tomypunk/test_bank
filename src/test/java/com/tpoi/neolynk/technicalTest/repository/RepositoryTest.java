package com.tpoi.neolynk.technicalTest.repository;

import com.tpoi.neolynk.technicalTest.entity.Address;
import com.tpoi.neolynk.technicalTest.entity.User;
import com.tpoi.neolynk.technicalTest.exception.RepositoryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryTest
{
    private final Repository<User> ur = new Repository<>();

    @Before
    public void clearRepository()
    {
        ur.clear();
    }

    @Test(expected = RepositoryException.class)
    public void addNullUser() throws Exception
    {
        User user = null;
        ur.save(Optional.ofNullable(user));
    }

    @Test
    public void addCorrectUser() throws Exception
    {
        Address address = Address.builder()
                .city("Paris")
                .country("France")
                .postalCode("75001")
                .street("1 rue de Rivoli")
                .build();

        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1985");
        User user = User.builder()
                .firstName("Michel")
                .lastName("Durand")
                .address(address)
                .birthDate(birthDate)
                .phoneNumber("0600000000")
                .build();

        ur.save(Optional.ofNullable(user));
        Collection<User> result = ur.findAll();
        assertThat(result).hasSize(1).containsExactlyInAnyOrder(user);
    }

    @Test
    public void deleteUser() throws Exception
    {
        Address address = Address.builder()
                .city("Paris")
                .country("France")
                .postalCode("75001")
                .street("1 rue de Rivoli")
                .build();

        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1985");
        User user = User.builder()
                .firstName("Michel")
                .lastName("Durand")
                .address(address)
                .birthDate(birthDate)
                .phoneNumber("0600000000")
                .build();
        user.setId(UUID.randomUUID());

        User user2 = User.builder()
                .firstName("Michel")
                .lastName("Durant")
                .address(address)
                .birthDate(birthDate)
                .phoneNumber("0600000001")
                .build();
        user2.setId(UUID.randomUUID());

        ur.save(Optional.of(user));
        ur.save(Optional.of(user2));


        Collection<User> result = ur.findAll();
        assertThat(result).hasSize(2).containsExactlyInAnyOrder(user, user2);

        ur.removeEntity(Optional.of(user2));
        Collection<User> result2 = ur.findAll();
        assertThat(result2).hasSize(1).contains(user);
    }

    @Test
    public void updateUser() throws Exception
    {
        Address address = Address.builder()
                .city("Paris")
                .country("France")
                .postalCode("75001")
                .street("1 rue de Rivoli")
                .build();

        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1985");
        User user = User.builder()
                .firstName("Michel")
                .lastName("Durand")
                .address(address)
                .birthDate(birthDate)
                .phoneNumber("0600000000")
                .build();
        user.setId(UUID.randomUUID());

        ur.save(Optional.of(user));
        Collection<User> result = ur.findAll();
        assertThat(result).hasSize(1).contains(user);

        user.setLastName("TOTO");
        ur.save(Optional.of(user));
        Collection<User> result2 = ur.findAll();
        assertThat(result2).hasSize(1).contains(user);
    }

    @Test
    public void findById() throws Exception
    {
        Address address = Address.builder()
                .city("Paris")
                .country("France")
                .postalCode("75001")
                .street("1 rue de Rivoli")
                .build();

        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1985");
        User user = User.builder()
                .firstName("Michel")
                .lastName("Durand")
                .address(address)
                .birthDate(birthDate)
                .phoneNumber("0600000000")
                .build();
        user.setId(UUID.randomUUID());

        UUID uuid = UUID.randomUUID();
        User user2 = User.builder()
                .firstName("Michel")
                .lastName("Durant")
                .address(address)
                .birthDate(birthDate)
                .phoneNumber("0600000001")
                .build();
        user2.setId(uuid);

        ur.save(Optional.of(user));
        ur.save(Optional.of(user2));

        Collection<User> result = ur.findAll();
        assertThat(result).hasSize(2).containsExactlyInAnyOrder(user, user2);

        Optional<User> optionalUser = ur.findById(uuid);
        assertThat(optionalUser).contains(user2);
    }

    @Test
    public void findById_inexistant_id()
    {
        Optional<User> optionalUser = ur.findById(UUID.randomUUID());
        Assert.assertFalse(optionalUser.isPresent());
    }
}
