package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }


    // Todo: Отправить запрос на указанный в требованиях path, передав в body запроса объект и подготовленную спецификацию requestSpec.
    private static void sendRequest(RegistrationInfo user) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String getRandomLogin() {
        String login = faker.name().username();
        return login;
    }

    public static String getRandomPassword() {
        String password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationInfo getUser(String status) {
            RegistrationInfo user = new RegistrationInfo(getRandomLogin(), getRandomPassword(), status);
            // Создать пользователя user используя методы getRandomLogin(), getRandomPassword() и параметр status
            return user;
        }

        public static RegistrationInfo getRegisteredUser(String status) {
            RegistrationInfo registeredUser = getUser(status);
            // Объявить переменную registeredUser и присвоить ей значение возвращённое getUser(status).
            sendRequest(registeredUser);
            // Послать запрос на регистрацию пользователя с помощью вызова sendRequest(registeredUser)
            return registeredUser;
        }
    }

    @Value
    public static class RegistrationInfo {
        String login;
        String password;
        String status;
    }
}
