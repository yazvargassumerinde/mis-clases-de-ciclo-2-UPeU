package pe.edu.upeu.coolbox.service.impl;

import lombok.RequiredArgsConstructor;

import pe.edu.upeu.coolbox.dto.ComboBoxOption;
import pe.edu.upeu.coolbox.enums.TipoProducto;
import pe.edu.upeu.coolbox.model.Producto;
import pe.edu.upeu.coolbox.repository.ICrudGenericoRepository;
import pe.edu.upeu.coolbox.repository.ProductoRepository;
import pe.edu.upeu.coolbox.service.IProductoService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductoServiceImp extends CrudGenericoServiceImp<Producto, Long> implements IProductoService {

    private final ProductoRepository productoRepository;

    @Override
    protected ICrudGenericoRepository<Producto, Long> getRepo() {
        return productoRepository;
    }
    @Override
    public List<ComboBoxOption> listarTipoProducto() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (TipoProducto tp : TipoProducto.values()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(String.valueOf(tp.name()));
            cb.setValue(tp.getDescripcion());
            listar.add(cb);
        }
        return listar;
    }

    @Override
    public List<Producto> finAll() {

        return super.finAll();
    }
}