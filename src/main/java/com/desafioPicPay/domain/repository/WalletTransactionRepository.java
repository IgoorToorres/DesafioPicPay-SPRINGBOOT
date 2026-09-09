package com.desafioPicPay.domain.repository;

import com.desafioPicPay.domain.transaction.WalletTransaction;

import java.util.List;
import java.util.UUID;

public interface WalletTransactionRepository {

    WalletTransaction save(WalletTransaction transaction);

    List<WalletTransaction> findAllByUserId(UUID userId);
}
