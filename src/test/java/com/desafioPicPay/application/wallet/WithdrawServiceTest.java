package com.desafioPicPay.application.wallet;

import com.desafioPicPay.application.wallet.command.WithdrawCommand;
import com.desafioPicPay.application.wallet.response.WithdrawResponse;
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
class WithdrawServiceTest {

    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    @Mock
    private UserRepository userRepository;

    private WithdrawService withdrawService;

    @BeforeEach
    void setUp() {
        withdrawService = new WithdrawService(walletTransactionRepository, userRepository);
    }

    @Test
    void shouldWithdrawAndRegisterTransaction() {
        User user = createUser(new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("40.00");
        WithdrawCommand command = new WithdrawCommand(user.getId(), amount);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        WithdrawResponse response = withdrawService.execute(command);

        assertEquals(new BigDecimal("60.00"), user.getBalance());
        assertEquals(user.getId(), response.userId());
        assertEquals(amount, response.amount());
        assertEquals(new BigDecimal("60.00"), response.currentBalance());
        assertNotNull(response.transactionId());
        assertNotNull(response.createdAt());

        verify(userRepository).save(user);

        ArgumentCaptor<WalletTransaction> captor =
                ArgumentCaptor.forClass(WalletTransaction.class);
        verify(walletTransactionRepository).save(captor.capture());

        WalletTransaction transaction = captor.getValue();
        assertEquals(TransactionType.WITHDRAWAL, transaction.getType());
        assertSame(user, transaction.getSource());
        assertNull(transaction.getDestination());
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        WithdrawCommand command = new WithdrawCommand(userId, new BigDecimal("40.00"));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> withdrawService.execute(command)
        );

        assertEquals("usuario nao encontrado", exception.getMessage());
        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(walletTransactionRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldNotPersistWhenBalanceIsInsufficient() {
        User user = createUser(new BigDecimal("30.00"));
        WithdrawCommand command =
                new WithdrawCommand(user.getId(), new BigDecimal("40.00"));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> withdrawService.execute(command)
        );

        assertEquals("saldo insuficiente", exception.getMessage());
        assertEquals(new BigDecimal("30.00"), user.getBalance());
        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(walletTransactionRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldNotPersistWhenAmountIsNotPositive() {
        User user = createUser(new BigDecimal("100.00"));
        WithdrawCommand command = new WithdrawCommand(user.getId(), BigDecimal.ZERO);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> withdrawService.execute(command)
        );

        assertEquals("O valor deve ser maior que zero", exception.getMessage());
        assertEquals(new BigDecimal("100.00"), user.getBalance());
        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(walletTransactionRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    private User createUser(BigDecimal balance) {
        return new User(
                "João",
                "Souza",
                "98765432100",
                "joao@email.com",
                "password-hash",
                balance,
                UserRole.COMMON
        );
    }
}
