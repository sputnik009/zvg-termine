package de.magic.creation.repo;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.magic.creation.search.SearchManagerWeb;

@Service
public class ZvgObjektRepositoryUpdater
{
  private final Logger               log = LoggerFactory.getLogger( ZvgObjektRepositoryUpdater.class);

  private final Configuration        zvgConf;

  private final SearchManagerWeb     searchManager;

  private final IZvgObjectRepository zvgObjectRepository;

  @Autowired
  public ZvgObjektRepositoryUpdater( SearchManagerWeb searchManager, IZvgObjectRepository zvgObjectRepository,
    Configuration zvgConf)
  {
    this.searchManager = searchManager;
    this.zvgObjectRepository = zvgObjectRepository;
    this.zvgConf = zvgConf;
  }

  //                      ms     s   m
  @Scheduled(fixedRate = 1000 * 60 * 60)
  public void update()
  {
    if( !isTimeToUpdate()) return;

    log.info( "update");

    for( ELand land : ELand.valuesAsList())
    {
      log.info( "update: " + land);

      List<ZvgObject> ofBundesland = searchManager.search( land);
      log.info( "returned " + ofBundesland.size());
      zvgObjectRepository.save( ofBundesland);
    }

    saveLastSearchUpdateDate();
  }

  private void saveLastSearchUpdateDate()
  {
    zvgConf.setProperty( "lastSearchUpdate", LocalDate.now().toEpochDay());
  }

  private boolean isTimeToUpdate()
  {
    LocalDate lastSearchUpdate = LocalDate.ofEpochDay( zvgConf.getLong( "lastSearchUpdate", 0));
    LocalDate nextSerachUpdate = lastSearchUpdate.plusDays( 1);

    log.info( "lastSearchUpdate: " + lastSearchUpdate);
    log.info( "nextSerachUpdate: " + nextSerachUpdate.plusDays( 1));

    return LocalDate.now().isAfter( nextSerachUpdate);
  }
}
