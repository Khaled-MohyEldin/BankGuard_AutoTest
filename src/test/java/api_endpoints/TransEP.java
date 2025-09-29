package api_endpoints;

import api_payload.Transaction;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class TransEP {

    public static Response createTranaction(Transaction payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when().post(Routes.trans_post_url)
                .then().extract().response();
    }

    public static Response getTransByAccID(int AccID) {
        return given()
                .pathParams("accountId", AccID)
                .when().get(Routes.trans_getByAccID_url)
                .then().extract().response();
    }

    public static Response getTransByID(int id) {
        return given().pathParams("id", id)
                .when().get(Routes.trans_get_url)
                .then().extract().response();
    }

    public static Response getAllTrans() {
        return given().when().get(Routes.trans_getAll_url)
                .then().extract().response();
    }

    public static Response getTransByRef(String reference) {
        return given().pathParams("reference", reference)
                .when().get(Routes.trans_getByRef_url)
                .then().extract().response();
    }

}
