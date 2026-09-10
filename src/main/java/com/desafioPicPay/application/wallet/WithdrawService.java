package com.desafioPicPay.application.wallet;

import com.desafioPicPay.application.wallet.command.WithdrawCommand;
import com.desafioPicPay.application.wallet.response.WithdrawResponse;
import com.desafioPicPay.domain.repository.UserRepository;
import com.desafioPicPay.domain.repository.WalletTransactionRepository;
import com.desafioPicPay.domain.transaction.WalletTransaction;
import com.desafioPicPay.domain.user.User;
import com.desafioPicPay.error.DomainException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class WithdrawService {

    private WalletTransactionRepository walletTransactionRepository;
    private UserRepository userRepository;

    public WithdrawService(
            WalletTransactionRepository walletTransactionRepository,
            UserRepository userRepository
    ){
        this.walletTransactionRepository = walletTransactionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WithdrawResponse execute(WithdrawCommand command){
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new DomainException("usuario nao encontrado"));

        user.debit(command.amount());
        WalletTransaction transaction = WalletTransaction.withdrawal(user, command.amount());

        userRepository.save(user);
        walletTransactionRepository.save(transaction);

        return new WithdrawResponse(
          transaction.getId(),
          user.getId(),
          transaction.getAmount(),
          user.getBalance(),
          transaction.getCreatedAt()
        );
    }
}
