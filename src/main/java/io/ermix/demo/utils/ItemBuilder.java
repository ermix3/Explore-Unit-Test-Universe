package io.ermix.demo.utils;

import io.ermix.demo.user.UserRequest;
import net.datafaker.Faker;

public class ItemBuilder {

  static Faker faker = new Faker();

  public static UserRequest buildUserRequest() {
    return UserRequest.builder()
        .username(faker.name().firstName())
        .email(faker.internet().emailAddress())
        .password(faker.internet().password())
        //                .phone(faker.phoneNumber().phoneNumber())
        .phone(faker.expression("#{regexify '[0-9]{10}'}"))
        .build();
  }
}
