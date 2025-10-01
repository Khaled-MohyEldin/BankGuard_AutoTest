package api_tests;

import api_endpoints.TransEP;
import api_endpoints.Routes;
import api_payload.Enums.TransType;
import api_payload.Transaction;
import api_utils.DB;
import com.github.javafaker.Faker;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

@Test(groups = {"ValidTransCreation"})
public class TransactionsCreationTest {
    Transaction transPayload;
    // int userId, transId;
    double precisionDelta = 0.001; // Allows a difference up to $0.001

    @BeforeMethod
    public void setup() {
        transPayload = new Transaction();
        transPayload.setFromAccountId(10);
        transPayload.setToAccountId(3);
        transPayload.setAmount( 98.50);
        transPayload.setFee( 1.50);
   }


    //there are 4 types of tranactions (DEPOSIT, WITHDRAWAL, PAYMENT, Transfer)
    @Test
    public void createWithdrawTransction() throws SQLException {
        // transPayload.setFromAccountId(2);
        transPayload.setTransactionType(TransType.WITHDRAWAL);
        transPayload.setDescription("ATM withdrawal");

        //1- we get Balance from account with account ID
        ResultSet rs = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        rs.next(); 
        double balanceBefore = rs.getDouble("balance");
        
        //execute tranastion 
        Response res = TransEP.createTransaction(transPayload);
        System.out.println(res.asPrettyString());
         Assert.assertEquals(res.getStatusCode(), 201);
        
        //3- get balance After and compare [after = before -amount -fee ]
        ResultSet rs2 = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?",transPayload.getFromAccountId());
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
        
        transPayload.setFromAccountId(10);
        transPayload.setTransactionType(TransType.DEPOSIT);
        transPayload.setDescription("Whatever Deposite");
        System.out.println(transPayload.toString());

        //1- we get Balance from account with account ID
        ResultSet rs = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        rs.next(); 
        double balanceBefore = rs.getDouble("balance");
        
        //execute tranastion 
        Response res = TransEP.createTransaction(transPayload);
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
    public void createPaymentTransction() throws SQLException {

        transPayload.setFromAccountId(3);
        transPayload.setTransactionType(TransType.PAYMENT);
        transPayload.setDescription("paying for a service");

        //1- we get Balance from account with account ID
        ResultSet rs = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        rs.next(); 
        double balanceBefore = rs.getDouble("balance");
        
        //2- execute tranastion 
         Response res = TransEP.createTransaction(transPayload);
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
    public void createTransferTransction() throws SQLException {
        transPayload.setFromAccountId(4);
        transPayload.setToAccountId(3);
        transPayload.setTransactionType(TransType.TRANSFER);
        transPayload.setDescription("Transfer between accounts");
        

        //1- we get Balance from Two accounts (from - to) with account ID
        ResultSet rs1 = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getFromAccountId());
        ResultSet rs2 = DB.executeQuery("SELECT * FROM accounts WHERE ID = ?", transPayload.getToAccountId());
        rs1.next(); rs2.next();
        double balanceFromBefore = rs1.getDouble("balance");
        double balanceToBefore = rs2.getDouble("balance");
        
        //2- execute tranastion 
        Response res = TransEP.createTransaction(transPayload);
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

        Assert.assertEquals(balance3, balanceFromAfter, precisionDelta, 
        "The calculated account balance does not match the database value.");
        Assert.assertEquals(balance4, balanceToAfter, precisionDelta, 
        "The calculated account balance does not match the database value.");
    }


}
