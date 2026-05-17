
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import usuariovalidator.UsuarioValidator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author maria
 */
public class UsuarioValidatorTest {
    UsuarioValidator validator = new UsuarioValidator();
    
    @Test
    @DisplayName("Usuario y password válidos")
    void usuarioYPasswordValidos() {
        assertTrue(validator.registrarUsuario("sandra123", "Abc1!"));
    }
    
    @Test 
    @DisplayName("Usuario con mayúscula no permitidos")
    void usuarioConMayuscula() {
        assertFalse(validator.registrarUsuario("Sandra123", "Abc1!"));
    }
    
    @Test
    @DisplayName("Usuario con carácter especial no permitido")
    void usuarioConCaracterEspecial() {
        assertFalse(validator.registrarUsuario("sandra_123", "Abc1!"));
    }
    
    @Test 
    @DisplayName("Usuario vacío no válido")
    void usuarioVacio() {
        assertFalse(validator.registrarUsuario("", "Abc1!"));
    }
    
    @Test 
    @DisplayName("Password sin mayúscula")
    void PasswordSinMayuscula() {
        assertFalse(validator.registrarUsuario("sandra123", "abc1!"));
    }
    
    @Test 
    @DisplayName("Password sin minuscula")
    void PasswordSinMinuscula() {
        assertFalse(validator.registrarUsuario("sandra123", "ABC1!"));
    }
    
    @Test 
    @DisplayName("Password sin numero")
    void PasswordSinNumero() {
        assertFalse(validator.registrarUsuario("sandra123", "Abcd!"));
    } 
    
    @Test
    @DisplayName("Password sin carácter especial")
    void PasswordSinCaracterEspecial() {
        assertFalse(validator.registrarUsuario("sandra123", "Abc12"));
    }
    
    @Test
    @DisplayName("Password vacío")
    void PasswordVacio() {
        assertFalse(validator.registrarUsuario("sandra123", ""));
    }
}
