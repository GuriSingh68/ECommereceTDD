package EGEN5203.EcommerceTDD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories
public class EcommerceTddApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceTddApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000") // Allow your frontend origin
						.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Allow specific HTTP methods
						.allowedHeaders("*") // Allow all headers
						.allowCredentials(true); // Allow credentials if needed
			}
		};
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**") // Map the URL pattern
				.addResourceLocations("file:./uploads/"); // Specify the location of the uploaded files
	}
}