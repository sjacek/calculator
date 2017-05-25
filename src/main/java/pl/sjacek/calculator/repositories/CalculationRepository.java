package pl.sjacek.calculator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pl.sjacek.calculator.model.Calculation;

/**
 * Created by jacek on 21.05.17.
 */
@RepositoryRestResource
public interface CalculationRepository extends JpaRepository<Calculation,Long> {
}
