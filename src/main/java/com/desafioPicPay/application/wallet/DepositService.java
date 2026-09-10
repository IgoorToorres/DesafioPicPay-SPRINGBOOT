package com.desafioPicPay.application.wallet;

import com.desafioPicPay.application.wallet.command.DepositCommand;
import com.desafioPicPay.application.wallet.response.DepositResponse;
import com.desafioPicPay.domain.repository.UserRepository;
import com.desafioPicPay.domain.repository.WalletTransactionRepository;
import com.desafioPicPay.domain.transaction.WalletTransaction;
import com.desafioPicPay.domain.user.User;
import com.desafioPicPay.error.DomainException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DepositService {
    private WalletTransactionRepository walletTransactionRepository;
    private UserRepository userRepository;

    public DepositService(
            WalletTransactionRepository walletTransactionRepository,
            UserRepository userRepository
        ){
        this.walletTransactionRepository = walletTransactionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DepositResponse execute(DepositCommand command){
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new DomainException("Usuario não encontrado"));

        user.credit(command.amount());
        WalletTransaction transaction = WalletTransaction.deposit(user, command.amount());

        userRepository.save(user);
        walletTransactionRepository.save(transaction);

        return new DepositResponse(
                transaction.getId(),
                user.getId(),
                transaction.getAmount(),
                user.getBalance(),
                transaction.getCreatedAt()
        );
    }
}
