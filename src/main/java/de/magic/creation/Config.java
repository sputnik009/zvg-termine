package de.magic.creation;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableCaching
@EnableJpaRepositories
@EnableAsync
@EnableScheduling
public class Config
{
  @Bean
  public javax.validation.Validator localValidatorFactoryBean()
  {
    return new LocalValidatorFactoryBean();
  }
  
  @Bean
  public ZvgAppConfigFactoryBean databseConfigFactory ( DataSource dataSource) throws IOException, ConfigurationException
  {
    return new ZvgAppConfigFactoryBean( new ZvgAppConfig( dataSource));
  }
}
