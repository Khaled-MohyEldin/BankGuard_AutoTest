package api_tests;

import api_endpoints.TransEP;
import api_payload.Enums.TransType;
import api_payload.Transaction;

import com.github.javafaker.Faker;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TransactionTestInvalid {
    Transaction transPayload;
    String msg;

    @BeforeMethod
    public void setup() {
        transPayload = new Transaction();
        transPayload.setTransactionType(TransType.TRANSFER);
        transPayload.setDescription("Transfer");
        transPayload.setAmount(98.50);
        transPayload.setFee(1.50);
    }

    // 1- create Tranaction with invalid AccountID
    @Test
    public void createTransWithoutTrandID() {
        transPayload.setFromAccountId(0);
        transPayload.setToAccountId(2);
        Response res = TransEP.createTransaction(transPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);

        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Account not found with id: " + transPayload.getFromAccountId()));
    }

    // 2- Account has insufficient funds or zero
    @Test
    public void createWithdrawTranactionWithFunds() {
        // adding this part of payload
        transPayload.setFromAccountId(1);
        transPayload.setToAccountId(2);
        Response res = TransEP.createTransaction(transPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 400);

        String msg = res.path("message");
        Assert.assertTrue(msg.equals("Insufficient funds in account"));
    }

    // 3-Transfer from and to Same account!
    // This is a bug it also deduct fee :)
    @Test
    public void createTransferTransction() {
        transPayload.setFromAccountId(4);
        transPayload.setToAccountId(4);
        transPayload.setTransactionType(TransType.TRANSFER);
        transPayload.setDescription("Transfer Same! accounts");

        Response res = TransEP.createTransaction(transPayload);
        System.out.println(res.asPrettyString());

    }

    // 4-Transfer to account that does not exit (from Account is already covered up)
    @Test
    public void createTransferTransction2() {
        transPayload.setFromAccountId(4);
        transPayload.setToAccountId(100);
        transPayload.setTransactionType(TransType.TRANSFER);
        transPayload.setDescription("Transfer to non exiting account");

        Response res = TransEP.createTransaction(transPayload);
        System.out.println(res.asPrettyString());
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Account not found with id: " + transPayload.getToAccountId()));

    }

    // 5- get Tranasction by invalid Account ID
    @Test
    public void readUsersTransactionByAccID() {
        // no account with id =0; 
        Response res = TransEP.getTransByAccID(0);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Account not found with id: "+0));
    }

    // 6- get Tranasction by invalid Tranaction Reference
    @Test
    public void readUsersTransactionByTransRef() {
        // no Tranaction with such refefrence; 
        Response res = TransEP.getTransByRef("TXN-4984C");
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 404);
        String msg = res.path("message");
        Assert.assertTrue(msg.contains("Transaction not found with reference: TXN-4984C"));
    }

}
