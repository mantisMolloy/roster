package app.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

import static org.springframework.http.HttpMethod.*;

/**
 * Created by tmolloy on 17/10/2015.
 */

@Configuration
@EnableGlobalMethodSecurity (securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureForDevelopment(AuthenticationManagerBuilder auth,
                                        Environment env) throws Exception {
        if (env.acceptsProfiles("!production")) {
            auth.inMemoryAuthentication()
                    .withUser("phil").password("webb").roles("USER").and()
                    .withUser("roy").password("clarkson").roles("USER", "ADMIN");
        }
    }

    @Autowired
    public void configureForProduction(AuthenticationManagerBuilder auth,
                                       DataSource dataSource, Environment env) throws Exception {
        if (env.acceptsProfiles("production")) {
            auth.jdbcAuthentication().dataSource(dataSource);
        }
    }

    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(GET, "/teammates").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .requiresChannel()
                    .anyRequest().requiresSecure()
                    .and()
                .formLogin()
                    .defaultSuccessUrl("/teammates")
                    .and()
                .logout()
                    .logoutSuccessUrl("/teammates");
    }
}
