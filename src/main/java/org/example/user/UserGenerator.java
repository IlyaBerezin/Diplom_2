package org.example.user;

import com.github.javafaker.Faker;

public class UserGenerator {


        private static final Faker faker = new Faker();

        public static CreateUser generateValidUser() {
            return new CreateUser(
                    faker.internet().emailAddress(),
                    faker.internet().password(8, 16),
                    faker.name().firstName()
            );
        }

    public static CreateUser generateEmailOnlyUser() {
            String login = faker.internet().emailAddress();
            String password = faker.internet().password(8, 16);
            return new CreateUser(login, password, "");
    }

    public static CreateUser generatePasswordOnlyUser() {
        String password = faker.internet().password(8, 16);
        String name = faker.name().firstName();
        return new CreateUser("", password, name);
    }

    public static CreateUser generateNameOnlyUser() {
        String login = faker.internet().emailAddress();
        String name = faker.name().firstName();
        return new CreateUser(login, "", name);
    }

}
