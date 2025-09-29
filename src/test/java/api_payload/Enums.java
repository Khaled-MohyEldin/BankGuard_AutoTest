package api_payload;

public class Enums {

    public interface ValuableEnum {
        default String toValue() {
            return ((Enum<?>) this).name();
        }
    }

    public enum AccountType implements ValuableEnum {
        SAVINGS, CHECKING, CREDIT;
    }

    public enum AccStatus implements ValuableEnum {
        ACTIVE, INACTIVE, FROZEN, CLOSED;
    }

    public enum TransType implements ValuableEnum {
        DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT;
    }

    public enum TransStatus implements ValuableEnum {
        PENDING, COMPLETED, FAILED, CANCELLED;
    }

}
