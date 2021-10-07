package nl.abnamro.app.controller;

import nl.abnamro.app.dto.CreationOutput;
import nl.abnamro.app.dto.Recipe;
import nl.abnamro.app.model.RecipeEnt;
import nl.abnamro.app.service.RecipeService;
import nl.abnamro.app.spec.RecipeApi;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Cool recipe API controller. This controller expose below APIs
 * <ol>
 *     <li>Create: create a new recipe</li>
 *     <li>Update: update a recipe</li>
 *     <li>Find by Id: find a recipe by Id</li>
 *     <li>Find: search recipes by fields</li>
 *     <li>Delete: delete a recipe</li>
 * </ol>
 *
 * @author Reza Nojavan
 */
@RestController
@RequestMapping("/rest")
public class RecipeApiControllerImpl implements RecipeApi {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private RecipeService recipeService;
    private ModelMapper mapper;

    /**
     * Constructor. The constructor also initiate the Model Mapper
     *
     * @param recipeService to set
     */
    public RecipeApiControllerImpl(RecipeService recipeService) {
        this.recipeService = recipeService;
        initMapper();
    }

    /**
     * Convert a list of string to a `$` separated string
     *
     * @param ctx to convert
     * @return a `$` separated string
     */
    private static String convert(MappingContext<List<String>, String> ctx) {
        StringBuilder builder = new StringBuilder();
        if (ctx.getSource() != null) {
            ctx.getSource().forEach(value -> builder.append(value).append("$"));
        }
        return builder.substring(0, builder.length() - 1);
    }

    @Override
    public ResponseEntity<CreationOutput> create(Recipe body) {
        RecipeEnt recipe = recipeService.create(mapper.map(body, RecipeEnt.class));
        return ResponseEntity.ok(mapper.map(recipe, CreationOutput.class));
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        recipeService.delete(id);
        return ResponseEntity.status(
                HttpStatus.NO_CONTENT
        ).build();
    }

    @Override
    public ResponseEntity<Recipe> findById(Integer id) {
        Optional<RecipeEnt> recipe = recipeService.findById(id);
        if (recipe.isPresent()) {
            return ResponseEntity.ok(mapper.map(recipe.get(), Recipe.class));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<List<Recipe>> findAll(String name, Boolean vegetarian, Integer servers, String ingredient, String instruction) {
        Iterable<RecipeEnt> recipes = recipeService.find(name, vegetarian, servers, ingredient, instruction);
        return ResponseEntity.ok(StreamSupport
                .stream(recipes.spliterator(), true)
                .map(val -> mapper.map(val, Recipe.class))
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Void> update(Integer id, Recipe recipe) {
        recipeService.update(id, mapper.map(recipe, RecipeEnt.class));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Configure the Model Mapper
     */
    private void initMapper() {
        Converter<String, List<String>> ingredientStringConverter =
                ctx -> ctx.getSource() == null ? null : Arrays.asList(ctx.getSource().split("[$]"));
        Converter<Date, String> dateConverter =
                ctx -> ctx.getSource() == null ? null : sdf.format(ctx.getSource());
        Converter<List<String>, String> ingredientListConverter = RecipeApiControllerImpl::convert;
        mapper = new ModelMapper();
        mapper.typeMap(Recipe.class, RecipeEnt.class)
                .addMappings(mapper -> mapper.using(ingredientListConverter).map(Recipe::getIngredients, RecipeEnt::setIngredients));
        mapper.typeMap(RecipeEnt.class, Recipe.class)
                .addMappings(mapper -> mapper.using(ingredientStringConverter).map(RecipeEnt::getIngredients, Recipe::setIngredients));
        mapper.typeMap(RecipeEnt.class, Recipe.class)
                .addMappings(mapper -> mapper.using(dateConverter).map(RecipeEnt::getCreationTime, Recipe::setCreationTime));
    }
}
