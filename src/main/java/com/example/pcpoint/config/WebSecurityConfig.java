package com.example.pcpoint.config;

import com.example.pcpoint.security.jwt.AuthEntryPointJwt;
import com.example.pcpoint.security.jwt.AuthTokenFilter;
import com.example.pcpoint.security.user.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                //Auth endpoints
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/logout/**").permitAll()
                .antMatchers("/api/statistics/all").hasRole("ADMIN")

                //Location endpoints
                .antMatchers("/api/location/all").permitAll()
                .antMatchers("/api/location/find/**").permitAll()
                .antMatchers("/api/location/delete/**").hasRole("ADMIN")
                .antMatchers("/api/location/add").hasRole("ADMIN")
                .antMatchers("/api/location/update").hasRole("ADMIN")

                //Product endpoints
                .antMatchers("/api/product/all").permitAll()
                .antMatchers("/api/product/find/**").permitAll()
                .antMatchers("/api/product/delete/**").hasRole("ADMIN")
                .antMatchers("/api/product/add").hasRole("ADMIN")
                .antMatchers("/api/product/update").hasRole("ADMIN")

                //Review endpoints
                .antMatchers("/api/review/all").permitAll()
                .antMatchers("/api/review/delete/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/review/add").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/review/update").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/review/by_product/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/review/by_user/**").hasAnyRole("ADMIN", "USER")

                //Order endpoints
                .antMatchers("/api/order/all").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/order/find/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/order/delete/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/order/add").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/order/update").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/order/by_user/**").hasAnyRole("ADMIN", "USER")

                .antMatchers("/","/public/**", "/resources/**","/resources/public/**")
                .permitAll()
                .antMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
