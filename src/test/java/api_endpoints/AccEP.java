package api_endpoints;

import api_payload.Account;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class AccEP {

    // Implement CRUD Operations
    public static Response createAccount(Account payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when().post(Routes.acc_post_url)
                .then().extract().response();
    }

    public static Response getAccByAccID(int id) {
        return given().pathParams("id", id)
                .when().get(Routes.acc_getByUser_url)
                .then().extract().response();
    }

    public static Response getAccByAccNum(String num) {
        return given().pathParams("accountNumber", num)
                .when().get(Routes.acc_getByAccNum_url)
                .then().extract().response();
    }

    public static Response getAccByUserID(int id) {
        return given().pathParams("userId", id)
                .when().get(Routes.acc_getByUser_url)
                .then().extract().response();
    }

    public static Response getAllAccounts() {
        return given().when().get(Routes.acc_getAll_url)
                .then().extract().response();
    }

    public static Response updateAcc(Account payload, int id) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParams("id", id)
                .body(payload)
                .when().put(Routes.acc_update_url)
                .then().extract().response();
    }

    public static Response deleteAcc(int id) {
        return given().pathParams("id", id)
                .when().delete(Routes.acc_delete_url)
                .then().extract().response();
    }

}
