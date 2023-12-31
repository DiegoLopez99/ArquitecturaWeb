package com.securityjwt.security.Service.Dto.User.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class UserRequestDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private Set<String> authorities;

}

