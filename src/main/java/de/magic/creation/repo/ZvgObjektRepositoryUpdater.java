package de.magic.creation.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.magic.creation.search.SearchManagerWeb;

@Service
public class ZvgObjektRepositoryUpdater
{
  private final Logger               log = LoggerFactory.getLogger( ZvgObjektRepositoryUpdater.class);

  private final SearchManagerWeb     searchManager;

  private final IZvgObjectRepository zvgObjectRepository;

  public ZvgObjektRepositoryUpdater( SearchManagerWeb searchManager, IZvgObjectRepository zvgObjectRepository)
  {
    this.searchManager = searchManager;
    this.zvgObjectRepository = zvgObjectRepository;
  }

  //                      ms     s   m
  @Scheduled(fixedRate = 1000 * 60 * 60)
  public void update()
  {
    log.info( "update");

    for( ELand land : ELand.valuesAsList())
    {
      log.info( "update: " + land);

      List<ZvgObject> ofBundesland = searchManager.search( land);
      log.info( "returned " + ofBundesland.size());
      zvgObjectRepository.save( ofBundesland);
    }

    deleteOld();
  }

  private void deleteOld()
  {
    long cnt = zvgObjectRepository.removeByTerminLessThan( LocalDateTime.now());
    log.debug( "deleted olds: " + cnt);
  }
}
