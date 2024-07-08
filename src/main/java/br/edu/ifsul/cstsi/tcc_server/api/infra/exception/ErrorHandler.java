package br.edu.ifsul.cstsi.tcc_server.api.infra.exception;

import br.edu.ifsul.cstsi.tcc_server.api.users.validations.ValidationEmailNotConfirmedYet;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity error400Handler(ConstraintViolationException ex) {
        var errors = ex.getConstraintViolations();
        return ResponseEntity.badRequest().body(errors.stream().map(ErroValidation::new).toList());
    }
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity error404Handler() { //404 - Not Found
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity error400Handler() { //400 - Bad Request
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ValidationEmailRegisteredException.class)
    public ResponseEntity error400Handler(ValidationEmailRegisteredException ex){ //400 - Bad Request para Erro de Validação das Regras de Negócio
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ValidationEmailNotConfirmedYetException.class)
    public ResponseEntity error400Handler(ValidationEmailNotConfirmedYetException ex){ //400 - Bad Request para Erro de Validação das Regras de Negócio
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private record ErroValidation(
            String field,
            String message) {
        public ErroValidation(ConstraintViolation<?> error){
            this(error.getPropertyPath().toString(), error.getMessage()); //qual campo e qual a mensagem do Validation
        }
    }
}
