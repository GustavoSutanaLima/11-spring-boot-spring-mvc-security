package edu.gustdev.demosecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {
    /* 
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails john = User.builder()
                                .username("john")
                                .password("{noop}test123")
                                .roles("EMPLOYEE")
                                .build();
        
        UserDetails mary = User.builder()
                                .username("mary")
                                .password("{noop}test123")
                                .roles("EMPLOYEE", "MANAGER")
                                .build();

        UserDetails susan = User.builder()
                                .username("susan")
                                .password("{noop}test123")
                                .roles("EMPLOYEE", "MANAGER", "ADMIN")
                                .build();

        return new InMemoryUserDetailsManager(john, mary, susan);
    }
    */

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
                //DataSource já é uma pre-configuração do spring boot
        
            JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        
            //define query to recieve a user by username:
            jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT user_id, pw, active FROM members WHERE user_id=?"
            );                                                                            //? -> será passado no login form

            jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT user_id, role FROM roles WHERE user_id=?"
            );

            //define query to recieve the authorities/roles by:

        
            return jdbcUserDetailsManager;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity https) throws Exception {
        
        https.authorizeHttpRequests(configurer -> 
                configurer.requestMatchers("/").hasRole("EMPLOYEE")
                          .requestMatchers("/leaders/**").hasRole("MANAGER")
                          .requestMatchers("/systems/**").hasRole("ADMIN")
                          .anyRequest().authenticated())
                          .exceptionHandling(configurer -> 
                                configurer.accessDeniedPage("/access-denied"))
        
             .formLogin(form -> 
                      form.loginPage("/showMyLoginPage") // end-point GET para ir para login page customizada;
                          .loginProcessingUrl("/authenticateTheUser") //end-point que processará as informações da login page;
                          .permitAll())
                          .logout(logout -> logout.permitAll());
        
        return https.build();       
    }             
}
