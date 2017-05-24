package pl.sjacek.calculator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pl.sjacek.calculator.model.Calculation;

import java.util.List;

/**
 * Created by jacek on 21.05.17.
 */
@RepositoryRestResource(collectionResourceRel = "calculation", path = "calculation")
public interface CalculationRepository extends JpaRepository<Calculation,Long> {
    List<Calculation> findAll();
    List<Calculation> findById(@Param("id") Long id);
}
