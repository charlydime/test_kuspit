package com.sistema.tareas.service;

import com.sistema.tareas.dto.TareaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TareaService {
    TareaDTO crearTarea(String username, TareaDTO tareaDTO);
    TareaDTO obtenerPorId(Long id, String username);
    Page<TareaDTO> listarPorUsuario(String username, Pageable pageable);
    TareaDTO completarTarea(Long id, String username);
    TareaDTO actualizarTarea(Long id, String username, TareaDTO tareaDTO);
    void eliminar(Long id, String username);
    void marcarVencidas();
}
