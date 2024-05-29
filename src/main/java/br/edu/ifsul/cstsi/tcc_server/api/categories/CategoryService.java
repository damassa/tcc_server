package br.edu.ifsul.cstsi.tcc_server.api.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository rep;

    public List<Category> getCategories() {
        return rep.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return rep.findById(id);
    }

    public List<Category> getCategoriesByName(String name) {
        return rep.findByName("%"+name+"%");
    }

    public Category insert (Category category) {
        Assert.isNull(category.getId(), "Não foi possível inserir o registro.");
        return rep.save(category);
    }

    public Category update(Category category, Long id) {
        Assert.notNull(id, "Não foi possível atualizar o registro.");
        Optional<Category> optional = rep.findById(id);
        if(optional.isPresent()) {
            Category db = optional.get();
            db.setName(category.getName());
            System.out.println("Categoria id " + db.getId());
            return rep.save(db);
        } else {
            return null;
        }
    }

    public boolean delete(Long id) {
        Optional<Category> optional = rep.findById(id);
        if(optional.isPresent()) {
            rep.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
