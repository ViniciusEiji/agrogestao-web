package br.com.agroehtop.top.exception;

/**
 * Exceção lançada quando uma regra ou invariante de negócio é violada.
 */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String message) {
        super(message);
    }
}
