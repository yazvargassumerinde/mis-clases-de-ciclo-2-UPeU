package pe.edu.upeu.coolbox.service.impl;

import lombok.RequiredArgsConstructor;
import pe.edu.upeu.coolbox.dto.ComboBoxOption;
import pe.edu.upeu.coolbox.model.Marca;
import pe.edu.upeu.coolbox.repository.ICrudGenericoRepository;
import pe.edu.upeu.coolbox.repository.MarcaRepository;
import pe.edu.upeu.coolbox.service.IMarcaService;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor

 public class MarcaServiceImp extends CrudGenericoServiceImp<Marca, Long>
        implements IMarcaService {

    private final MarcaRepository marcaRepository;
        @Override
        protected ICrudGenericoRepository<Marca, Long> getRepo () {
            return marcaRepository;
        }
        @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (Marca m : marcaRepository.findAll()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(m.getIdMarca()));
            cb.setValue(m.getNombre());
            listar.add(cb);
        }
        return listar;
    }
    }