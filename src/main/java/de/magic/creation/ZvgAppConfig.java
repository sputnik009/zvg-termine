package de.magic.creation;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.DatabaseConfiguration;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.DatabaseBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ZvgAppConfig
{
  private final BasicConfigurationBuilder<DatabaseConfiguration> builder;

  private final DatabaseConfiguration                            configuration;

  @Autowired
  public ZvgAppConfig( DataSource dataSource) throws IOException, ConfigurationException
  {
    createConfigTable( dataSource);

    DatabaseBuilderParameters pbp = new Parameters().database().setDataSource( dataSource)
      .setTable( "Config")
      .setKeyColumn( "KEY")
      .setValueColumn( "VALUE");

    builder = new BasicConfigurationBuilder<DatabaseConfiguration>( DatabaseConfiguration.class).configure( pbp);
    configuration = builder.getConfiguration();
  }

  public Configuration getConfig()
  {
    return configuration;
  }

  public void save( String key, Object value) throws ConfigurationException
  {
    configuration.setProperty( key, value);
  }

  private void createConfigTable( DataSource dataSource)
  {
    JdbcTemplate jdbc = new JdbcTemplate( dataSource);
    String createTable =
      "CREATE TABLE IF NOT EXISTS Config(KEY VARCHAR(255) NOT NULL PRIMARY KEY, VALUE VARCHAR(255));";
    jdbc.execute( createTable);
  }
}
