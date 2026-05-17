/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usuariovalidator;

/**
 *
 * @author maria
 */
public class UsuarioValidator {
    /**
     * Valida un usuario y una contraseña según las reglas especificadas.
     *
     * @param usuario El nombre de usuario (debe ser minúsculo, solo letras y
     * números).
     * @param password La contraseña (debe contener mayúsculas, minúsculas, al
     * menos un número y un carácter especial).
     * @return True si el usuario y la contraseña son válidos, False en caso
     * contrario.
     */
    public boolean registrarUsuario(String usuario, String password) {
        if (!usuario.matches("[a-z0-9]+")) { // Solo letras y números minúsculas
            return false;
        }
        if (password.isEmpty()) {
            return false;
        }
        boolean tieneMayusculas = false;
        boolean tieneMinusculas = false;
        boolean tieneNumero = false;
        boolean tieneCaracterEspecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                tieneMayusculas = true;
            } else if (Character.isLowerCase(c)) {
                tieneMinusculas = true;
            } else if (Character.isDigit(c)) {
                tieneNumero = true;
            } else if ("!@#$%&[]{}".indexOf(c) != -1) { // Verifica si es uncaracter especial
                tieneCaracterEspecial = true;
            }
        }
        return tieneMayusculas && tieneMinusculas && tieneNumero && tieneCaracterEspecial;
    }

}
