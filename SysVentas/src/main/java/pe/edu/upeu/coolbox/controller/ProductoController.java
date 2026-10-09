package pe.edu.upeu.coolbox.controller;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import pe.edu.upeu.coolbox.components.ColumnInfo;
import pe.edu.upeu.coolbox.components.TableViewHelper;
import pe.edu.upeu.coolbox.components.Toast;
import pe.edu.upeu.coolbox.components.ToltipCustom;
import pe.edu.upeu.coolbox.dto.ComboBoxOption;
import pe.edu.upeu.coolbox.model.Producto;
import pe.edu.upeu.coolbox.service.ICategoriasService;
import pe.edu.upeu.coolbox.service.IMarcaService;
import pe.edu.upeu.coolbox.service.IProductoService;
import pe.edu.upeu.coolbox.service.IUnidadMedidaService;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ProductoController {
    private final IMarcaService ms;
    private final ICategoriasService cs;
    private final IProductoService ps;
    private final IUnidadMedidaService us;

    @FXML
    TextField txtNombreProducto, txtPUnit,
            txtPUnitOld, txtUtilidad, txtStock, txtStockOld, txtFiltroDato;
    @FXML
    ComboBox<ComboBoxOption> cbxTipoProducto;
    @FXML
    ComboBox<ComboBoxOption> cbxMarca;
    @FXML
    ComboBox<ComboBoxOption> cbxCategoria;
    @FXML
    ComboBox<ComboBoxOption> cbxUnidMedida;

    @FXML
    private TableView<Producto> tableView;
    @FXML
    Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;
    Stage stage;
    private Validator validator;
    ObservableList<Producto> listarProducto;
    Producto formulario;
    Long idProductoCE = 0L;

    private final ToltipCustom ttc = new ToltipCustom();

    @FXML
    public void initialize() {
        System.out.println("Holas");
        cbxTipoProducto.getItems().addAll(ps.listarTipoProducto());
        cbxMarca.getItems().addAll(ms.listarCombobox());
        cbxCategoria.getItems().addAll(cs.listarCombobox());
        cbxUnidMedida.getItems().addAll(us.listarCombobox());
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Producto> tableViewHelper = new TableViewHelper<>();

        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Pro.", new ColumnInfo("idProducto", 60.0));
        columns.put("Tipo Producto", new ColumnInfo("tipoProducto", 150.0));
        columns.put("Nombre Producto", new ColumnInfo("nombre", 200.0));
        columns.put("P. Unitario", new ColumnInfo("pu", 150.0));
        columns.put("Utilidad", new ColumnInfo("utilidad", 100.0));
        columns.put("Marca", new ColumnInfo("idMarca.nombre", 200.0));
        columns.put("Categoria", new ColumnInfo("idCategoria.nombre", 200.0));

        Consumer<Producto> updateAction = producto -> {/*editForm(producto);*/};
            Consumer<Producto> deleteAction = producto -> {
                ps.delete(producto.getIdProducto());
                double w = stage.getWidth() / 1.5, h = stage.getHeight() / 2;
                Toast.showToast(stage, "Se eliminó correctamente!!", 2000, w, h);
                listar();
            };
            tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
            tableView.setTableMenuButtonVisible(true);
            listar();

        }
    public void listar() {
        try {
            tableView.getItems().clear();
            listarProducto = FXCollections.observableArrayList (ps.finAll());
            tableView.getItems().addAll(listarProducto);
            //txtFiltroDato.textProperty().addListener((obs, o, n) -> filtrarProductos(n));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    }
