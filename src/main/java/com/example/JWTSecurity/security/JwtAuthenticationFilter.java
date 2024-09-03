package com.example.JWTSecurity.security;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import com.example.JWTSecurity.Repository.UserRepository;
import com.example.JWTSecurity.Service.JwtService;
import com.example.JWTSecurity.exceptionHandling.securityExceptions.JwtErrorHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtAuthenticationFilter: doFilterInternal called");
        String token = jwtService.extractToken(request);
        int i= 0;
//        if(i<1)  throw new BadCredentialsException("test");
        if(token == null){
            filterChain.doFilter(request,response);
            return;
        }

        try {
            String extractedEmail = jwtService.extractUsername(token);//
            System.out.println("JwtAuthenticationFilter: Extracted Email - " + extractedEmail);
            UserDetails userDetails = userRepository.findByEmail(extractedEmail).orElseThrow(RuntimeException::new);
            boolean isValidToken = jwtService.verifyToken(extractedEmail, userDetails, token);

            if (isValidToken) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
                // contains Returns the Internet Protocol (IP) address of the client or last proxy that sent the request
                //or session id;
                usernamePasswordAuthenticationToken.setDetails(webAuthenticationDetails);
                System.out.println(webAuthenticationDetails.toString());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                System.out.println("hello");
            }

            filterChain.doFilter(request, response);
        }
        catch (Exception e){
            System.out.println(e.getClass());
            e.printStackTrace();
            JwtErrorHandler.handle(request,response,e);  //Better way is to delegate it to the Exception translation filter
                                                        //by catching the jwt exceptions and rethrowing
                                                        // some exceptions(create subclass of the Authentication exception)
        }
    }

}

