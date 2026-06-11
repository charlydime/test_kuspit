package com.sistema.tareas.service;

import com.sistema.tareas.dto.TareaDTO;
import com.sistema.tareas.model.entity.Tarea;
import com.sistema.tareas.model.entity.Usuario;
import com.sistema.tareas.model.enums.EstadoTarea;
import com.sistema.tareas.repository.TareaRepository;
import com.sistema.tareas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public TareaDTO crearTarea(String username, TareaDTO tareaDTO) {
        log.debug("Creando tarea para usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        Tarea tarea = Tarea.builder()
                .usuario(usuario)
                .titulo(tareaDTO.getTitulo())
                .descripcion(tareaDTO.getDescripcion())
                .fechaVencimiento(tareaDTO.getFechaVencimiento())
                .estado(EstadoTarea.PENDIENTE)
                .build();

        tarea = tareaRepository.save(tarea);
        log.info("Usuario {} creó exitosamente la tarea ID: {}", username, tarea.getId());
        return toDTO(tarea);
    }

    @Override
    @Transactional(readOnly = true)
    public TareaDTO obtenerPorId(Long id, String username) {
        log.debug("Obteniendo tarea id: {} para usuario: {}", id, username);
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con id: " + id));

        if (!tarea.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("La tarea no pertenece al usuario autenticado");
        }

        return toDTO(tarea);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TareaDTO> listarPorUsuario(String username, Pageable pageable) {
        log.debug("Listando tareas paginadas para usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        return tareaRepository.findByUsuario(usuario, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public TareaDTO completarTarea(Long id, String username) {
        log.debug("Completando tarea id: {} para usuario: {}", id, username);

        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con id: " + id));

        if (!tarea.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("La tarea no pertenece al usuario autenticado");
        }

        if (tarea.getEstado() == EstadoTarea.COMPLETA) {
            throw new RuntimeException("La tarea ya está completa");
        }

        tarea.setEstado(EstadoTarea.COMPLETA);
        tarea = tareaRepository.save(tarea);
        log.info("Usuario {} completó exitosamente la tarea ID: {}", username, id);
        return toDTO(tarea);
    }

    @Override
    @Transactional
    public TareaDTO actualizarTarea(Long id, String username, TareaDTO tareaDTO) {
        log.debug("Actualizando tarea id: {} para usuario: {}", id, username);

        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con id: " + id));

        if (!tarea.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("La tarea no pertenece al usuario autenticado");
        }

        tarea.setTitulo(tareaDTO.getTitulo());
        tarea.setDescripcion(tareaDTO.getDescripcion());
        tarea.setFechaVencimiento(tareaDTO.getFechaVencimiento());

        tarea = tareaRepository.save(tarea);
        log.info("Usuario {} actualizó exitosamente la tarea ID: {}", username, id);
        return toDTO(tarea);
    }

    @Override
    @Transactional
    public void eliminar(Long id, String username) {
        log.debug("Eliminando tarea id: {} para usuario: {}", id, username);

        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con id: " + id));

        if (!tarea.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("La tarea no pertenece al usuario autenticado");
        }

        tareaRepository.delete(tarea);
        log.info("Usuario {} eliminó exitosamente la tarea ID: {}", username, id);
    }

    @Override
    @Transactional
    public void marcarVencidas() {
        log.debug("Marcando tareas vencidas");
        List<Tarea> vencidas = tareaRepository
                .findByFechaVencimientoBeforeAndEstado(LocalDateTime.now(), EstadoTarea.PENDIENTE);
        vencidas.forEach(t -> t.setEstado(EstadoTarea.VENCIDA));
        tareaRepository.saveAll(vencidas);
        log.info("{} tareas marcadas como VENCIDAS", vencidas.size());
    }

    private TareaDTO toDTO(Tarea tarea) {
        EstadoTarea estadoEfectivo = tarea.getEstado();
        if (estadoEfectivo == EstadoTarea.PENDIENTE
                && tarea.getFechaVencimiento().isBefore(LocalDateTime.now())) {
            estadoEfectivo = EstadoTarea.VENCIDA;
        }
        return TareaDTO.builder()
                .id(tarea.getId())
                .usuarioId(tarea.getUsuario().getId())
                .titulo(tarea.getTitulo())
                .descripcion(tarea.getDescripcion())
                .fechaVencimiento(tarea.getFechaVencimiento())
                .estado(estadoEfectivo)
                .build();
    }
}
