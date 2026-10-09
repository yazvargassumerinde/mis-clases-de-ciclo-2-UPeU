package pe.edu.upeu.coolbox.service.impl;
import lombok.RequiredArgsConstructor;
import pe.edu.upeu.coolbox.dto.ComboBoxOption;
import pe.edu.upeu.coolbox.model.UnidMedida;
import pe.edu.upeu.coolbox.repository.ICrudGenericoRepository;
import pe.edu.upeu.coolbox.repository.UnidadMedidaRepository;
import pe.edu.upeu.coolbox.service.IUnidadMedidaService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UnidadMedidaServiceImp extends CrudGenericoServiceImp<UnidMedida, Long>
        implements IUnidadMedidaService {

    private final UnidadMedidaRepository unidadMedidaRepository;

    @Override
    protected ICrudGenericoRepository<UnidMedida, Long> getRepo() {
        return unidadMedidaRepository;
    }
    @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (UnidMedida m : unidadMedidaRepository.findAll()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(m.getIdUnidad()));
            cb.setValue(m.getNombreMedida ());
            listar.add(cb);
        }
        return listar;
    }
}