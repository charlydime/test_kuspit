package com.sistema.tareas.controller;

import com.sistema.tareas.dto.TareaDTO;
import com.sistema.tareas.service.TareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
@Tag(name = "Tareas", description = "Endpoints para la gestión de tareas")
public class TareaController {

    private final TareaService tareaService;

    @Operation(summary = "Crear una nueva tarea", description = "Crea una tarea asociada al usuario autenticado con estado PENDIENTE")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tarea creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @PostMapping
    public ResponseEntity<TareaDTO> crear(@Valid @RequestBody TareaDTO tareaDTO) {
        String username = obtenerUsername();
        log.info("POST /api/tareas - Usuario {} creando tarea", username);
        TareaDTO creada = tareaService.crearTarea(username, tareaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Listar tareas del usuario autenticado", description = "Retorna las tareas del usuario con paginación, filtro y ordenamiento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista paginada de tareas"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping
    public ResponseEntity<Page<TareaDTO>> listarTareas(
            @PageableDefault(size = 10, sort = "fechaVencimiento", direction = Sort.Direction.ASC) Pageable pageable) {
        String username = obtenerUsername();
        log.info("GET /api/tareas - Usuario {} listando tareas (page={}, size={})", username, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(tareaService.listarPorUsuario(username, pageable));
    }

    @Operation(summary = "Obtener tarea por ID", description = "Retorna una tarea específica del usuario autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarea encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TareaDTO> obtenerPorId(@PathVariable Long id) {
        String username = obtenerUsername();
        log.info("GET /api/tareas/{} - Usuario {}", id, username);
        return ResponseEntity.ok(tareaService.obtenerPorId(id, username));
    }

    @Operation(summary = "Completar tarea", description = "Cambia el estado de la tarea a COMPLETA permanentemente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarea completada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "409", description = "La tarea ya está completa")
    })
    @PutMapping("/{id}/complete")
    public ResponseEntity<TareaDTO> completarTarea(@PathVariable Long id) {
        String username = obtenerUsername();
        log.info("PUT /api/tareas/{}/complete - Usuario {}", id, username);
        return ResponseEntity.ok(tareaService.completarTarea(id, username));
    }

    @Operation(summary = "Actualizar tarea", description = "Actualiza los datos de una tarea existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TareaDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TareaDTO tareaDTO) {
        String username = obtenerUsername();
        log.info("PUT /api/tareas/{} - Usuario {}", id, username);
        return ResponseEntity.ok(tareaService.actualizarTarea(id, username, tareaDTO));
    }

    @Operation(summary = "Eliminar tarea", description = "Elimina una tarea del usuario autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tarea eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        String username = obtenerUsername();
        log.info("DELETE /api/tareas/{} - Usuario {}", id, username);
        tareaService.eliminar(id, username);
        return ResponseEntity.noContent().build();
    }

    private String obtenerUsername() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        return authentication.getName();
    }
}
