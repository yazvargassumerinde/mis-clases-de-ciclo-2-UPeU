package pe.edu.upeu.coolbox.service;

import pe.edu.upeu.coolbox.dto.ComboBoxOption;
import pe.edu.upeu.coolbox.model.UnidMedida;

import java.util.List;

public interface IUnidadMedidaService extends ICrudGenericoService<UnidMedida,Long> {
    List<ComboBoxOption> listarCombobox();
}
