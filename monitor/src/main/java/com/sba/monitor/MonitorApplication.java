package com.sba.monitor;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @description: spring boot admin
 * @author: zjbing
 * @create: 2019-03-28 11:30
 **/
@SpringCloudApplication
@EnableAdminServer
public class MonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
	}


	@Profile("insecure")
	@Configuration
	public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
		private final String adminContextPath;

		public SecurityPermitAllConfig(AdminServerProperties adminServerProperties) {
			this.adminContextPath = adminServerProperties.getContextPath();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
					.anyRequest()
					.permitAll()
					.and()
					.csrf()
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
					.ignoringAntMatchers(adminContextPath + "/instances", adminContextPath + "/actuator/**");
		}
	}

	@Profile("secure")
	@Configuration
	public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
		private final String adminContextPath;

		public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
			this.adminContextPath = adminServerProperties.getContextPath();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
			successHandler.setTargetUrlParameter("redirectTo");
			successHandler.setDefaultTargetUrl(adminContextPath + "/");

			http.authorizeRequests()
					.antMatchers(adminContextPath + "/assets/**").permitAll()
					.antMatchers(adminContextPath + "/login").permitAll()
					.anyRequest().authenticated()
					.and()
					.formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
					.logout().logoutUrl(adminContextPath + "/logout").and()
					.httpBasic().and()
					.csrf()
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
					.ignoringAntMatchers(adminContextPath + "/instances", adminContextPath + "/actuator/**");
			// @formatter:on
		}
	}
}
