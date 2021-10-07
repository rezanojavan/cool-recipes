package nl.abnamro.app.service;

import nl.abnamro.app.exception.ResourceNotFoundException;
import nl.abnamro.app.model.RecipeEnt;
import nl.abnamro.app.repository.RecipeRepository;
import nl.abnamro.app.repository.specification.RecipeSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Recipe service implementation
 *
 * @author Reza Nojavan
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    /**
     * Logger
     */
    private final static Logger logger = LogManager.getLogger();

    /**
     * Recipe repository
     */
    private RecipeRepository repository;

    /**
     * Constructor
     *
     * @param repository to set
     */
    public RecipeServiceImpl(RecipeRepository repository) {
        this.repository = repository;
    }


    @Override
    public RecipeEnt create(RecipeEnt recipe) {
        logger.debug("Creating recipe {}", recipe);
        return repository.save(recipe);
    }

    @Override
    public RecipeEnt update(Integer id, RecipeEnt recipe) {
        logger.debug("Updating recipe {}", recipe);
        Optional<RecipeEnt> oldRecipeOptional = findById(id);
        if (oldRecipeOptional.isEmpty()) {
            throw new ResourceNotFoundException("Recipe not found");
        }
        RecipeEnt oldRecipe = oldRecipeOptional.get();
        oldRecipe.setName(recipe.getName());
        oldRecipe.setIngredients(recipe.getIngredients());
        oldRecipe.setInstruction(recipe.getInstruction());
        oldRecipe.setServes(recipe.getServes());
        oldRecipe.setVegetarian(recipe.getVegetarian());
        return repository.save(oldRecipe);
    }

    @Override
    public Iterable<RecipeEnt> find(String name, Boolean vegetarian, Integer serves, String ingredients, String instruction) {
        logger.debug("finding recipe by vegetarian:{}, serves:{}, ingredients:{}, instruction:{}",
                vegetarian, serves, ingredients, instruction);
        Specification<RecipeEnt> specification = null;
        if (name != null) {
            specification = RecipeSpecification.nameLike(name);
        }
        if (vegetarian != null) {
            if (specification != null) {
                specification = specification.and(RecipeSpecification.vegetarianEqual(vegetarian));
            } else {
                specification = RecipeSpecification.vegetarianEqual(vegetarian);
            }
        }
        if (serves != null) {
            if (specification != null) {
                specification = specification.and(RecipeSpecification.servesEqual(serves));
            } else {
                specification = RecipeSpecification.servesEqual(serves);
            }
        }

        if (ingredients != null) {
            if (specification != null) {
                specification = specification.and(RecipeSpecification.ingredientsLike(ingredients));
            } else {
                specification = RecipeSpecification.ingredientsLike(ingredients);
            }
        }
        if (instruction != null) {
            if (specification != null) {
                specification = specification.and(RecipeSpecification.instructionLike(instruction));
            } else {
                specification = RecipeSpecification.instructionLike(instruction);
            }
        }
        return repository.findAll(specification);
    }

    @Override
    public Optional<RecipeEnt> findById(Integer id) {
        logger.debug("find recipe by id:{}", id);
        return repository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        logger.debug("deleting recipe by id:{}", id);
        repository.deleteById(id);
    }
}
