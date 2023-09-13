//package org.launchcode.journal;
//
//import org.launchcode.journal.models.EntryService;
//import org.launchcode.journal.models.NlpService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.nio.charset.Charset;
//import java.util.List;
//
//@Configuration
//public class WebApplicationConfig implements WebMvcConfigurer {
//
//
//    // Create spring-managed object to allow the app to access our filter
//    @Bean
//    public AuthenticationFilter authenticationFilter() {
//        return new AuthenticationFilter();
//    }
//
//    // Register the filter with the Spring container
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor( authenticationFilter() );
//    }
//
//    //Override the AJAX type
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
//    }
//
//}