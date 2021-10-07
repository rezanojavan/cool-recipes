package nl.abnamro.app.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "Recipe")
public class RecipeEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @CreationTimestamp
    private Date creationTime;

    @NotBlank
    @Size(max = 512)
    private String name;

    @NotNull
    private Boolean vegetarian;

    @NotNull
    private Integer serves;

    @NotBlank
    @Size(max = 4000)
    private String ingredients;

    @NotBlank
    @Size(max = 4000)
    private String instruction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeEnt recipeEnt = (RecipeEnt) o;
        return Objects.equals(id, recipeEnt.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
