package de.vereinsplaner.backend.repository;

import de.vereinsplaner.backend.model.Veranstaltung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeranstaltungRepository extends JpaRepository<Veranstaltung, Long> {
}