package br.com.sgstudio.controller;

import br.com.sgstudio.repository.UserRepository;
import br.com.sgstudio.security.AccountCredentialsVO;
import br.com.sgstudio.security.jwt.JwtTokenProvider;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "Authenticate a user by credentials")
    @PostMapping(value = "/signin",
            consumes = { "application/json", "application/xml", "application/x-yaml" },
            produces = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity signin(@RequestBody AccountCredentialsVO data)
    {
        try {
            var username = data.getUsername();
            var password = data.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = userRepository.findByUsername(username);

            var token = "";

            if (user != null)
            {
                token = tokenProvider.createToken(username, user.getRoles());
            }
            else
            {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);

            return ResponseEntity.ok(model);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

}
