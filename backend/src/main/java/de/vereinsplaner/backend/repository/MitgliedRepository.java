package de.vereinsplaner.backend.repository;

import de.vereinsplaner.backend.model.Mitglied;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MitgliedRepository extends JpaRepository<Mitglied, Long> {
}