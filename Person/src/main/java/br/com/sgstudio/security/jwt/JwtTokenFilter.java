package br.com.sgstudio.security.jwt;

import br.com.sgstudio.exception.InvalidJwtAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);

        try {
            if (token != null && tokenProvider.validateToken(token))
            {
                Authentication auth = tokenProvider.getAuthentication(token);

                if (auth != null)
                {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (InvalidJwtAuthenticationException e) {
            e.printStackTrace();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
