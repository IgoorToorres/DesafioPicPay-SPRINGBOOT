package com.desafioPicPay.infrastructure.persistence;

import com.desafioPicPay.domain.repository.WalletTransactionRepository;
import com.desafioPicPay.domain.transaction.WalletTransaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class WalletTransactionRepositoryImpl implements WalletTransactionRepository {
    private JpaWalletTransactionRepository jpaWalletTransactionRepository;

    public WalletTransactionRepositoryImpl(JpaWalletTransactionRepository jpaWalletTransactionRepository){
        this.jpaWalletTransactionRepository = jpaWalletTransactionRepository;
    }

    @Override
    public WalletTransaction save(WalletTransaction transaction) {
        return jpaWalletTransactionRepository.save(transaction);
    }

    @Override
    public List<WalletTransaction> findAllByUserId(UUID userId) {
        return jpaWalletTransactionRepository.findAll();
    }
}
