package de.vereinsplaner.backend.repository;

import de.vereinsplaner.backend.model.SchichtZuweisung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchichtZuweisungRepository
        extends JpaRepository<SchichtZuweisung, Long> {

    List<SchichtZuweisung> findBySchichtId(Long schichtId);

    List<SchichtZuweisung> findByMitgliedId(Long mitgliedId);

    boolean existsBySchichtIdAndMitgliedId(
            Long schichtId,
            Long mitgliedId
    );
}