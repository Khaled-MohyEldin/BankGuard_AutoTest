package api_tests;

import api_endpoints.Routes;
import api_endpoints.UserEP;
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

public class UsersTest {
    Faker faker;
    User userPayload;
    int userId;

    @BeforeTest
    public void setup(ITestContext context) {
        faker = new Faker();
        userPayload = new User();
        // userPayload.setId(faker.idNumber().hashCode());
        userPayload.setUsername(faker.name().username());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().password());
        userPayload.setFullName(faker.name().fullName());
        userPayload.setPhoneNumber("+1234567890");
    }

    @Test
    public void createUser(ITestContext context) throws SQLException {
        Response res1 = UserEP.createUser(userPayload);
        userId = res1.path("id");
        context.getSuite().setAttribute("userId", userId);
        Assert.assertEquals(res1.getStatusCode(), 201);

        // Validate DB data
        ResultSet rs = DB.executeQuery("SELECT * FROM USERS WHERE ID = ?", userId);
        if (rs.next()) {
            String dbEmail = rs.getString("email");
            assertThat(dbEmail, is(userPayload.getEmail()));
        } else {
            throw new AssertionError("User not found in DB!");
        }
        rs.close();
    }

    @Test
    public void readAllUsers(){
        Response res = UserEP.getAllUsers(); 
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 200);

        //checking against schema 
        res.then().assertThat() 
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaUserAll.json"));
    }

    @Test
    public void readUser() {
        Response res = UserEP.getUserByID(userId);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 200);

        //checking against schema
        res.then().assertThat()
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaUser.json"));
    }

    @Test
    public void updateUser() throws SQLException {
        // get result set before update
        ResultSet rsBefore = DB.executeQuery("SELECT * FROM USERS WHERE ID = ?", userId);

        Response res = UserEP.updateUser(userPayload, userId);
        res.then()
                .body("username", equalTo(userPayload.getUsername()))
                .time(lessThan(3000L));
        Assert.assertEquals(res.getStatusCode(), 200);

        // get results for same user after update
        ResultSet rsAfter = DB.executeQuery("SELECT * FROM USERS WHERE ID = ?", userId);

        // if even one field changed (updated) between them then returns false
        Assert.assertFalse(DB.compareResultSets(rsBefore, rsAfter));
    }

    @Test
    public void deleteUser() throws SQLException {
        // API side
        Response res = UserEP.deleteUser(userId);
        Assert.assertEquals(res.getStatusCode(), 204);

        // DB Side
        ResultSet rs = DB.executeQuery("SELECT * FROM USERS WHERE ID = ?", userId);
        if (!rs.isBeforeFirst())
            System.out.println("results are empty");
        // asserting that results is empty (no record of that user)
        Assert.assertTrue(!rs.isBeforeFirst());

        // second way 
        int rowsDeleted = DB.executeUpdate("DELETE FROM USERS WHERE ID = ?", userId);
        assertThat("Row should already be deleted by API",
                rowsDeleted, equalTo(0)); // expect 0 since API already removed it
    }


}
