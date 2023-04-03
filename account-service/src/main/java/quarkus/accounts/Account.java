package quarkus.accounts;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Account extends PanacheEntity {

    public Long accountNumber;

    public Long customerNumber;

    public String customerName;

    public BigDecimal balance;

    public AccountStatus accountStatus = AccountStatus.OPEN;

    public static Account findByAccountNumber(Long accountNumber) {
        return find("accountNumber", accountNumber).firstResult();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
