package com.softwebdevelopers.ecommerce.security;

import com.softwebdevelopers.ecommerce.models.enums.EPrivilege;
import com.softwebdevelopers.ecommerce.models.enums.ERole;
import com.softwebdevelopers.ecommerce.security.jwt.JwtAuthEntryPoint;
import com.softwebdevelopers.ecommerce.security.jwt.JwtAuthTokenFilter;
import com.softwebdevelopers.ecommerce.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String BASE_URL = "/api";
//    private static final String SECURED_PATTERN = "/secured/**";

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/login",
            "/api/guest/**"
    };

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtAuthEntryPoint authEntryPoint;

    @Bean
    public JwtAuthTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()

                // USER
                .antMatchers(HttpMethod.GET, "/api/auth/user").permitAll()
                .antMatchers("/api/auth/user/**").authenticated()

                // CATEGORY
                .antMatchers(HttpMethod.GET, "/api/auth/category").permitAll()
//                .antMatchers(HttpMethod.POST,"/api/auth/category", "/api/auth/category/*").hasAnyAuthority(
//                        ERole.ROLE_SUPERADMIN.name(),
////                        EPrivilege.READ_PRIVILEGE.name(),
//                        EPrivilege.CREATE_PRIVILEGE.name(),
//                        EPrivilege.UPDATE_PRIVILEGE.name(),
//                        EPrivilege.DELETE_PRIVILEGE.name())
                .antMatchers("/api/auth/category").authenticated()

                // BRAND
                .antMatchers(HttpMethod.GET, "/api/auth/brand/**").permitAll()
                .antMatchers("/api/auth/brand/**").authenticated()

                // PRODUCT
                .antMatchers(HttpMethod.GET, "/api/auth/product/**").permitAll()
                .antMatchers("/api/auth/product/**").authenticated()

                // PRODUCT STOCK
                .antMatchers(HttpMethod.GET, "/api/auth/productstock").permitAll()
                .antMatchers("/api/auth/productstock/**").authenticated()

                //CART
                .antMatchers("/api/auth/cart/**").authenticated()

                //WISHLIST
                .antMatchers("/api/auth/wishlist/**").authenticated()

                // AUTH
                .antMatchers(HttpMethod.GET, "/api/auth/**").permitAll()

                //DEFAULT
                .antMatchers("/api/**").permitAll();

        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Access-Control-Allow-Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Cache-Control", "Content-Type", "Authorization"));
        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
        CorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        ((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
