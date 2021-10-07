package nl.abnamro.app.service;

import nl.abnamro.app.model.RecipeEnt;
import nl.abnamro.app.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    private RecipeRepository repository;

    private final static RecipeEnt RECIPE_ENT = new RecipeEnt();

    @BeforeEach
    public void setUp() throws Exception {
        repository = Mockito.mock(RecipeRepository.class);
        RECIPE_ENT.setId(1);
        RECIPE_ENT.setCreationTime(new Date());
        RECIPE_ENT.setName("Baked Potato");
        RECIPE_ENT.setVegetarian(true);
        RECIPE_ENT.setServes(4);
        RECIPE_ENT.setIngredients("potato 4pcs,Water 1L");
        RECIPE_ENT.setInstruction("Boil the potato");
        Mockito.doReturn(RECIPE_ENT).when(repository).save(any(RecipeEnt.class));
        Mockito.doReturn(Optional.of(RECIPE_ENT)).when(repository).findById(1);
    }

    @Test
    public void create() {
        RecipeService recipeService = new RecipeServiceImpl(repository);
        RecipeEnt recipe = new RecipeEnt();
        recipe.setName("Baked Potato");
        recipe.setVegetarian(true);
        recipe.setServes(4);
        recipe.setIngredients("potato 4pcs,Water 1L");
        recipe.setInstruction("Boil the potato");
        RecipeEnt created = recipeService.create(recipe);
        verify(repository, times(1)).save(recipe);
        assertEquals(RECIPE_ENT.getId(), created.getId());
        assertEquals(RECIPE_ENT.getCreationTime(), created.getCreationTime());

    }

    @Test
    public void update() {
        RecipeService recipeService = new RecipeServiceImpl(repository);
        when(repository.save(any(RecipeEnt.class))).thenAnswer(i -> i.getArguments()[0]);
        RecipeEnt recipe = new RecipeEnt();
        recipe.setCreationTime(RECIPE_ENT.getCreationTime());
        recipe.setName("Baked Potato[Edited] ");
        recipe.setVegetarian(RECIPE_ENT.getVegetarian());
        recipe.setServes(RECIPE_ENT.getServes());
        recipe.setIngredients(RECIPE_ENT.getIngredients());
        recipe.setInstruction(RECIPE_ENT.getInstruction());
        RecipeEnt updated = recipeService.update(RECIPE_ENT.getId(), recipe);
        verify(repository, times(1)).save(any(RecipeEnt.class));
        assertEquals(RECIPE_ENT.getId(), updated.getId());
        assertEquals(RECIPE_ENT.getCreationTime(), updated.getCreationTime());
        assertEquals(recipe.getName(), updated.getName());
    }

    @Test
    public void findById() {
        RecipeService recipeService = new RecipeServiceImpl(repository);
        Optional<RecipeEnt> recipe = recipeService.findById(1);
        verify(repository, times(1)).findById(1);
        assertTrue(recipe.isPresent());
        assertEquals(RECIPE_ENT.getId(), recipe.get().getId());
    }

    @Test
    public void delete() {
        RecipeService recipeService = new RecipeServiceImpl(repository);
        doNothing().when(repository).deleteById(RECIPE_ENT.getId());
        recipeService.delete(RECIPE_ENT.getId());
        verify(repository, times(1)).deleteById(RECIPE_ENT.getId());
    }
}