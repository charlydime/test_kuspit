package com.sistema.tareas.repository;

import com.sistema.tareas.model.entity.Tarea;
import com.sistema.tareas.model.entity.Usuario;
import com.sistema.tareas.model.enums.EstadoTarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    Page<Tarea> findByUsuario(Usuario usuario, Pageable pageable);
    List<Tarea> findByUsuarioId(Long usuarioId);
    List<Tarea> findByUsuarioIdAndEstado(Long usuarioId, EstadoTarea estado);
    List<Tarea> findByFechaVencimientoBeforeAndEstado(LocalDateTime fecha, EstadoTarea estado);
}
