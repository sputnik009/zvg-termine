package de.magic.creation;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableCaching
@EnableJpaRepositories
public class Config
{
  @Bean
  public javax.validation.Validator localValidatorFactoryBean() {
     return new LocalValidatorFactoryBean();
  }
}
