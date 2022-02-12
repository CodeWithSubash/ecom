package com.softwebdevelopers.ecommerce.exceptions;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@ControllerAdvice
public class ECOMExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ValidationErrorDTO> handleAllException(Exception ex, WebRequest request) {
        ValidationErrorDTO dto = new ValidationErrorDTO(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(), request.getDescription(false));
        ex.printStackTrace();

        log.info("Exception INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<>(dto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ValidationErrorDTO> handleAccessDeniedException(AccessDeniedException ex,
                                                                                WebRequest request) {
        ValidationErrorDTO error = new ValidationErrorDTO(new Date(), HttpStatus.UNAUTHORIZED.value(), ex.getMessage(),
                request.getDescription(false));
        log.info("AccessDeniedException INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ValidationErrorDTO> handleCustomMessageException(BadRequestException ex, WebRequest request) {
        String detail = ex.getLocalizedMessage();
        ValidationErrorDTO error = new ValidationErrorDTO(new Date(), HttpStatus.BAD_REQUEST.value(),
                ECOMMessage.BAD_REQUEST, detail);
        log.info("CustomMessageException INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentFatalException.class)
    public final ResponseEntity<ValidationErrorDTO> handlePaymentFatalException(PaymentFatalException ex, WebRequest request) {
        String detail = ex.getLocalizedMessage();
        ValidationErrorDTO error = new ValidationErrorDTO(new Date(), HttpStatus.PAYMENT_REQUIRED.value(),
                ECOMMessage.PAYMENT_FAILED, detail);
        log.info("PaymentFatalException INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<ValidationErrorDTO> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
        String detail = ex.getLocalizedMessage();
        ValidationErrorDTO error = new ValidationErrorDTO(new Date(), HttpStatus.NO_CONTENT.value(),
                ECOMMessage.RECORD_DOES_NOT_EXIST, detail);
        log.info("RecordNotFoundException INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(UnprocesableException.class)
    public final ResponseEntity<ValidationErrorDTO> handleUnprocesablleException(UnprocesableException ex, WebRequest request) {
        String detail = ex.getLocalizedMessage();
        ValidationErrorDTO error = new ValidationErrorDTO(new Date(), HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ECOMMessage.UNPROCESSABLE_ENTITY_FOUND, detail);
        log.info("UnprocesablleException INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(RecordConflictException.class)
    public final ResponseEntity<ValidationErrorDTO> handleRecordAlreadyExistsException(RecordConflictException ex, WebRequest request) {
        String detail = ex.getLocalizedMessage();
        ValidationErrorDTO error = new ValidationErrorDTO(new Date(), HttpStatus.CONFLICT.value(),
                ECOMMessage.RECORD_CONFLICT_OCCURRED, detail);
        log.info("RecordConflictException INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    // @Validate For Validating Path Variables and Request Parameters
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(HttpServletResponse response) throws IOException {
        log.info("ConstraintViolationException INFO : {}", ECOMMessage.CONSTRAINT_VIOLATION);
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ValidationErrorDTO> handlePropertyValueException(PropertyValueException ex, WebRequest request) {
        String detail = ex.getLocalizedMessage();
        ValidationErrorDTO error = new ValidationErrorDTO(new Date(), HttpStatus.NOT_ACCEPTABLE.value(),
                ECOMMessage.DATA_INTEGRITY_VIOLATION, detail);
        error.addFieldError(ex.getPropertyName(), ex.getMessage());
        log.info("DataIntegrityViolationException INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({MailAuthenticationException.class})
    public ResponseEntity<ValidationErrorDTO> handleMail(RuntimeException ex, WebRequest request) {
        String detail = ex.getLocalizedMessage();
        log.error("500 Status Code", ex);
        ValidationErrorDTO error = new ValidationErrorDTO(new Date(),
                HttpStatus.NOT_FOUND.value(), ECOMMessage.EMAIL_CONFIG_ERROR, detail);
        return new ResponseEntity<ValidationErrorDTO>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationErrorDTO dto = new ValidationErrorDTO(new Date(), HttpStatus.BAD_REQUEST.value(),
                ECOMMessage.METHOD_ARGUMENT_NOT_VALID, "");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            dto.addFieldError(error.getField(), error.getDefaultMessage());
        }
        log.warn("MethodArgumentNotValidException INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<Object>(dto, headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod()).append(" method is not supported. ");
        builder.append("Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        ValidationErrorDTO error = new ValidationErrorDTO(new Date(), HttpStatus.METHOD_NOT_ALLOWED.value(),
                ECOMMessage.HTTP_METHOD_NOT_SUPPORTED, builder.toString());

        log.warn("MethodArgumentNotValidException INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error, headers, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append("Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        ValidationErrorDTO error = new ValidationErrorDTO(new Date(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                ECOMMessage.MEDIA_TYPE_NOT_SUPPORTED, builder.substring(0, builder.length() - 2));

        log.warn("HttpMediaTypeNotSupportedException INFO : {}", ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

}
