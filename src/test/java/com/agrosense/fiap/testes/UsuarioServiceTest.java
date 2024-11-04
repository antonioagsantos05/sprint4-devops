package com.agrosense.fiap.testes;

import com.agrosense.fiap.model.UsuarioModel;
import com.agrosense.fiap.repository.UsuarioRepository;
import com.agrosense.fiap.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioModel usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new UsuarioModel();
        usuario.setNmEmail("teste@exemplo.com");
    }

    @Test
    void testCreateUsuario_Success() throws Exception {
        when(usuarioRepository.findByNmEmail(usuario.getNmEmail())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(usuario);

        UsuarioModel createdUsuario = usuarioService.createUsuario(usuario);

        assertNotNull(createdUsuario);
        assertEquals(usuario.getNmEmail(), createdUsuario.getNmEmail());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testCreateUsuario_EmailAlreadyExists() {
        when(usuarioRepository.findByNmEmail(usuario.getNmEmail())).thenReturn(Optional.of(usuario));

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.createUsuario(usuario);
        });

        String expectedMessage = "Este email já está cadastrado. Por favor, insira um email diferente.";
        assertEquals(expectedMessage, exception.getMessage());
        verify(usuarioRepository, never()).save(any(UsuarioModel.class));
    }
}
