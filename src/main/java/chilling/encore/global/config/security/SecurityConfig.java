package chilling.encore.global.config.security;

import chilling.encore.global.config.security.filter.CustomAccessDeniedHandler;
import chilling.encore.global.config.security.filter.CustomAuthenticationEntryPoint;
import chilling.encore.global.config.security.filter.JwtExceptionFilter;
import chilling.encore.global.config.security.filter.JwtFilter;
import chilling.encore.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtExceptionFilter exceptionFilter;
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .cors().configurationSource(corsConfigurationSource())
//                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .headers().frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .antMatchers("/alarm").authenticated()
                .antMatchers("/listen/mine").authenticated()
                .antMatchers("/listen/participant").authenticated()
                .antMatchers("/review/mine").authenticated()
                .antMatchers("/user/favRegions").authenticated()
                .antMatchers("/user/grade").authenticated()
                .antMatchers("/user/participants").authenticated()
                .antMatchers("/user/userInfo").authenticated()
                .antMatchers("/user/myPosts").authenticated()
                .antMatchers("/lecture/my").authenticated()
                .antMatchers(HttpMethod.PUT, "/user/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/free/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/listen/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/review/**").authenticated()
                .antMatchers(HttpMethod.POST, "/free/**").authenticated()
                .antMatchers(HttpMethod.POST, "/listen/**").authenticated()
                .antMatchers(HttpMethod.POST, "/review/**").authenticated()
                .antMatchers("/user/logout").permitAll()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionFilter, JwtFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resource/**", "/css/**", "/js/**", "/img/**", "/lib/**");
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addExposedHeader("Authorization");
//        configuration.addAllowedOriginPattern("*");
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
    //nginx에 cors 추가
}
