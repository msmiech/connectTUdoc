package at.connectTUdoc.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * This class implements the settings for the spring WebSecurity
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FirebaseAuthenticationProvider authenticationProvider;

    private FirebaseAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new FirebaseAuthenticationTokenFilter();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**");
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/office").permitAll()
            .antMatchers(HttpMethod.GET, "/office/{id}").permitAll()
            .antMatchers(HttpMethod.GET, "/office/searchText/{searchText}").permitAll()
            .antMatchers(HttpMethod.GET, "/office/searchTextNameOnly/{searchText}").permitAll()
            .antMatchers(HttpMethod.GET, "/office/searchTextSpecialityNameOnly/{searchText}").permitAll()
            .antMatchers(HttpMethod.GET, "/office/searchTextDoctorNameOnly/{searchText}").permitAll()
            .antMatchers(HttpMethod.GET, "/appointment/available/{officeId}/{date}").permitAll()
            //Temporary for testing
            //.antMatchers("/office/**").permitAll()
            //.antMatchers("/chat/**").permitAll()
            //.antMatchers("/medicalworker/**").permitAll()
            //.antMatchers("/appointment/**").permitAll()
            .anyRequest().authenticated()
            .and().requiresChannel().anyRequest().requiresSecure() //enforce HTTPS security.require-ssl=true
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Custom firebase based security filter
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), BasicAuthenticationFilter.class);
    }
}
