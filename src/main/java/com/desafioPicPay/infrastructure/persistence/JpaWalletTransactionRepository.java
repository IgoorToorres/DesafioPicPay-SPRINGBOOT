package com.desafioPicPay.infrastructure.persistence;

import com.desafioPicPay.domain.transaction.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaWalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
}
