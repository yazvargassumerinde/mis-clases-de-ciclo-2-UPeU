package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.enums.TipoDocumento;
import pe.edu.upeu.sysventas.model.Cliente;

// El ID del cliente es su DNI/RUC (un String), por eso ID = String y no Long.
public class ClienteRepository extends AbstractJpaRepository<Cliente, String> {

    @Override
    protected String getId(Cliente entity) {
        return entity.getDniruc();
    }

    @Override
    protected void setId(Cliente entity, String id) {
        entity.setDniruc(id);
    }

    @Override
    protected String generateId() {
        // No se genera automáticamente: el DNI/RUC lo escribe el usuario.
        throw new UnsupportedOperationException("El DNI/RUC lo define el usuario");
    }

    // Datos de ejemplo para que la tabla no aparezca vacía al abrir la pantalla
    public void seedData() {
        if (findAll().isEmpty()) {
            //ejemplos profe
            save(new Cliente("12345678", "Juan Pérez Quispe", TipoDocumento.DNI, "", "Av. Ejército 123, Arequipa"));
            save(new Cliente("87654321", "María López Huamán", TipoDocumento.DNI, "", "Calle Mercaderes 456, Arequipa"));
            save(new Cliente("20123456789", "Comercial Andina S.A.C.", TipoDocumento.RUC, "Carlos Ramos", "Av. Parra 789, Arequipa"));
        }
    }
}
