package de.vereinsplaner.backend.repository;

import de.vereinsplaner.backend.model.Einsatzbereich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EinsatzbereichRepository
        extends JpaRepository<Einsatzbereich, Long> {

    List<Einsatzbereich> findByVeranstaltungId(Long veranstaltungId);
}