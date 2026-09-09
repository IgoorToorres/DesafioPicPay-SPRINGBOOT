package com.desafioPicPay.domain.transaction;

import com.desafioPicPay.domain.user.User;
import com.desafioPicPay.error.DomainException;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Getter
@Entity
@Table(
        name = "wallet_transactions",
        indexes = {
                @Index(
                        name = "idx_wallet_transaction_source",
                        columnList = "source_user_id"
                ),
                @Index(
                        name = "idx_wallet_transaction_destination",
                        columnList = "destination_user_id"
                ),
                @Index(
                        name = "idx_wallet_transaction_created_at",
                        columnList = "created_at"
                )
        }
)
public class WalletTransaction {

    @Id
    @Column(name = "id", nullable = false,updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_user_id", updatable = false)
    private User source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_user_id", updatable = false)
    private User destination;

    @Column(
            name = "amount",
            nullable = false,
            updatable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "type",
            nullable = false,
            updatable = false,
            length = 20
    )
    private TransactionType type;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected WalletTransaction(){}

    private WalletTransaction(User source, User destination, BigDecimal amount, TransactionType type){
        validateAmount(amount);
        validateParticipants(source, destination, type);

        this.id = UUID.randomUUID();
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.type = type;
        this.createdAt = Instant.now();
    }

    public static WalletTransaction deposit(User destination, BigDecimal amount){
        return new WalletTransaction(null, destination, amount, TransactionType.DEPOSIT);
    }

    public static WalletTransaction withdrawal(User source, BigDecimal amount){
        return new WalletTransaction(source, null, amount, TransactionType.WITHDRAWAL);
    }

    public static WalletTransaction transfer(User source, User destination, BigDecimal amount){
        return new WalletTransaction(source, destination, amount, TransactionType.TRANSFER);
    }

    private void validateAmount(BigDecimal amount){
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new DomainException("O valor da transação deve ser maior que zero");
        }
    }

    private void validateParticipants(User source, User destination, TransactionType type){
        if(type == null){
            throw new DomainException("O tipo de transação é obrigatório");
        }
        switch(type){
            case DEPOSIT -> validateDeposit(destination);
            case WITHDRAWAL -> validateWithdrawal(source);
            case TRANSFER -> validateTransfer(source, destination);
        }
    }

    private void validateDeposit(User destination){
        if(destination == null){
            throw new DomainException("O destinatário é obrigatório");
        }
    }

    private void validateWithdrawal(User source){
        if(source == null){
            throw new DomainException("A origem do saque é obrigatória");
        }
    }

    private void validateTransfer(User source, User destination){
        if(source == null || destination == null){
            throw new DomainException("Origem e destinatario são obrigatórios");
        }

        if(Objects.equals(source.getId(), destination.getId())){
            throw new DomainException("Origem e destinatario devem ser diferentes");
        }
    }
}
