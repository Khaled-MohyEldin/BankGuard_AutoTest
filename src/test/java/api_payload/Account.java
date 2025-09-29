package api_payload;

import api_payload.Enums.AccStatus;
import api_payload.Enums.AccountType;
import lombok.Data;

@Data
public class Account {
    int         id;
    int         userId;
    String      accountNumber;
    User        user;
    AccountType accountType;
    double      balance;
    double      creditLimit;
    AccStatus   status;
    String      createdAt;
    String      updatedAt;
}

// Example usage
// AccountType accountType = AccountType.ACTIVE;

/*
 * id": 1,
 * "accountNumber": "-945487953785",
 * "user": {
 * "id": 1,
 * "username": "normand.ullrich",
 * "email": "eileen.williamson@example.com",
 * "fullName": "Carline Walter",
 * "phoneNumber": "+1234567890",
 * "createdAt": "2025-09-24T23:02:23.736328",
 * "updatedAt": "2025-09-28T13:33:30.768532"
 * },
 * "accountType": "SAVINGS",
 * "balance": 25196.00,
 * "creditLimit": 0.00,
 * "status": "ACTIVE",
 * "createdAt": "2025-09-24T23:02:27.834923",
 * "updatedAt": "2025-09-24T23:02:42.687518"
 */