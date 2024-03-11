package br.edu.ifsul.cstsi.tcc_server.api.series;

import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class SerieDTO {
    private Long id;
    private String name;
    private String plot;
    private int year;
    private String image;
    private String bigImage;
    private String opening_video;
    private int duration;

    public static SerieDTO create(Serie s) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(s, SerieDTO.class);
    }
}
