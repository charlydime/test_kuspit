package com.sistema.tareas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Petición de inicio de sesión")
public class LoginRequest {

    @NotBlank(message = "El username es obligatorio")
    @Schema(description = "Nombre de usuario", example = "juanperez")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    @Schema(description = "Contraseña del usuario", example = "miPassword123")
    private String password;
}
