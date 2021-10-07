package nl.abnamro.app.controller;

import com.google.gson.Gson;
import nl.abnamro.app.dto.Recipe;
import nl.abnamro.app.model.RecipeEnt;
import nl.abnamro.app.repository.RecipeRepository;
import nl.abnamro.app.service.RecipeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeApiControllerImpl.class)
public class RecipeApiControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeRepository repository;

    @MockBean
    private RecipeService recipeService;

    private final static RecipeEnt RECIPE_ENT = new RecipeEnt();

    private static Gson gson = new Gson();

    @BeforeAll
    public static void setup() {
        RECIPE_ENT.setId(1);
        RECIPE_ENT.setCreationTime(new Date());
        RECIPE_ENT.setName("Baked Potato");
        RECIPE_ENT.setVegetarian(true);
        RECIPE_ENT.setServes(4);
        RECIPE_ENT.setIngredients("potato 4pcs,Water 1L");
        RECIPE_ENT.setInstruction("Boil the potato");
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    public void findById() throws Exception {
        Mockito.doReturn(Optional.of(RECIPE_ENT)).when(recipeService).findById(any(Integer.class));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/rest/recipe/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(RECIPE_ENT.getName())));
    }

    private Recipe getNewRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName(RECIPE_ENT.getName());
        recipe.setServes(RECIPE_ENT.getServes());
        recipe.setVegetarian(RECIPE_ENT.getVegetarian());
        recipe.setIngredients(List.of("Potato 4pcs"));
        recipe.setInstruction(RECIPE_ENT.getInstruction());
        return recipe;
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    public void create() throws Exception {
        Mockito.doReturn(RECIPE_ENT).when(recipeService).create(any(RecipeEnt.class));
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/recipe")
                .content(gson.toJson(getNewRecipe()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(RECIPE_ENT.getId())));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    public void update() throws Exception {
        Mockito.doReturn(RECIPE_ENT).when(recipeService)
                .update(any(Integer.class), any(RecipeEnt.class));
        Recipe recipe = getNewRecipe();
        String updatedName = RECIPE_ENT.getName() + "[Edited]";
        recipe.setName(updatedName);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/rest/recipe/1")
                .content(gson.toJson(recipe))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    public void find() throws Exception {
        Mockito.doReturn(List.of(RECIPE_ENT)).when(recipeService)
                .find(null, Boolean.TRUE, null, null, null);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/rest/recipe?vegetarian=true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(RECIPE_ENT.getId())));
    }
}