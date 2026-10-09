package pe.edu.upeu.coolbox.repository;

import pe.edu.upeu.coolbox.enums.TipoDocumento;
import pe.edu.upeu.coolbox.model.Cliente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

// El ID del cliente es su DNI/RUC (un String), por eso ID = String y no Long.
public class ClienteRepository extends AbstractJpaRepository<Cliente, String> {
    @Override
    protected String getTableName() {
        return "";
    }

    @Override
    protected String getPkColumn() {
        return "";
    }

    @Override
    protected Cliente insert(Connection connection, Cliente entity) throws SQLException {
        return null;
    }

    @Override
    protected Cliente updateRow(Connection connection, Cliente entity) throws SQLException {
        return null;
    }

    @Override
    protected Cliente mapRow(ResultSet rs) throws SQLException {
        return null;
    }
}
