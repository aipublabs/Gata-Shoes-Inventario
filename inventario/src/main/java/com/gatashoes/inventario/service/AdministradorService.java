package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.CorreoDuplicadoException;
import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Administrador> listarAdministradores() {
        return administradorRepository.findAll();
    }

    public Administrador guardarAdministrador(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    public Administrador obtenerAdminPorId(Integer idAdministrador) {
        return administradorRepository.findById(idAdministrador).orElse(null);
    }

    public Administrador obtenerAdminPorIdOrThrow(Integer idAdministrador) {
        return administradorRepository.findById(idAdministrador)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado con id " + idAdministrador));
    }

    public Optional<Administrador> obtenerAdminPorCorreo(String correo) {
        return administradorRepository.findByCorreo(correo);
    }

    public Administrador actualizarAdministrador(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    public void eliminarAdministrador(Integer idAdministrador) {
        if (!administradorRepository.existsById(idAdministrador)) {
            throw new ResourceNotFoundException("Administrador no encontrado con id " + idAdministrador);
        }
        administradorRepository.deleteById(idAdministrador);
    }

    /**
     * Registra un nuevo administrador en el sistema.
     *
     * Realiza el siguiente proceso:
     * 1. Normaliza el nombre y correo (espacios, mayúsculas)
     * 2. Verifica que el correo no exista en la base de datos
     * 3. Cifra la contraseña con BCrypt
     * 4. Persiste el administrador en la base de datos
     *
     * @param administrador Objeto Administrador con nombre, correo y contraseña sin cifrar
     * @return Administrador guardado con idAdmin asignado por la base de datos
     * @throws CorreoDuplicadoException si el correo ya está registrado
     */
    public Administrador registrarAdministrador(Administrador administrador) {
        // Normalizar nombre
        administrador.setNombre(administrador.getNombre().trim());

        // Normalizar correo: espacios y minúsculas
        String correoNormalizado = administrador.getCorreo().trim().toLowerCase(Locale.ROOT);

        // Verificar que el correo no exista
        if (administradorRepository.existsByCorreoIgnoreCase(correoNormalizado)) {
            throw new CorreoDuplicadoException("El correo ya se encuentra registrado");
        }

        // Asignar correo normalizado
        administrador.setCorreo(correoNormalizado);

        // Cifrar contraseña con BCrypt
        String contrasenaCifrada = passwordEncoder.encode(administrador.getContrasena());
        administrador.setContrasena(contrasenaCifrada);

        // Persistir y retornar
        return administradorRepository.save(administrador);
    }
}
