package api_tests;

import api_endpoints.Routes;
import api_endpoints.TransEP;
import api_payload.Enums.TransStatus;
import api_payload.Enums.TransType;
import api_payload.User;
import api_utils.DB;
import api_payload.Transaction;
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
import java.sql.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class TranactionTestInvalid {
    Faker faker;
    Transaction  transPayload;
    int userId, transId;
    String msg; 
    /* 
     {
  "fromAccountId": 1,
  "amount": 100.00,
  "transactionType": "WITHDRAWAL",
  "description": "ATM withdrawal",
  "fee": 1.50
}           
    code 400 , message: "Insufficient funds in account"
    */
    @BeforeClass
    public void setup(ITestContext context) {
        faker = new Faker();
        transPayload = new Transaction();
        
        transPayload.setFromAccountId(0);
        transPayload.setAmount( 100.00);
        transPayload.setTransactionType(TransType.TRANSFER);
        transPayload.setDescription("ATM withdrawal");
        transPayload.setFee( 1.50);
    }

    //1- create Tranaction without invalid AccountID
    @Test
    public void createTransWithoutTrandID() throws SQLException {

        Response res = TransEP.createTranaction(transPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);

        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Account not found with id"));
       
    }

    //2-Account has insufficient funds or  zero 
    @Test
    public void createWithdrawTranactionWithFunds() throws SQLException {
        //adding this part of payload 
        transPayload.setFromAccountId(1);
        Response res = TransEP.createTranaction(transPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 400);

        String msg = res.path("message");
        Assert.assertTrue(msg.equals("Insufficient funds in account"));
       
    }

}
