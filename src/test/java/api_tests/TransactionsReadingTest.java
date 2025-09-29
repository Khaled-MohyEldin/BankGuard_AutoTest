package api_tests;

import api_endpoints.Routes;
import api_endpoints.TransEP;
import api_endpoints.UserEP;
import api_payload.Enums.TransType;
import api_payload.Transaction;
import api_payload.User;
import api_utils.DB;
import api_payload.User;
import com.github.javafaker.Faker;
import com.sun.tools.jconsole.JConsoleContext;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.response.ValidatableResponseOptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class TransactionsReadingTest {
    Faker faker;
    Transaction transPayload;
    int userId, transId;

    @Test
    public void readAllTranactions() {
        Response res = TransEP.getAllTrans();
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 200);

        // checking against schema
        res.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaTransactionAll.json"));
    }

    @Test
    public void readUsersTransactionByAccID() {
        Response res = TransEP.getTransByAccID(6);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 200);

        // checking against schema
        res.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaTransactionAll.json"));
    }

    @Test
    public void readTransactionByID() {
        Response res = TransEP.getTransByID(4);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 200);

        // checking against schema
        res.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaTransaction.json"));
    }

    @Test
    public void readTransactionByReference() {
        Response res = TransEP.getTransByRef("TXN-58B68304");
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 200);

        // checking against schema
        res.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaTransaction.json"));
    }

}
