package de.magic.creation;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class Config
{
//  @Bean
//  public CacheManager cacheManager() {
//      return new CaffeineCacheManager(ehCacheCacheManager().getObject());
//  }

//  @Bean
//  public EhCacheManagerFactoryBean ehCacheCacheManager() {
//      EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
//      factory.setConfigLocation(new ClassPathResource("ehcache.xml"));
//      factory.setShared(true);
//      return factory;
//  }
}
