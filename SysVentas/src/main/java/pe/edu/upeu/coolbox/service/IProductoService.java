package pe.edu.upeu.coolbox.service;

import pe.edu.upeu.coolbox.dto.ComboBoxOption;
import pe.edu.upeu.coolbox.model.Producto;

import java.util.List;

public interface IProductoService extends ICrudGenericoService<Producto,Long> {
    List<ComboBoxOption> listarTipoProducto();
}
