package com.desafioPicPay.controller.user;

import com.desafioPicPay.application.user.SaveUserService;
import com.desafioPicPay.application.user.command.RegisterUserCommand;
import com.desafioPicPay.application.user.response.UserResponse;
import com.desafioPicPay.controller.user.request.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Usuários", description = "Operações relacionadas a usuários e lojistas")
public class UserController {
    private final SaveUserService saveUserService;

    public UserController(
            SaveUserService saveUserService
    ){
        this.saveUserService = saveUserService;
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar usuário",
            description = "Cadastra um usuário comum ou lojista. O documento e o e-mail devem ser únicos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos, e-mail ou documento já cadastrado",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para cadastrar o usuário",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserRequest.class),
                            examples = @ExampleObject(
                                    name = "Usuário comum",
                                    value = """
                                            {
                                              "fullName": "Maria da Silva",
                                              "email": "maria@email.com",
                                              "document": "12345678901",
                                              "password": "senhaSegura123",
                                              "role": "COMMON"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody UserRequest userRequest
    ){
        RegisterUserCommand command = new RegisterUserCommand(
                userRequest.fullName(),
                userRequest.email(),
                userRequest.document(),
                userRequest.password(),
                userRequest.role()
        );

        UserResponse response =  saveUserService.saveUser(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
