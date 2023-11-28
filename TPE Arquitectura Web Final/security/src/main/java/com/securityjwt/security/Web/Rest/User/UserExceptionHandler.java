package com.securityjwt.security.Web.Rest.User;


import com.securityjwt.security.Service.Exception.User.ErrorDTO;
import com.securityjwt.security.Service.Exception.User.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice( basePackages = "com.monopatin.authservice.web.rest.USER" )
public class UserExceptionHandler {

    @ExceptionHandler( UserException.class )
    public ErrorDTO getUserException(UserException ex ){
        return new ErrorDTO( ex.getCode(), ex.getMessage() );
    }
}
