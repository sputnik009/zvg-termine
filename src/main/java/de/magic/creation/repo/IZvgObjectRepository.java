package de.magic.creation.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IZvgObjectRepository extends JpaRepository<ZvgObject, Long>
{
  List<ZvgObject> findByStadtAndLandAndArtInAndTerminAfter(String stadt, ELand land, EKind[] art, LocalDateTime termin);
  
  List<ZvgObject> findByLandAndArtInAndTerminAfter(ELand land, EKind[] art, LocalDateTime termin);
}
