package nl.abnamro.app.repository;

import nl.abnamro.app.model.RecipeEnt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Recipe JPA repository
 *
 * @author Reza Nojavan
 */
@Repository
public interface RecipeRepository extends CrudRepository<RecipeEnt, Integer>, JpaSpecificationExecutor<RecipeEnt> {
}
