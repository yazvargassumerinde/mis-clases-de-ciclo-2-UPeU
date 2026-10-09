package pe.edu.upeu.coolbox.service;

import pe.edu.upeu.coolbox.dto.ComboBoxOption;
import pe.edu.upeu.coolbox.model.Categoria;

import java.util.List;

public interface ICategoriasService extends ICrudGenericoService<Categoria, Long> {
    List<ComboBoxOption> listarCombobox();

}
