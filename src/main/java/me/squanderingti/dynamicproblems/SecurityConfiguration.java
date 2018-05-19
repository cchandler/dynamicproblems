package me.squanderingti.dynamicproblems;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // Allow anonymous access of the root URL
        httpSecurity.authorizeRequests().antMatchers("/")
                .permitAll()
                .anyRequest()
                .anonymous();

        //Disable CSRF so CURL works
        httpSecurity.csrf().disable();
    }
}
