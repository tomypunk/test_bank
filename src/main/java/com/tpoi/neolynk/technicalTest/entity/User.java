package com.tpoi.neolynk.technicalTest.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, of = "id")
@ToString(callSuper = true)
public class User extends AbstractEntity
{
    String firstName;
    String lastName;
    Date birthDate;
    Address address;
    String phoneNumber;
    @Builder.Default
    Set<Account> accounts = new HashSet<>();
}
