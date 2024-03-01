package br.edu.ifsul.cstsi.tcc_server.api.categories;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "Category")
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Serie> series; //1 categoria pertence a várias séries
}
