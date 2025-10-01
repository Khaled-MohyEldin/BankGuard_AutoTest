package api_payload;

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
    Enums.TransType transactionType;
    Enums.TransStatus status;
    String      description;
    double      fee;
    String      createdAt;
    String      updatedAt;
}
