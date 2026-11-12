package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import pe.edu.upeu.sysventas.dto.ComboBoxOption;
import pe.edu.upeu.sysventas.enums.TipoDocumento;
import pe.edu.upeu.sysventas.model.Proveedor;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.repository.ProveedorRepository;
import pe.edu.upeu.sysventas.service.IProveedorService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProveedorServiceImp extends CrudGenericoServiceImp<Proveedor, Long> implements IProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Override
    protected ICrudGenericoRepository<Proveedor, Long> getRepo() {
        return proveedorRepository;
    }
    @Override
    public List<ComboBoxOption> listarTipoDocumento() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (TipoDocumento td : TipoDocumento.values()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(td.name());
            cb.setValue(td.name());
            listar.add(cb);
        }
        return listar;
    }
}
