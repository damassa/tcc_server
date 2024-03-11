package br.edu.ifsul.cstsi.tcc_server.api.series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    private SerieRepository rep;

    public List<SerieDTO> getSeries() {
        return rep.findAll().stream().map(SerieDTO::create).collect(Collectors.toList());
    }

    public SerieDTO getSerieById(Long id) {
        Optional<Serie> serie = rep.findById(id);
        return serie.map(SerieDTO::create).orElse(null);
    }

    public List<SerieDTO> getSeriesByName(String name) {
        return rep.findByName(name+"%").stream().map(SerieDTO::create).collect(Collectors.toList());
    }

    public SerieDTO insert(Serie serie) {
        Assert.isNull(serie.getId(),"Não foi possível inserir o registro");

        return SerieDTO.create(rep.save(serie));
    }

    public SerieDTO update(Serie serie, Long id) {
        Assert.notNull(id,"Não foi possível atualizar o registro");

        // Busca o produto no banco de dados
        Optional<Serie> optional = rep.findById(id);
        if(optional.isPresent()) {
            Serie db = optional.get();
            // Copia as propriedades
            db.setName(serie.getName());
            db.setPlot(serie.getPlot());
            db.setDuration(serie.getDuration());
            db.setCategory(serie.getCategory());
            db.setBigImage(serie.getBigImage());
            db.setImage(serie.getImage());
            db.setEpisodes(serie.getEpisodes());
            db.setOpening_video(serie.getOpening_video());
            db.setRatings(serie.getRatings());
            db.setYear(serie.getYear());
            System.out.println("Serie id " + db.getId());

            // Atualiza a série
            rep.save(db);

            return SerieDTO.create(db);
        } else {
            return null;
            //throw new RuntimeException("Não foi possível atualizar o registro");
        }
    }

    public boolean delete(Long id) {
        Optional<Serie> optional = rep.findById(id);
        if(optional.isPresent()) {
            rep.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
