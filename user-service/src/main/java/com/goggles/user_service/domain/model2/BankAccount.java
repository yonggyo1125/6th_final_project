package com.goggles.user_service.domain.model2;

import com.goggles.user_service.domain.exception.InvalidBankAccountException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankAccount {

    @Column(name = "bank_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private Bank bankName;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "account_holder", nullable = false)
    private String accountHolder;

    private BankAccount(Bank bankName, String accountNumber, String accountHolder){
        validate(bankName, accountNumber, accountHolder);
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
    }

    public static BankAccount of(Bank bankName, String accountNumber, String accountHolder){
        return new BankAccount(bankName, accountNumber, accountHolder);
    }

    private void validate(Bank bankName, String accountNumber, String accountHolder){
        if(bankName == null){
            throw new InvalidBankAccountException("bankName");
        }

        if (accountNumber == null || !accountNumber.matches("^[0-9-]{10,20}$")) {
            throw new InvalidBankAccountException("accountNumber");
        }

        String digitsOnly = accountNumber.replace("-", "");

        if (!digitsOnly.matches("^\\d{10,20}$")) {
            throw new InvalidBankAccountException(accountNumber);
        }
        if (accountHolder == null || !accountHolder.matches("^[a-zA-Z가-힣]+$")) {
            throw new InvalidBankAccountException(accountHolder);
        }
    }
}
