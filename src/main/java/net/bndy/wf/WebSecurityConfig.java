package net.bndy.wf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable() // TODO: remove before release
            .authorizeRequests()
                .antMatchers("/", "/home", "/webjars/*/**", "/static/*/**", 
                		"/api/*/**", 
                		"/test/*/**",
                		"/v2/api-docs", "/docs/*/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//            .inMemoryAuthentication()
//                .withUser("admin").password("123456").roles("ADMIN");
    	auth.jdbcAuthentication().dataSource(this.dataSource)
    		.usersByUsernameQuery("SELECT username, password, enabled FROM app_user WHERE username=?")
    		.authoritiesByUsernameQuery("SELECT username, role FROM app_user_role WHERE username=?");
    	
    }
}