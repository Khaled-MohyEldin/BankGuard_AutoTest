package api_tests;

import api_endpoints.TransEP;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TransactionsReadingTest {

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
