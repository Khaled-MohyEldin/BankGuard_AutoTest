package api_tests;

import api_endpoints.AccEP;
import api_payload.Account;
import api_payload.Enums.AccStatus;
import api_payload.Enums.AccountType;
import api_utils.DB;
import com.github.javafaker.Faker;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class AccountsTestNegative {
    Faker faker;
    Account accPayload;
    int AccId, userId;
    String accNum;

    // @BeforeClass
    // public void setup(ITestContext context) {
    //     faker = new Faker();
    //     // getting UserId
    //     Object userIdObj = context.getSuite().getAttribute("userId");
    //     userId = userIdObj != null ? (int) userIdObj : -1; // or any default
        
        // accPayload = new Account();
        // // accPayload.setId(faker.idNumber().hashCode());
        // accPayload.setUserId(5);
        // accPayload.setBalance(faker.number().randomDouble(2, 0, 1000000));
        // accPayload.setCreditLimit(faker.number().randomDouble(2, 0, 10000));
        // accPayload.setAccountType(AccountType.CREDIT);
        // accPayload.setStatus(AccStatus.ACTIVE);

    // }


    //1- get account with invalid Account number
    @Test
    public void readAccountByAccNumber() {
        //no account with number = "0"
        Response res = AccEP.getAccByAccNum("0");
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Account not found with number: 0"));
    }

    //2- get account with invalid Account number
    @Test
    public void readAccountByAccID() {
        //no account with id = 0
        Response res = AccEP.getAccByAccID(0);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Account not found with id: "+0));
    }

    //3- get account with invalid User ID
    @Test
    public void readAccountByUserID() {
        //no account with Userid = 0
        Response res = AccEP.getAccByUserID(0);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("User not found with id: "+0));
    }

    //4- create account without Accountype (empty/null)
    @Test
    public void createAccount() throws SQLException {
        accPayload = new Account();
        accPayload.setUserId(3);
        accPayload.setBalance(	208201.57);
        accPayload.setCreditLimit(3800.00);
        accPayload.setStatus(AccStatus.ACTIVE);

        Response res = AccEP.createAccount(accPayload);
        System.out.println(res.asPrettyString());
        String msg = res.path("accountType");
        Assert.assertTrue(msg.contains("Account type is required"));
    }

    //5- update account with Same data as before 
    @Test
    public void updateAccount() throws SQLException {
        accPayload = new Account();
        accPayload.setUserId(5);
        accPayload.setBalance(	208201.57);
        accPayload.setCreditLimit(3800.00);
        accPayload.setAccountType(AccountType.CHECKING);
        accPayload.setStatus(AccStatus.FROZEN);

        Response res = AccEP.updateAcc(accPayload, 6);
        System.out.println(res.asPrettyString());
        res.then()
                .body("accountType", equalTo(accPayload.getAccountType().toValue()))
                .body("status", equalTo(accPayload.getStatus().toValue()))
                .time(lessThan(3000L));
        Assert.assertEquals(res.getStatusCode(), 200);
    }

    //6- update account with invalid account ID 
    @Test
    public void updateAccountwithInvalidID() throws SQLException {
        
        accPayload = new Account();
        accPayload.setBalance(	208201.57);
        accPayload.setCreditLimit(3800.00);
        accPayload.setAccountType(AccountType.CHECKING);
        accPayload.setStatus(AccStatus.INACTIVE);

        Response res = AccEP.updateAcc(accPayload, 0);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Account not found with id: "+0));
    }
    
    //7- update account with null/empty Account TYpe
    @Test
    public void updateAccountwithEmptyType() throws SQLException {
        
        accPayload = new Account();
        accPayload.setBalance(	300000.00);
        accPayload.setCreditLimit(4800.00);
        accPayload.setStatus(AccStatus.ACTIVE);

        Response res = AccEP.updateAcc(accPayload, 10);
        System.out.println(res.asPrettyString());
        String msg = res.path("accountType");
        Assert.assertTrue(msg.contains("Account type is required"));
    }

    //8- delete account with invalid account id 
    @Test
    public void deleteAccount() throws SQLException {
        //No Account with id=0 
        Response res = AccEP.deleteAcc(0);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Account not found with id: "+0));
    }
    
    /* */
}
