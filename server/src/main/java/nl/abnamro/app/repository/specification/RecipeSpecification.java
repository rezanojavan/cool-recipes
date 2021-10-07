package nl.abnamro.app.repository.specification;

import nl.abnamro.app.model.RecipeEnt;
import org.springframework.data.jpa.domain.Specification;

/**
 * Recipe jpa specification
 *
 * @author Reza Nojavan
 */
public class RecipeSpecification {

    public static Specification<RecipeEnt> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<RecipeEnt> vegetarianEqual(Boolean vegetarian) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("vegetarian"), vegetarian);
    }

    public static Specification<RecipeEnt> servesEqual(Integer serves) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("serves"), serves);
    }

    public static Specification<RecipeEnt> instructionLike(String instruction) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("instruction"), "%" + instruction + "%");
    }

    public static Specification<RecipeEnt> ingredientsLike(String ingredients) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("ingredients"), "%" + ingredients + "%");
    }
}
