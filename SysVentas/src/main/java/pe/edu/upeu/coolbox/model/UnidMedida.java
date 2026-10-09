package pe.edu.upeu.coolbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidMedida {

    private Long idUnidad;
    private String nombreMedida;
}