package api_endpoints;

import api_payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserEP {

    // Implement CRUD Operations
    public static Response createUser(User payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when().post(Routes.user_post_url)
                .then().extract().response();
    }

    public static Response getUserByName(String userName) {
        return given()
                .pathParams("username", userName)
                .when().get(Routes.user_getByName_url)
                .then().extract().response();
    }

    public static Response getUserByID(int id) {
        return given().pathParams("id", id)
                .when().get(Routes.user_get_url)
                .then().extract().response();
    }

    public static Response getAllUsers() {
        return given().when().get(Routes.user_getAll_url)
                .then().extract().response();
    }

    public static Response updateUser(User payload, int id) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParams("id", id)
                .body(payload)
                .when().put(Routes.user_update_url)
                .then().extract().response();
    }

    public static Response deleteUser(int id) {
        return given().pathParams("id", id)
                .when().delete(Routes.user_delete_url)
                .then().extract().response();
    }

}
