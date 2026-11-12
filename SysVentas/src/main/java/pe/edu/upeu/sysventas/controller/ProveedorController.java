package pe.edu.upeu.sysventas.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import pe.edu.upeu.sysventas.components.ColumnInfo;
import pe.edu.upeu.sysventas.components.TableViewHelper;
import pe.edu.upeu.sysventas.components.Toast;
import pe.edu.upeu.sysventas.components.ToltipCustom;
import pe.edu.upeu.sysventas.dto.ComboBoxOption;
import pe.edu.upeu.sysventas.model.Proveedor;
import pe.edu.upeu.sysventas.service.IProveedorService;

import java.util.*;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ProveedorController {
    private final IProveedorService ps;

    @FXML
    TextField txtDniRuc, txtNombresRaso, txtCelular, txtEmail, txtDireccion, txtFiltroDato;
    @FXML
    ComboBox<ComboBoxOption> cbxTipoDoc;

    @FXML private TableView<Proveedor> tableView;

    @FXML
    Label lbnMsg;
    @FXML private AnchorPane miContenedor;
    Stage stage;

    private Validator validator;
    ObservableList<Proveedor> listarProveedor;
    Proveedor formulario;
    Long idProveedorCE = 0L;

    private final ToltipCustom ttc = new ToltipCustom();

    @FXML
    public void initialize() {
        cbxTipoDoc.getItems().addAll(ps.listarTipoDocumento());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Proveedor> tableViewHelper = new TableViewHelper<>();

        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Prov.", new ColumnInfo("idProveedor", 70.0));
        columns.put("Tipo Doc.", new ColumnInfo("tipoDoc", 80.0));
        columns.put("DNI/RUC", new ColumnInfo("dniruc", 110.0));
        columns.put("Nombre / Razón Social", new ColumnInfo("nombresRaso", 220.0));
        columns.put("Celular", new ColumnInfo("celular", 100.0));
        columns.put("Email", new ColumnInfo("email", 180.0));
        columns.put("Dirección", new ColumnInfo("direccion", 200.0));

        Consumer<Proveedor> updateAction = proveedor -> { editForm(proveedor); };
        Consumer<Proveedor> deleteAction = proveedor -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            ps.delete(proveedor.getIdProveedor());
            double w = stage.getWidth() / 1.5, h = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, w, h);
            clearForm();
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);

        txtFiltroDato.textProperty().addListener((obs, o, n) -> filtrarProveedores(n));
        lbnMsg.setText("");
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarProveedor = FXCollections.observableArrayList(ps.finAll());
            tableView.setItems(listarProveedor);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void filtrarProveedores(String texto) {
        if (texto == null || texto.isBlank()) {
            tableView.setItems(listarProveedor);
            return;
        }
        String t = texto.toLowerCase();
        tableView.setItems(listarProveedor.filtered(p ->
                p.getDniruc().toLowerCase().contains(t) ||
                        p.getNombresRaso().toLowerCase().contains(t)));
    }

    @FXML
    public void validarFormulario() {
        formulario = new Proveedor();
        formulario.setDniruc(txtDniRuc.getText().trim());
        formulario.setNombresRaso(txtNombresRaso.getText().trim());
        formulario.setCelular(txtCelular.getText().trim());
        formulario.setEmail(txtEmail.getText().trim().isEmpty() ? null : txtEmail.getText().trim());
        formulario.setDireccion(txtDireccion.getText().trim());

        String idxTD = cbxTipoDoc.getSelectionModel().getSelectedItem() == null ? ""
                : cbxTipoDoc.getSelectionModel().getSelectedItem().getKey();
        formulario.setTipoDoc(idxTD);

        Set<ConstraintViolation<Proveedor>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Proveedor>> violacionesOrdenadas = violaciones.stream()
                .sorted(Comparator.comparing(v -> v.getPropertyPath().toString())).toList();

        if (violacionesOrdenadas.isEmpty()) {
            procesarFormulario();
        } else {
            mostrarErroresValidacion(violacionesOrdenadas);
        }
    }

    @FXML
    public void clearForm() {
        txtDniRuc.clear();
        txtNombresRaso.clear();
        txtCelular.clear();
        txtEmail.clear();
        txtDireccion.clear();
        cbxTipoDoc.getSelectionModel().clearSelection();
        idProveedorCE = 0L;
        lbnMsg.setText("");
        limpiarError();
    }

    public void editForm(Proveedor proveedor) {
        txtDniRuc.setText(proveedor.getDniruc());
        txtNombresRaso.setText(proveedor.getNombresRaso());
        txtCelular.setText(proveedor.getCelular());
        txtEmail.setText(proveedor.getEmail());
        txtDireccion.setText(proveedor.getDireccion());

        cbxTipoDoc.getSelectionModel().select(
                cbxTipoDoc.getItems().stream()
                        .filter(t -> t.getKey().equals(proveedor.getTipoDoc()))
                        .findFirst().orElse(null));

        idProveedorCE = proveedor.getIdProveedor();
        lbnMsg.setText("Editando proveedor N° " + idProveedorCE);
        lbnMsg.setStyle("-fx-text-fill: #1f3397; -fx-font-size: 14px;");
        limpiarError();
    }

    private void mostrarErroresValidacion(List<ConstraintViolation<Proveedor>> violaciones) {
        limpiarError();
        Map<String, Control> campos = new LinkedHashMap<>();
        campos.put("tipoDoc", cbxTipoDoc);
        campos.put("dniruc", txtDniRuc);
        campos.put("nombresRaso", txtNombresRaso);
        campos.put("celular", txtCelular);
        campos.put("email", txtEmail);

        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        final Control[] primerCtrl = {null};
        for (String campo : campos.keySet()) {
            violaciones.stream()
                    .filter(v -> v.getPropertyPath().toString().equals(campo))
                    .findFirst().ifPresent(v -> {
                        erroresOrdenados.put(campo, v.getMessage());
                        Control c = campos.get(campo);
                        if (c != null) ttc.marcarError(c, v.getMessage().trim());
                        if (primerCtrl[0] == null) primerCtrl[0] = c;
                    });
        }
        if (!erroresOrdenados.isEmpty()) {
            lbnMsg.setText(erroresOrdenados.entrySet().iterator().next().getValue());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            if (primerCtrl[0] != null) Platform.runLater(primerCtrl[0]::requestFocus);
        }
    }

    private void procesarFormulario() {
        stage = (Stage) miContenedor.getScene().getWindow();
        double w = stage.getWidth() / 1.5, h = stage.getHeight() / 2;

        boolean duplicado = ps.finAll().stream().anyMatch(p ->
                p.getDniruc().equals(formulario.getDniruc())
                        && !p.getIdProveedor().equals(idProveedorCE));
        if (duplicado) {
            limpiarError();
            ttc.marcarError(txtDniRuc, "Ya existe un proveedor con ese DNI/RUC");
            lbnMsg.setText("Ya existe un proveedor con ese DNI/RUC");
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            return;
        }

        limpiarError();
        if (idProveedorCE > 0L) {
            formulario.setIdProveedor(idProveedorCE);
            ps.update(idProveedorCE, formulario);
            Toast.showToast(stage, "Se actualizó correctamente!!", 2000, w, h);
        } else {
            ps.save(formulario);
            Toast.showToast(stage, "Se guardó correctamente!!", 2000, w, h);
        }
        clearForm();
        listar();
    }

    public void limpiarError() {
        List.of(cbxTipoDoc, txtDniRuc, txtNombresRaso, txtCelular, txtEmail)
                .forEach(c -> ttc.limpiarCampo(c));
    }
}