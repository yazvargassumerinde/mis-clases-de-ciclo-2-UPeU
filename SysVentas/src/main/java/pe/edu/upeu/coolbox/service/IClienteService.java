package pe.edu.upeu.coolbox.service;

import pe.edu.upeu.coolbox.dto.ComboBoxOption;
import pe.edu.upeu.coolbox.model.Cliente;

import java.util.List;

public interface IClienteService extends ICrudGenericoService<Cliente, String> {
    List<ComboBoxOption> listarTipoDocumento();
}
