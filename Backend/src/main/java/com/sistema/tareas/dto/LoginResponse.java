package com.sistema.tareas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta de inicio de sesión o registro")
public class LoginResponse {

    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Nombre de usuario", example = "juanperez")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "juan@example.com")
    private String email;
}
