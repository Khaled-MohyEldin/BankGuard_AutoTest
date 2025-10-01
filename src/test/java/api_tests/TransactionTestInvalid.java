package api_tests;

import api_endpoints.TransEP;
import api_payload.Enums.TransType;
import api_payload.Transaction;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class TransactionTestInvalid {
    Faker faker;
    Transaction  transPayload;
    int userId, transId;
    String msg; 

     @BeforeMethod
     public void setup() {
         faker = new Faker();
         transPayload = new Transaction();
         transPayload.setAmount( 100.00);
         transPayload.setTransactionType(TransType.TRANSFER);
         transPayload.setDescription("ATM withdrawal");
         transPayload.setFee(1.50);
     }


    //1- create Tranaction with invalid AccountID
    @Test
    public void createTransWithoutTrandID() throws SQLException {

        transPayload.setFromAccountId(100);
//        transPayload.setToAccountId(2);
        Response res = TransEP.createTransaction(transPayload);
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
        transPayload.setToAccountId(2);
        Response res = TransEP.createTransaction(transPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 400);

        String msg = res.path("message");
        Assert.assertTrue(msg.equals("Insufficient funds in account"));
       
    }

}
