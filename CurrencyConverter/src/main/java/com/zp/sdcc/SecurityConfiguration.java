package com.zp.sdcc;

import static com.zp.sdcc.common.CurrencyConverterConstants.LOGIN_REQUEST_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.REGISTER_REQUEST_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.ROOT_MAPPING;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
    private UserDetailsService userDetailsService;


	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
    

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/css/**",REGISTER_REQUEST_MAPPING,"/h2-console/**").permitAll()
        .anyRequest().authenticated().and()       
        .formLogin().loginPage(LOGIN_REQUEST_MAPPING).permitAll().defaultSuccessUrl(ROOT_MAPPING).and()
        .logout().permitAll();

       http.csrf().disable();
       http.headers().frameOptions().disable();
    }

}
