package de.magic.creation.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IZvgObjectRepository extends JpaRepository<ZvgObject, Long>
{
  List<ZvgObject> findByStadtAndLandAndArtInAndTerminAfterOrderByVerkerhswert(String stadt, ELand land, EKind[] art, LocalDateTime termin);
  
  List<ZvgObject> findByLandAndArtInAndTerminAfterOrderByVerkerhswert(ELand land, EKind[] art, LocalDateTime termin);
}
