package br.edu.ifsul.cstsi.tcc_server.api.categories;

import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository rep;

    // Retorna todas as categorias (entidades)
    public List<Category> getCategories() {
        return rep.findAll();
    }

    // Retorna categoria por ID
    public Optional<Category> getCategoryById(Long id) {
        return rep.findById(id);
    }

    // Retorna lista filtrada por nome
    public List<Category> getCategoriesByName(String name) {
        return rep.findByName("%" + name + "%");
    }

    // Inserção via entidade
    public Category insert(Category category) {
        Assert.isNull(category.getId(), "Não foi possível inserir o registro.");
        return rep.save(category);
    }

    // Atualização via entidade
    public Category update(Category category, Long id) {
        Assert.notNull(id, "Não foi possível atualizar o registro.");
        Optional<Category> optional = rep.findById(id);
        if (optional.isPresent()) {
            Category db = optional.get();
            db.setName(category.getName());
            return rep.save(db);
        }
        return null;
    }

    // Delete
    public boolean delete(Long id) {
        Optional<Category> optional = rep.findById(id);
        if (optional.isPresent()) {
            rep.deleteById(id);
            return true;
        }
        return false;
    }
}
