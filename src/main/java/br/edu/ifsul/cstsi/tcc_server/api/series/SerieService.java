package br.edu.ifsul.cstsi.tcc_server.api.series;

import br.edu.ifsul.cstsi.tcc_server.api.categories.Category;
import br.edu.ifsul.cstsi.tcc_server.api.categories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
@Service
public class SerieService { // TODO: Não tem que botar DTO aqui?
    private final SerieRepository serieRepository;

    public SerieService (SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public List<SerieDTOResponse> getAllSeries() {
        return serieRepository.findAll().stream().peek(s -> {
            if(s.getCategory() != null) s.getCategory().getName();
            if(s.getEpisodes() != null) s.getEpisodes().size();
        }).map(SerieDTOResponse::new).toList();
    }

    @Transactional
    public Page<Serie> getSeries(Pageable pagination) {
        return serieRepository.findAll(pagination);
    }

//    public Optional<Serie> getSerieById(Long id) {
//        return serieRepository.findById(id);
//    }

    @Transactional
    public SerieDTOResponse getSerieById(Long id) {
        Serie serie = serieRepository.findById(id).orElseThrow(() -> new RuntimeException("Série não encontrada"));

        if (serie.getCategory() != null) serie.getCategory().getName();
        if (serie.getEpisodes() != null) serie.getEpisodes().size();

        return new SerieDTOResponse(serie);
    }

    public List<Serie> getSeriesByName(String name) {
        return serieRepository.findByName("%"+name+"%");
    }

    public List<Serie> getSeriesOrderedByYear(boolean asc) {
        Sort sort = asc ? Sort.by("year").ascending() : Sort.by("year").descending();
        return serieRepository.findAll(sort);
    }

    public List<Serie> getSeriesByCategoryId(Long id) {
        return serieRepository.getSeriesByCategoryId(id);
    }

    @Transactional
    public void toggleFavorite(Long id_serie, Long id_user) {
        serieRepository.insertFavorite(id_serie, id_user);
    }

    public Integer getFavoritesBySerieId(Long id_serie, Long id_user) {
        return serieRepository.countSeriesByIds(id_serie, id_user);
    }

    public void removeFromFavorites(Long id_serie, Long id_user) {
        serieRepository.removeFromFavorites(id_serie, id_user);
    }

    @Transactional
    public Serie insert(SerieDTOPost serie) {
//        Assert.isNull(serie.getId(),"Não foi possível inserir o registro");

        Category category = categoryRepository.findById(serie.categoryId()).orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Serie s = new Serie();
        s.setName(serie.name());
        s.setPlot(serie.plot());
        s.setYear(serie.year());
        s.setImage(serie.image());
        s.setBigImage(serie.bigImage());
        s.setOpening_video(serie.opening_video());
        s.setCategory(category);

        return serieRepository.save(s);
    }

    @Transactional
    public SerieDTOResponse update(Long id, SerieDTOPut dto) {
        Serie db = serieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Série não encontrada"));

        db.setName(dto.name());
        db.setPlot(dto.plot());
        db.setYear(dto.year());
        db.setImage(dto.image());
        db.setBigImage(dto.bigImage());
        db.setOpening_video(dto.opening_video());

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        db.setCategory(category);

        // Salva e força carregamento de lazy collections
        Serie saved = serieRepository.save(db);

        // Força carregamento dos dados para evitar LazyInitializationException
        if (saved.getCategory() != null) saved.getCategory().getName();
        if (saved.getEpisodes() != null) saved.getEpisodes().size();

        return new SerieDTOResponse(saved);
    }



    public boolean delete(Long id) {
        Optional<Serie> optional = serieRepository.findById(id);
        if(optional.isPresent()) {
            serieRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
