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

public class TransactionsCreationTest {
    Faker faker;
    Transaction transPayload;
    int userId, transId;

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

    //there are 4 types of tranactions (DEPOSIT, WITHDRAWAL, PAYMENT, Transfer)
    //first 3 share same payload, while the 4th (Transfer) needs additional field in his body 
    @Test
    public void createWithdrawTransction(ITestContext context) throws SQLException {

        transPayload.setFromAccountId(1);
        transPayload.setAmount( 500.00);
        transPayload.setTransactionType(TransType.WITHDRAWAL);
        transPayload.setDescription("ATM withdrawal");
        transPayload.setFee( 0.0);

        //1- we get Balance from account with account ID
        ResultSet rs = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        rs.next(); 
        double balanceBefore = rs.getDouble("balance");
        
        //execute tranastion 
        Response res = TransEP.createTranaction(transPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 201);
        
        //3- get balance After and compare [after = before -amount -fee ]
        ResultSet rs2 = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        rs2.next(); 
        double balance2 = rs2.getDouble("balance");
        double balanceAfter = balanceBefore - (transPayload.getAmount() + transPayload.getFee()); 
        System.out.println(balanceBefore +" - (amount + fee) = "+ balance2);
        Assert.assertEquals(balance2, balanceAfter);
    }

    // deposite Transaction , here Transaction fee is not working 
    // also it should be "toAccountID" not "fromAccountID"
    // just add amount to current balance of that account 
    @Test
    public void createDepositeTransction(ITestContext context) throws SQLException {

        transPayload.setFromAccountId(1);
        transPayload.setAmount( 100.00);
        transPayload.setTransactionType(TransType.DEPOSIT);
        transPayload.setDescription("Whatever Deposite");
        transPayload.setFee( 0.00);

        //1- we get Balance from account with account ID
        ResultSet rs = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        rs.next(); 
        double balanceBefore = rs.getDouble("balance");
        
        //execute tranastion 
        Response res = TransEP.createTranaction(transPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 201);
        
        //3- get balance After and compare [after = before -amount -fee ]
        ResultSet rs2 = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        rs2.next(); 
        double balance2 = rs2.getDouble("balance");
        double balanceAfter = balanceBefore + transPayload.getAmount(); 
        System.out.println("Before "+ balanceBefore +" after "+ balance2);
        Assert.assertEquals(balance2, balanceAfter);
    }

    @Test
    public void createPaymentTransction(ITestContext context) throws SQLException {

        transPayload.setFromAccountId(1);
        transPayload.setAmount( 98.00);
        transPayload.setTransactionType(TransType.PAYMENT);
        transPayload.setDescription("paying for a service");
        transPayload.setFee( 1.00);

        //1- we get Balance from account with account ID
        ResultSet rs = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        rs.next(); 
        double balanceBefore = rs.getDouble("balance");
        
        //execute tranastion 
        Response res = TransEP.createTranaction(transPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 201);
        
        //3- get balance After and compare [after = before -amount -fee ]
        ResultSet rs2 = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        rs2.next(); 
        double balance2 = rs2.getDouble("balance");
        double balanceAfter = balanceBefore - transPayload.getAmount() - transPayload.getFee(); 
        System.out.println("Before ="+ balanceBefore +" ,Expected ="+ balanceAfter + " ,Actual =" + balance2);
        Assert.assertEquals(balance2, balanceAfter);
    }

    // in Transfer we add "ToAccountId" to payload 
    // then we check that two accounts balances updated correctly in DB
    @Test
    public void createTransferTransction(ITestContext context) throws SQLException {

        transPayload.setFromAccountId(1);
        transPayload.setToAccountId(2);
        transPayload.setAmount( 99.00);
        transPayload.setTransactionType(TransType.TRANSFER);
        transPayload.setDescription("Transfer between accounts");
        transPayload.setFee( 1.00);

        //1- we get Balance from Two accounts (from - to) with account ID
        ResultSet rs1 = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        ResultSet rs2 = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getToAccountId());
        rs1.next(); rs2.next();
        double balanceFromBefore = rs1.getDouble("balance");
        double balanceToBefore = rs2.getDouble("balance");
        
        //execute tranastion 
        Response res = TransEP.createTranaction(transPayload);
        System.out.println(res.asPrettyString());
        Assert.assertEquals(res.getStatusCode(), 201);
        
        //3- get balance After and compare [after = before -amount -fee ]
        ResultSet rs3 = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        ResultSet rs4 = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getToAccountId());
        rs3.next(); rs4.next(); 
        double balance3 = rs3.getDouble("balance");
        double balance4 = rs4.getDouble("balance");
        double balanceFromAfter = balanceFromBefore - transPayload.getAmount() - transPayload.getFee(); 
        double balanceToAfter = balanceToBefore + transPayload.getAmount();
        System.out.println("FromAccount Before ="+ balanceFromBefore +" ,Expected ="+ balanceFromAfter + " ,Actual =" + balance3);
        System.out.println("ToAccount Before ="+ balanceToBefore +" ,Expected ="+ balanceToAfter + " ,Actual =" + balance4);
        Assert.assertEquals(balance3, balanceFromAfter);
        Assert.assertEquals(balance4, balanceToAfter);
    }


}
