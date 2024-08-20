package br.edu.ifsul.cstsi.tcc_server.api.histories;

import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HistoryService {
    @Autowired
    private HistoryRepository rep;

    public History getHistoryById(Long id){
        return rep.findById(id).orElse(null);
    }

    public History insert (History history) {
        Assert.notNull(history.getId(), "Não foi possível inserir o registro.");
        return rep.save(history);
    }

    public History update(History history) {
        Assert.notNull(history.getId(), "Não foi possível atualizar o registro.");
        Optional<History> optional = rep.findById(history.getId());
        if(optional.isPresent()) {
            History db = optional.get();
            db.setPausedAt(history.getPausedAt());
            return rep.save(db);
        } else {
            return null;
        }
    }

    public boolean delete(Long id) {
        Optional<History> optional = rep.findById(id);
        if(optional.isPresent()) {
            rep.delete(optional.get());
            return true;
        } else {
            return false;
        }
    }
}
