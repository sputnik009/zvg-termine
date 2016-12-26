package de.magic.creation.repo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface INominatimRepository extends JpaRepository<NominatimCacheEntry, String>
{
}
