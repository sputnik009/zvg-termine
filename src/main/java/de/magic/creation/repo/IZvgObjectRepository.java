package de.magic.creation.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IZvgObjectRepository extends JpaRepository<ZvgObject, Long>
{
  Long removeByTerminLessThan( LocalDateTime termin);
  
  List<ZvgObject> findByStadtAndLandAndArtIn(String stadt, ELand land, EKind[] art);
}
