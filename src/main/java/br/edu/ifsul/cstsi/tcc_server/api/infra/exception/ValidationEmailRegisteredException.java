package br.edu.ifsul.cstsi.tcc_server.api.infra.exception;

public class ValidationEmailRegisteredException extends RuntimeException {
    public ValidationEmailRegisteredException(String message) {
        super(message);
    }
}
