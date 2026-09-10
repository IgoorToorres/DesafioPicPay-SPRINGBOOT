package com.desafioPicPay.application.wallet;

import com.desafioPicPay.application.wallet.command.DepositCommand;
import com.desafioPicPay.application.wallet.response.DepositResponse;
import com.desafioPicPay.domain.repository.UserRepository;
import com.desafioPicPay.domain.repository.WalletTransactionRepository;
import com.desafioPicPay.domain.transaction.TransactionType;
import com.desafioPicPay.domain.transaction.WalletTransaction;
import com.desafioPicPay.domain.user.User;
import com.desafioPicPay.domain.user.UserRole;
import com.desafioPicPay.error.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {

    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    @Mock
    private UserRepository userRepository;

    private DepositService depositService;

    @BeforeEach
    void setUp() {
        depositService = new DepositService(walletTransactionRepository, userRepository);
    }

    @Test
    void shouldDepositAndRegisterTransaction() {
        User user = createUser(new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("50.00");
        DepositCommand command = new DepositCommand(user.getId(), amount);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        DepositResponse response = depositService.execute(command);

        assertEquals(new BigDecimal("150.00"), user.getBalance());
        assertEquals(user.getId(), response.userId());
        assertEquals(amount, response.amount());
        assertEquals(new BigDecimal("150.00"), response.currentBalance());
        assertNotNull(response.transactionId());
        assertNotNull(response.createdAt());

        verify(userRepository).save(user);

        ArgumentCaptor<WalletTransaction> captor =
                ArgumentCaptor.forClass(WalletTransaction.class);
        verify(walletTransactionRepository).save(captor.capture());

        WalletTransaction transaction = captor.getValue();
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertNull(transaction.getSource());
        assertSame(user, transaction.getDestination());
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        DepositCommand command = new DepositCommand(userId, new BigDecimal("50.00"));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> depositService.execute(command)
        );

        assertEquals("Usuario não encontrado", exception.getMessage());
        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(walletTransactionRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldNotPersistWhenAmountIsNotPositive() {
        User user = createUser(new BigDecimal("100.00"));
        DepositCommand command = new DepositCommand(user.getId(), BigDecimal.ZERO);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> depositService.execute(command)
        );

        assertEquals("O valor deve ser maior que zero", exception.getMessage());
        assertEquals(new BigDecimal("100.00"), user.getBalance());
        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(walletTransactionRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    private User createUser(BigDecimal balance) {
        return new User(
                "Maria",
                "Silva",
                "12345678901",
                "maria@email.com",
                "password-hash",
                balance,
                UserRole.COMMON
        );
    }
}
