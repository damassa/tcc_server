package br.edu.ifsul.cstsi.tcc_server.api.categories;

public record CategoryDTOGet(
        Long id,
        String name
) {
    public CategoryDTOGet (Category category) {
        this(category.getId(), category.getName());
    }
}
