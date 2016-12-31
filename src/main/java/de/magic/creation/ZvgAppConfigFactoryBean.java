package de.magic.creation;

import org.apache.commons.configuration2.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class ZvgAppConfigFactoryBean extends AbstractFactoryBean<Configuration>
{
  private final ZvgAppConfig config;

  @Autowired
  public ZvgAppConfigFactoryBean( ZvgAppConfig config)
  {
    this.config = config;
    setSingleton( true);
  }

  @Override
  public Class< ? > getObjectType()
  {
    return Configuration.class;
  }

  @Override
  protected Configuration createInstance() throws Exception
  {
    return config.getConfig();
  }

}