package api_tests;

import api_endpoints.AccEP;
import api_endpoints.Routes;
import api_endpoints.UserEP;
import api_payload.Account;
import api_payload.User;
import api_utils.DB;
import api_payload.User;
import api_payload.Account;
import api_payload.Enums.AccountType;
import api_payload.Enums.AccStatus;
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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class AccountsTest {
    Faker faker;
    Account accPayload;
    int AccId, userId;
    String accNum;

    @BeforeTest
    public void setup(ITestContext context) {
        faker = new Faker();
        accPayload = new Account();
        // getting UserId
        Object userIdObj = context.getSuite().getAttribute("userId");
        userId = userIdObj != null ? (int) userIdObj : -1; // or any default

        // accPayload.setId(faker.idNumber().hashCode());
        accPayload.setUserId(5);
        accPayload.setBalance(faker.number().randomDouble(2, 0, 1000000));
        accPayload.setCreditLimit(faker.number().randomDouble(2, 0, 10000));
        accPayload.setAccountType(AccountType.CREDIT);
        accPayload.setStatus(AccStatus.ACTIVE);

    }

    @Test
    public void createAccount() throws SQLException {
        Response res = AccEP.createAccount(accPayload);
        // System.out.println(res.statusCode() +" \\ "+ res.prettyPrint());
        AccId = res.path("id");
        accNum = res.path("accountNumber");
        Assert.assertEquals(res.getStatusCode(), 201);

        // Validate DB data
        ResultSet rs = DB.executeQuery("SELECT * FROM ACCOUNTS WHERE ID = ?", AccId);
        if (rs.next()) {
            int founduser_id = rs.getInt("user_id");
            System.out.println("user in DB =>" + founduser_id);
            Assert.assertTrue(founduser_id == 5);
            // assertThat(dbEmail, is(accPayload.getEmail()));
        } else {
            throw new AssertionError("User not found in DB!");
        }
        rs.close();
    }

    @Test
    public void deleteAccount() throws SQLException {
        // API side
        Response res = AccEP.deleteAcc(AccId);
        Assert.assertEquals(res.getStatusCode(), 204);

        // DB Side
        ResultSet rs = DB.executeQuery("SELECT * FROM ACCOUNTS WHERE ID = ?", AccId);
        if (!rs.isBeforeFirst())
            System.out.println("results are empty");
        // asserting that results is empty (no record of that user)
        Assert.assertTrue(!rs.isBeforeFirst());

        // second way
        int rowsDeleted = DB.executeUpdate("DELETE FROM ACCOUNTS WHERE ID = ?", AccId);
        assertThat("Row should already be deleted by API",
                rowsDeleted, equalTo(0)); // expect 0 since API already removed it
    }

    @Test
    public void readAllUsers() {
        Response res = AccEP.getAllAccounts();
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 200);

        // checking against schema
        res.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaAccountAll.json"));
    }

    @Test
    public void readAccountByID() {
        Response res = AccEP.getAccByAccID(AccId);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 200);

        // checking against schema
        res.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaAccount.json"));
    }

    @Test
    public void readAccountByAccNumber() {

        Response res = AccEP.getAccByAccNum("-945487953785");
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 200);

        // checking against schema
        res.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaAccount.json"));
    }

    @Test
    public void readAllAccountsForUserID() {

        Response res = AccEP.getAccByUserID(5);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 200);

        // checking against schema
        res.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaAccountAll.json"));
    }

    @Test
    public void updateUser() throws SQLException {
        
        // get result set before update
        ResultSet rsBefore = DB.executeQuery("SELECT * FROM ACCOUNTS WHERE ID = ?", 6);
        accPayload.setAccountType(AccountType.CHECKING);
        accPayload.setStatus(AccStatus.FROZEN);

        Response res = AccEP.updateAcc(accPayload, 6);
        res.then()
                .body("accountType", equalTo(accPayload.getAccountType().toValue()))
                .body("status", equalTo(accPayload.getStatus().toValue()))
                .time(lessThan(3000L));
        Assert.assertEquals(res.getStatusCode(), 200);

        // get results for same user after update
        ResultSet rsAfter = DB.executeQuery("SELECT *FROM ACCOUNTS WHERE ID = ?", 6);

        // if even one field changed (updated) between them then returns false
        // Assert.assertFalse(DB.compareResultSets(rsBefore, rsAfter));
    }

}
