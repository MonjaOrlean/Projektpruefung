package de.vereinsplaner.backend.repository;

import de.vereinsplaner.backend.model.SchichtZuweisung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query(
            "DELETE FROM SchichtZuweisung z " +
                    "WHERE z.schicht.id = :schichtId"
    )
    void deleteBySchichtId(
            @Param("schichtId") Long schichtId
    );

    @Modifying
    @Transactional
    @Query(
            "DELETE FROM SchichtZuweisung z " +
                    "WHERE z.mitglied.id = :mitgliedId"
    )
    void deleteByMitgliedId(
            @Param("mitgliedId") Long mitgliedId
    );
}