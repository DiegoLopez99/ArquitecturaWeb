package com.tpe.microserviciousarios.service;

import com.tpe.microserviciousarios.domain.Usuario;
import com.tpe.microserviciousarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario getUsuarioId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()){
            return usuario.get();
        }else{
            throw new RuntimeException("No se encontró el usuario con ID: " + id);
        }
    }

    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        try {
            return usuarioRepository.save(usuario);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar el usuario: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Usuario modificicarUsuario(Long id, Usuario usuarioNuevo) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()){
            Usuario usuario = usuarioExistente.get();
            usuario.setNombre(usuarioNuevo.getNombre());
            usuario.setApellido(usuarioNuevo.getApellido());
            usuario.setNroTelefono(usuarioNuevo.getNroTelefono());
            usuario.setEmail(usuarioNuevo.getEmail());
            usuario.setNombreUsuario(usuarioNuevo.getNombreUsuario());
            usuario.setContrasenia(usuarioNuevo.getContrasenia());
            return usuarioRepository.save(usuario);
        }else{
            throw new RuntimeException("No se encontró el usuario con ID: " + id);
        }
    }

    @Transactional
    public boolean eliminarUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuarioRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
