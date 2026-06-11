package com.sistema.tareas.dto;

import com.sistema.tareas.model.enums.EstadoTarea;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa una tarea")
public class TareaDTO {

    @Schema(description = "ID de la tarea (asignado por el sistema)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario propietario (se asigna automáticamente del usuario autenticado)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long usuarioId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no debe exceder 100 caracteres")
    @Schema(description = "Título de la tarea", example = "Comprar víveres", maxLength = 100)
    private String titulo;

    @Schema(description = "Descripción detallada de la tarea", example = "Ir al supermercado y comprar frutas, verduras y pan")
    private String descripcion;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Schema(description = "Fecha y hora de vencimiento", example = "2026-06-10T18:00:00")
    private LocalDateTime fechaVencimiento;

    @Schema(description = "Estado actual de la tarea (PENDIENTE, COMPLETA, VENCIDA)", example = "PENDIENTE", accessMode = Schema.AccessMode.READ_ONLY)
    private EstadoTarea estado;
}
