package api_payload;


import api_payload.Enums.TransType;
import api_payload.Enums.TransStatus;
import lombok.Data;

@Data
public class Transaction {
    int         id;
    String      transactionReference;
    int         fromAccount;
    int         fromAccountId;
    int         toAccount;
    int         toAccountId;
    double      amount;
    TransType   transactionType;
    TransStatus status;
    String      description;
    double      fee;
    String      createdAt;
    String      updatedAt;
}



/*
{
    "id": 4,
    "transactionReference": "TXN-54F6A7B1",
    "fromAccount": {
        "id": 2,
        "accountNumber": "-795960695931",
        "accountType": "CHECKING",
        "balance": 25700.00,
        "creditLimit": 0.00,
        "status": "ACTIVE",
        "createdAt": "2025-09-24T23:02:33.881069",
        "updatedAt": "2025-09-25T14:05:08.074866"
    },
    "amount": 500.00,
    "transactionType": "DEPOSIT",
    "status": "COMPLETED",
    "description": "Salary deposit",
    "fee": 0.00,
    "createdAt": "2025-09-25T14:05:08.047854",
    "processedAt": "2025-09-25T14:05:08.03385"
}
 */