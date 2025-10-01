package api_tests;

import api_endpoints.UserEP;
import api_payload.User;
import api_utils.DB;
import com.github.javafaker.Faker;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UsersTestNegative {
    Faker faker;
    User userPayload;
    int userId;

    // @BeforeMethod
    // public void setup(ITestContext context) {
    // faker = new Faker();
    // userPayload = new User();
    // // userPayload.setId(faker.idNumber().hashCode());
    // userPayload.setUsername(faker.name().username());
    // userPayload.setEmail(faker.internet().safeEmailAddress());
    // userPayload.setPassword(faker.internet().password());
    // userPayload.setFullName(faker.name().fullName());
    // userPayload.setPhoneNumber("+1234567890");
    // }

    //create user with same username
    @Test
    public void createUserWithsameUSerName(){
        faker = new Faker();
        userPayload = new User();
        userPayload.setUsername("Shady");
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().safeEmailAddress());
        userPayload.setFullName(faker.name().fullName());

        Response res = UserEP.createUser(userPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 409);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Username already exists"));
    }

    //create user with same email
    @Test
    public void createUserWithsameEmail(){
        faker = new Faker();
        userPayload = new User();
        userPayload.setUsername(faker.name().username());
        userPayload.setEmail("shady@example.com");
        userPayload.setPassword(faker.internet().safeEmailAddress());
        userPayload.setFullName(faker.name().fullName());

        Response res = UserEP.createUser(userPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 409);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Email already exists"));
    }

    // read user with invalid user ID
    @Test
    public void readUser() {
        // No user Id = 0
        Response res = UserEP.getUserByID(0);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("User not found with id: " + 0));
    }

    // read user with invalid user ID (BUG Not Working)
    @Test
    public void readUserByName() {
        // No user Id = 0
        Response res = UserEP.getUserByName("Carline Walter");
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 500);
    }

    // update user with invalid user ID
    @Test
    public void updateUserwithInvalidID() {
        faker = new Faker();
        userPayload = new User();
        userPayload.setUsername(faker.name().username());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().password());
        userPayload.setFullName(faker.name().fullName());
        userPayload.setPhoneNumber("+1234567890");

        Response res = UserEP.updateUser(userPayload, 0);
        System.out.println(res.asPrettyString());
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("User not found with id: " + 0));
    }

    // update user without username
    @Test
    public void updateUserWithoutUserName() {
        faker = new Faker();
        userPayload = new User();
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().password());
        userPayload.setFullName(faker.name().fullName());

        Response res = UserEP.updateUser(userPayload, 3);
        System.out.println(res.asPrettyString());
        String msg = res.path("username");
        Assert.assertTrue(msg.contains("Username is required"));
    }

    // update user without email
    @Test
    public void updateUserWithoutEmail() {
        faker = new Faker();
        userPayload = new User();
        userPayload.setUsername(faker.name().username());
        userPayload.setPassword(faker.internet().password());
        userPayload.setFullName(faker.name().fullName());

        Response res = UserEP.updateUser(userPayload, 3);
        System.out.println(res.asPrettyString());
        String msg = res.path("email");
        Assert.assertTrue(msg.contains("Email is required"));
    }

    // update user without Password
    @Test
    public void updateUserWithoutPassword() {
        faker = new Faker();
        userPayload = new User();
        userPayload.setUsername(faker.name().username());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setFullName(faker.name().fullName());

        Response res = UserEP.updateUser(userPayload, 3);
        System.out.println(res.asPrettyString());
        String msg = res.path("password");
        Assert.assertTrue(msg.contains("Password is required"));
    }

    // update user without FullName
    @Test
    public void updateUserWithoutFullName() {
        faker = new Faker();
        userPayload = new User();
        userPayload.setUsername(faker.name().username());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().password());

        Response res = UserEP.updateUser(userPayload, 3);
        System.out.println(res.asPrettyString());
        String msg = res.path("fullName");
        Assert.assertTrue(msg.contains("Full name is required"));
    }

    // update user without data at all
    @Test
    public void updateUserWithoutData() {
        userPayload = new User();
        Response res = UserEP.updateUser(userPayload, 3);
        System.out.println(res.asPrettyString());
        String msg1 = res.path("password");
        Assert.assertTrue(msg1.contains("Password is required"));
        String msg2 = res.path("fullName");
        Assert.assertTrue(msg2.contains("Full name is required"));
    }

    // delete user with invalid id
    @Test
    public void deleteUser() {
        // No user with id=0
        Response res = UserEP.deleteUser(0);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("User not found with id: " + 0));
    }

}
