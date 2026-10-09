package pe.edu.upeu.coolbox.service;

import pe.edu.upeu.coolbox.dto.ComboBoxOption;
import pe.edu.upeu.coolbox.model.Marca;

import java.util.List;

public interface IMarcaService extends ICrudGenericoService<Marca, Long> {
    List<ComboBoxOption> listarCombobox();
}
