package br.edu.ifsul.cstsi.tcc_server.api.series;

import jakarta.validation.constraints.NotBlank;

public record SerieDTOPost(
        @NotBlank(message = "Série não pode ser vazia ou nula")
        String name,
        @NotBlank(message = "Sinopse não pode ser vazia")
        String plot,
        @NotBlank(message = "Ano não pode ser vazio")
        Integer year,
        @NotBlank(message = "Imagem não pode ser vazia")
        String image,
        @NotBlank(message = "Imagem poster não pode ser vazia")
        String bigImage,
        @NotBlank(message = "Abertura não pode ser vazia")
        String opening_video
) {
        public SerieDTOPost(Serie serie) {
                this(serie.getName(), serie.getPlot(), serie.getYear(), serie.getImage(), serie.getBigImage(), serie.getOpening_video());
        }
}
