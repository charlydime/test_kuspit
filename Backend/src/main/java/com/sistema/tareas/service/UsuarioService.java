package com.sistema.tareas.service;

import com.sistema.tareas.dto.UsuarioDTO;
import com.sistema.tareas.model.entity.Usuario;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO);
    UsuarioDTO obtenerPorId(Long id);
    UsuarioDTO obtenerPorUsername(String username);
    List<UsuarioDTO> listarTodos();
    void eliminar(Long id);
    Usuario buscarOCrear(Usuario usuario);
}
