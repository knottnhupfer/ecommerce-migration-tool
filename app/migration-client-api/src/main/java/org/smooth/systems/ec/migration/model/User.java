package org.smooth.systems.ec.migration.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class User {

  public enum CustomerType {
    Company, Mister, Miss
  }

  private String language;

  private String firstName;

  private String lastName;

  private String email;

  private LocalTime birthDate;

  private LocalDate registrationDate;

  private List<Long> groupIds;

  private List<Address> addresses;
}
