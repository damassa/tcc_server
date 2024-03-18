package br.edu.ifsul.cstsi.tcc_server.api.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT c FROM Category c where c.name like ?1")
    List<Category> findByName(String name);
}
