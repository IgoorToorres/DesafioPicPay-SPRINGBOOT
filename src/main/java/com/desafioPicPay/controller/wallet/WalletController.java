package com.desafioPicPay.controller.wallet;

import com.desafioPicPay.application.wallet.DepositService;
import com.desafioPicPay.application.wallet.WithdrawService;
import com.desafioPicPay.application.wallet.command.DepositCommand;
import com.desafioPicPay.application.wallet.command.WithdrawCommand;
import com.desafioPicPay.application.wallet.response.DepositResponse;
import com.desafioPicPay.application.wallet.response.WithdrawResponse;
import com.desafioPicPay.controller.wallet.request.DepositRequest;
import com.desafioPicPay.controller.wallet.request.WithdrawRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
@Tag(name = "Carteira", description = "Operações de depósito e saque da carteira")
public class WalletController {
    private DepositService depositService;
    private WithdrawService withdrawService;

    public WalletController(
            DepositService depositService,
            WithdrawService withdrawService
    ){
        this.depositService = depositService;
        this.withdrawService = withdrawService;
    }

    @PostMapping("/{userId}/deposits")
    @Operation(
            summary = "Realizar depósito",
            description = "Adiciona saldo à carteira do usuário e registra a movimentação."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Depósito realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = DepositResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Valor inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    public ResponseEntity<DepositResponse> deposit(
            @Parameter(description = "ID do usuário que receberá o depósito", required = true)
            @PathVariable UUID userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Valor a ser depositado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = DepositRequest.class),
                            examples = @ExampleObject(value = "{\"amount\": 100.00}")
                    )
            )
            @Valid @RequestBody DepositRequest request
        ){
        DepositCommand command = new DepositCommand(
                userId,
                request.amount()
        );

        DepositResponse response = depositService.execute(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{userId}/withdrawals")
    @Operation(
            summary = "Realizar saque",
            description = "Retira saldo da carteira do usuário e registra a movimentação."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Saque realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = WithdrawResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Valor inválido ou saldo insuficiente",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    public ResponseEntity<WithdrawResponse> withdraw(
            @Parameter(description = "ID do usuário que realizará o saque", required = true)
            @PathVariable UUID userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Valor a ser sacado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = WithdrawRequest.class),
                            examples = @ExampleObject(value = "{\"amount\": 50.00}")
                    )
            )
            @Valid @RequestBody WithdrawRequest request
        ){
        WithdrawCommand command = new WithdrawCommand(
                userId,
                request.amount()
        );

        WithdrawResponse response = withdrawService.execute(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
