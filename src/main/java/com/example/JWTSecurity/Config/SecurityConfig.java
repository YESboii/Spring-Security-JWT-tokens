package com.example.JWTSecurity.Config;

import com.example.JWTSecurity.security.JwtAuthenticationEntryPoint;
import com.example.JWTSecurity.security.JwtAuthenticationFilter;
import com.example.JWTSecurity.security.LogoutHandlerImpl;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    private final LogoutHandlerImpl logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
         return httpSecurity
                 .csrf(AbstractHttpConfigurer::disable)
                 .authorizeHttpRequests(authorization ->
                         authorization
                                 .requestMatchers("/auth/**").permitAll()
                                 .anyRequest().authenticated()

                 )
                 .sessionManagement(session ->
                         session.sessionCreationPolicy(SessionCreationPolicy.NEVER)
                 )
                 .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class
                 )
                 .exceptionHandling(exception ->
                         exception
                                 .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                 .accessDeniedHandler(accessDeniedHandler)
                 )
                 .securityContext(context -> context.securityContextRepository(new NullSecurityContextRepository()))
                 .logout(logout ->
                            logout
                                    .addLogoutHandler(logoutHandler)
                                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                                    .logoutUrl("/auth/logout"))
                 .build();

    }
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return  new ProviderManager(daoAuthenticationProvider);
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
}

//    If you still want to declare your filter as a Spring bean to take advantage of
//    dependency injection for example, and avoid the duplicate invocation,
//        you can tell Spring Boot to not register it with the container by declaring
//        a FilterRegistrationBean bean and setting its enabled property to false:
//    @Bean
//    public FilterRegistrationBean<TenantFilter> tenantFilterRegistration(TenantFilter filter) {
//        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>(filter);
//        registration.setEnabled(false);
//        return registration;
//    }