package de.vereinsplaner.backend.repository;

import de.vereinsplaner.backend.model.Schicht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchichtRepository extends JpaRepository<Schicht, Long> {
}