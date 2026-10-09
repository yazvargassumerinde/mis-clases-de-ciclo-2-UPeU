package pe.edu.upeu.coolbox.service.impl;

import pe.edu.upeu.coolbox.dto.ComboBoxOption;
import pe.edu.upeu.coolbox.model.Categoria;
import pe.edu.upeu.coolbox.repository.CategoriaRepository;
import pe.edu.upeu.coolbox.repository.ICrudGenericoRepository;
import pe.edu.upeu.coolbox.service.ICategoriasService;

import java.util.ArrayList;
import java.util.List;

public class CategoriaServiceImp extends CrudGenericoServiceImp<Categoria,Long> implements ICategoriasService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImp(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    protected ICrudGenericoRepository<Categoria, Long> getRepo() {
        return categoriaRepository;
    }
    @Override
    public List<ComboBoxOption> listarCombobox() {

        List<ComboBoxOption> listar = new ArrayList<>();
        for (Categoria m : categoriaRepository.findAll()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(m.getIdCategoria()));
            cb.setValue(m.getNombre());
            listar.add(cb);
        }
        return listar;
    }
}