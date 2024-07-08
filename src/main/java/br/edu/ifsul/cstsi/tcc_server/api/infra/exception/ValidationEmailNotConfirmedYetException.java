package br.edu.ifsul.cstsi.tcc_server.api.infra.exception;


public class ValidationEmailNotConfirmedYetException extends RuntimeException {
    public ValidationEmailNotConfirmedYetException(String message) {
        super(message);
    }
}
