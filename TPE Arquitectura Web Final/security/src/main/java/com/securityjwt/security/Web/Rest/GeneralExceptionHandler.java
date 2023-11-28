package com.securityjwt.security.Web.Rest;

import com.securityjwt.security.Service.Exception.User.ErrorDTO;
import com.securityjwt.security.Service.Exception.User.NotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice( basePackages = "com.monopatin.authservice.web.rest" )
public class GeneralExceptionHandler {

    @ExceptionHandler( NotFoundException.class )
    public ErrorDTO getException(NotFoundException ex ){
        return new ErrorDTO( ex.getCode(), ex.getMessage() );
    }
}
