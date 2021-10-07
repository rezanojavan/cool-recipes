package nl.abnamro.app.service;

import nl.abnamro.app.model.RecipeEnt;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * Recipe service specification
 *
 * @author Reza Nojavan
 */
public interface RecipeService {

    /**
     * Create new recipe
     *
     * @param recipe to create
     * @return created recipe
     */
    RecipeEnt create(RecipeEnt recipe);

    /**
     * Update a recipe
     *
     * @param id to update
     * @param recipe to update
     * @return updated recipe
     */
    RecipeEnt update(Integer id, RecipeEnt recipe);

    /**
     * Find a recipe based on given parameter
     *
     * @param name          to filter recipes
     * @param vegetarian    to filter recipes
     * @param serves        to filter recipes
     * @param ingredients   to filter recipes
     * @param instruction   to filter recipes
     * @return list of recipes
     */
    Iterable<RecipeEnt> find(@Nullable String name,
                             @Nullable Boolean vegetarian,
                             @Nullable Integer serves,
                             @Nullable String ingredients,
                             @Nullable String instruction);

    /**
     * Find a recipe by Id
     *
     * @param id to find
     * @return optional
     */
    Optional<RecipeEnt> findById(Integer id);

    /**
     * Delete a recipe by its Id
     * @param id to delete
     */
    void delete(Integer id);

}
