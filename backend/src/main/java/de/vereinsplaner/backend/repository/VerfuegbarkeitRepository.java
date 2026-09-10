package de.vereinsplaner.backend.repository;

import de.vereinsplaner.backend.model.Verfuegbarkeit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VerfuegbarkeitRepository
        extends JpaRepository<Verfuegbarkeit, Long> {

    List<Verfuegbarkeit> findByMitgliedId(Long mitgliedId);

    List<Verfuegbarkeit> findByMitgliedIdAndDatum(
            Long mitgliedId,
            LocalDate datum
    );
}