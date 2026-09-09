package de.vereinsplaner.backend.repository;

import de.vereinsplaner.backend.model.Aufgabe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AufgabeRepository extends JpaRepository<Aufgabe, Long> {
}
