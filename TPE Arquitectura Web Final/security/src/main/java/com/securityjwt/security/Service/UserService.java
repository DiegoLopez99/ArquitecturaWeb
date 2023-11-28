package com.securityjwt.security.Service;


import com.securityjwt.security.Entity.User;
import com.securityjwt.security.Repository.AccountRepository;
import com.securityjwt.security.Repository.AuthorityRepository;
import com.securityjwt.security.Repository.UserRepository;
import com.securityjwt.security.Service.Dto.User.Request.UserRequestDTO;
import com.securityjwt.security.Service.Dto.User.Response.UserResponseDTO;
import com.securityjwt.security.Service.Exception.User.EnumUserException;
import com.securityjwt.security.Service.Exception.User.NotFoundException;
import com.securityjwt.security.Service.Exception.User.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO request ) {
        if( this.userRepository.existsUsersByEmailIgnoreCase( request.getEmail() ) )
            throw new UserException( EnumUserException.already_exist, String.format("Ya existe un usuario con email %s", request.getEmail() ) );
        final var authorities = request.getAuthorities()
                .stream()
                .map( string -> this.authorityRepository.findById( string ).orElseThrow( () -> new NotFoundException("Autority", string ) ) )
                .toList();
        if( authorities.isEmpty() )
            throw new UserException( EnumUserException.invalid_authorities,
                    String.format("No se encontro ninguna autoridad con id %s", request.getAuthorities().toString() ) );
        final var user = new User( request );
        user.setAuthorities( authorities );
        final var encryptedPassword = passwordEncoder.encode( request.getPassword() );
        user.setPassword( encryptedPassword );
        final var createdUser = this.userRepository.save( user );
        return new UserResponseDTO( createdUser );
    }
}