package br.edu.ifsul.cstsi.tcc_server.api.categories;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTOPost(
        @NotBlank(message = "Nome da categoria não pode ser vazio")
        String name
) {
    public CategoryDTOPost (Category category) {
        this(category.getName());
    }
}
