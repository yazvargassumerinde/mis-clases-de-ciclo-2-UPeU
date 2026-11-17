package pe.edu.upeu.sysventas.components.comprob;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pe.edu.upeu.sysventas.dto.comprobante.Comprobante;
import pe.edu.upeu.sysventas.dto.comprobante.ItemComprobante;

import java.net.URL;

/**
 * Boleta en pantalla con el estilo "La noche estrellada" de Coolbox:
 * azul noche, amarillo dorado, blanco y negro.
 */
public class ComprobanteView {

    private static final Color AZUL_NOCHE = Color.web("#0F1F47");
    private static final Color AZUL       = Color.web("#1E3A8A");
    private static final Color DORADO     = Color.web("#E8B923");
    private static final Color DORADO_OSC = Color.web("#B8860B");
    private static final Color NEGRO      = Color.web("#0A0D14");
    private static final Color GRIS       = Color.web("#6B7488");
    private static final Color NIEBLA     = Color.web("#F3F6FC");
    private static final Color LINEA      = Color.web("#C9D3EA");

    private static final double ANCHO = 702;   // 750 - 2 * 24 de margen

    private static final Font F_NORMAL  = Font.font("Segoe UI", 11);
    private static final Font F_BOLD    = Font.font("Segoe UI", FontWeight.BOLD, 11);
    private static final Font F_TITULO  = Font.font("Georgia", FontWeight.BOLD, 22);
    private static final Font F_DOC     = Font.font("Segoe UI", FontWeight.BOLD, 14);
    private static final Font F_SECCION = Font.font("Segoe UI", FontWeight.BOLD, 10);
    private static final Font F_GRACIAS = Font.font("Georgia", FontPosture.ITALIC, 13);

    private final Comprobante boleta;

    public ComprobanteView(Comprobante boleta) {
        this.boleta = boleta;
    }

    public VBox construir() {
        VBox pagina = new VBox(14);
        pagina.setStyle("-fx-background-color: white; -fx-border-color: #0F1F47; -fx-border-width: 1;");
        pagina.setPadding(new Insets(0, 24, 20, 24));
        pagina.setMaxWidth(750);
        pagina.setMinWidth(750);

        pagina.getChildren().addAll(
            franjaNocheEstrellada(96),
            seccionCabecera(),
            seccionCliente(),
            seccionItems(),
            seccionTotales(),
            seccionPie()
        );
        return pagina;
    }

    // Pintura de remolinos arriba de la boleta
    private Region franjaNocheEstrellada(double alto) {
        StackPane franja = new StackPane();
        franja.setPrefSize(ANCHO, alto);
        franja.setMaxSize(ANCHO, alto);
        URL url = getClass().getResource("/img/vg-boleta.png");
        if (url != null) {
            ImageView iv = new ImageView(new Image(url.toExternalForm()));
            iv.setFitWidth(ANCHO);
            iv.setFitHeight(alto);
            iv.setPreserveRatio(false);
            franja.getChildren().add(iv);
        } else {
            franja.setStyle("-fx-background-color: #0F1F47;");
        }
        Rectangle lineaDorada = new Rectangle(ANCHO, 3, DORADO);
        StackPane.setAlignment(lineaDorada, Pos.BOTTOM_CENTER);
        franja.getChildren().add(lineaDorada);
        VBox.setMargin(franja, new Insets(24, 0, 0, 0));
        return franja;
    }

    private HBox seccionCabecera() {
        Label nombre = lbl(boleta.getRazonSocial(), F_TITULO, AZUL_NOCHE);
        nombre.setTextAlignment(TextAlignment.LEFT);
        VBox emisor = new VBox(3,
            nombre,
            lbl("RUC: " + boleta.getRucEmisor(), F_BOLD, NEGRO),
            lbl(boleta.getDireccion() + " - " + boleta.getUbigeo(), F_NORMAL, GRIS),
            lbl("Tel: " + boleta.getTelefono(), F_NORMAL, GRIS)
        );
        emisor.setAlignment(Pos.CENTER_LEFT);
        emisor.setPrefWidth(410);

        Label tipo = lbl(boleta.getTipoDocumento(), F_DOC, Color.WHITE);
        HBox bandaTipo = new HBox(tipo);
        bandaTipo.setAlignment(Pos.CENTER);
        bandaTipo.setPadding(new Insets(6));
        bandaTipo.setBackground(fondo(AZUL_NOCHE, 6, 6, 0, 0));

        VBox datosDoc = new VBox(3,
            lbl("R.U.C. " + boleta.getRucEmisor(), F_BOLD, NEGRO),
            lbl(boleta.getSerie() + " - " + boleta.getNumero(), Font.font("Segoe UI", FontWeight.BOLD, 16), DORADO_OSC),
            lbl("Fecha: " + boleta.getFechaEmision() + "  ·  Moneda: " + boleta.getMoneda(), F_NORMAL, GRIS)
        );
        datosDoc.setAlignment(Pos.CENTER);
        datosDoc.setPadding(new Insets(8));

        VBox docBox = new VBox(bandaTipo, datosDoc);
        docBox.setPrefWidth(276);
        docBox.setBorder(new Border(new BorderStroke(DORADO, BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2))));
        docBox.setBackground(fondo(Color.WHITE, 8, 8, 8, 8));

        return new HBox(16, emisor, docBox);
    }

    private VBox seccionCliente() {
        GridPane grid = new GridPane();
        grid.setBackground(fondo(NIEBLA, 8, 8, 8, 8));
        grid.setPadding(new Insets(6, 4, 6, 4));
        agregarFilaGrid(grid, 0, boleta.getTipoDocCliente(), boleta.getNroDocCliente());
        agregarFilaGrid(grid, 1, "Cliente", boleta.getNombreCliente());
        agregarFilaGrid(grid, 2, "Dirección", boleta.getDireccionCliente());

        ColumnConstraints col1 = new ColumnConstraints(110);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col1, col2);

        return new VBox(6, tituloSeccion("DATOS DEL CLIENTE"), grid);
    }

    private VBox seccionItems() {
        GridPane header = new GridPane();
        header.setBackground(fondo(AZUL_NOCHE, 6, 6, 0, 0));
        header.setBorder(new Border(new BorderStroke(DORADO, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2, 0))));
        header.setPadding(new Insets(6, 8, 6, 8));
        header.addRow(0,
            celda("CANT.", Color.WHITE, F_BOLD, 60, Pos.CENTER),
            celda("DESCRIPCIÓN", Color.WHITE, F_BOLD, 386, Pos.CENTER_LEFT),
            celda("P. UNIT", Color.WHITE, F_BOLD, 120, Pos.CENTER_RIGHT),
            celda("SUBTOTAL", Color.WHITE, F_BOLD, 120, Pos.CENTER_RIGHT)
        );

        VBox filas = new VBox(0);
        filas.setBorder(new Border(new BorderStroke(LINEA, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 1, 1, 1))));
        for (int i = 0; i < boleta.getItems().size(); i++) {
            ItemComprobante item = boleta.getItems().get(i);
            GridPane fila = new GridPane();
            fila.setPadding(new Insets(6, 8, 6, 8));
            fila.setBackground(fondo(i % 2 == 0 ? Color.WHITE : NIEBLA, 0, 0, 0, 0));
            fila.addRow(0,
                celda(formatoCantidad(item.getCantidad()), NEGRO, F_NORMAL, 60, Pos.CENTER),
                celda(item.getDescripcion(), NEGRO, F_NORMAL, 386, Pos.CENTER_LEFT),
                celda("S/ " + item.getPrecioUnitario(), NEGRO, F_NORMAL, 120, Pos.CENTER_RIGHT),
                celda("S/ " + item.getSubtotal(), NEGRO, F_BOLD, 120, Pos.CENTER_RIGHT)
            );
            filas.getChildren().add(fila);
        }
        return new VBox(6, tituloSeccion("DETALLE DE LA COMPRA"), new VBox(header, filas));
    }

    private HBox seccionTotales() {
        Label letras = lbl(NumeroALetras.soles(boleta.getTotal()), F_BOLD, AZUL_NOCHE);
        letras.setTextAlignment(TextAlignment.LEFT);
        VBox izq = new VBox(4, lbl("SON:", F_SECCION, DORADO_OSC), letras);
        izq.setPadding(new Insets(10));
        izq.setPrefWidth(380);
        izq.setBackground(fondo(NIEBLA, 8, 8, 8, 8));
        izq.setBorder(new Border(new BorderStroke(DORADO, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 3))));

        VBox der = new VBox(
            filaTotal("Op. Gravadas", boleta.getOpGravadas().toString(), false),
            filaTotal("IGV (18%)", boleta.getIgv().toString(), false),
            filaTotal("TOTAL", boleta.getTotal().toString(), true)
        );
        der.setPrefWidth(306);
        der.setAlignment(Pos.TOP_RIGHT);

        return new HBox(16, izq, der);
    }

    private VBox seccionPie() {
        Region franja = franjaPie();
        VBox pie = new VBox(4,
            lbl("¡Gracias por tu compra en Coolbox!", F_GRACIAS, AZUL_NOCHE),
            lbl("Representación impresa de la " + boleta.getTipoDocumento() + "  ·  Generado el " + boleta.getFechaEmision(), F_NORMAL, GRIS),
            franja
        );
        pie.setAlignment(Pos.CENTER);
        pie.setPadding(new Insets(10, 0, 0, 0));
        pie.setBorder(new Border(new BorderStroke(LINEA, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, new BorderWidths(1, 0, 0, 0))));
        return pie;
    }

    // Franja fina de remolinos al final
    private Region franjaPie() {
        StackPane franja = new StackPane();
        franja.setPrefSize(ANCHO, 14);
        franja.setMaxSize(ANCHO, 14);
        URL url = getClass().getResource("/img/vg-banner.png");
        if (url != null) {
            ImageView iv = new ImageView(new Image(url.toExternalForm()));
            iv.setFitWidth(ANCHO);
            iv.setFitHeight(14);
            iv.setPreserveRatio(false);
            franja.getChildren().add(iv);
        } else {
            franja.setStyle("-fx-background-color: #0F1F47;");
        }
        VBox.setMargin(franja, new Insets(8, 0, 0, 0));
        return franja;
    }

    // -- Componentes reutilizables --

    private HBox tituloSeccion(String texto) {
        Rectangle punto = new Rectangle(4, 14, DORADO);
        HBox t = new HBox(6, punto, lbl(texto, F_SECCION, AZUL_NOCHE));
        t.setAlignment(Pos.CENTER_LEFT);
        return t;
    }

    private Label lbl(String texto, Font font, Color color) {
        Label l = new Label(texto == null ? "" : texto);
        l.setFont(font);
        l.setTextFill(color);
        l.setWrapText(true);
        l.setTextAlignment(TextAlignment.CENTER);
        return l;
    }

    private Label celda(String texto, Color color, Font font, double width, Pos pos) {
        Label l = lbl(texto, font, color);
        l.setPrefWidth(width);
        l.setAlignment(pos);
        return l;
    }

    private void agregarFilaGrid(GridPane grid, int row, String etiqueta, String valor) {
        Label lEtiqueta = lbl(etiqueta, F_BOLD, AZUL);
        lEtiqueta.setPadding(new Insets(3, 8, 3, 8));
        Label lValor = lbl(valor, F_NORMAL, NEGRO);
        lValor.setTextAlignment(TextAlignment.LEFT);
        lValor.setPadding(new Insets(3, 8, 3, 8));
        grid.addRow(row, lEtiqueta, lValor);
    }

    private HBox filaTotal(String etiqueta, String valor, boolean esTotal) {
        Font f = esTotal ? F_DOC : F_BOLD;
        Label lEtiq = lbl(etiqueta, f, esTotal ? DORADO : AZUL_NOCHE);
        lEtiq.setPrefWidth(170);
        lEtiq.setAlignment(Pos.CENTER_LEFT);
        lEtiq.setPadding(new Insets(5, 10, 5, 10));
        Label lVal = lbl("S/ " + valor, f, esTotal ? Color.WHITE : NEGRO);
        lVal.setPrefWidth(136);
        lVal.setAlignment(Pos.CENTER_RIGHT);
        lVal.setPadding(new Insets(5, 10, 5, 10));

        HBox fila = new HBox(lEtiq, lVal);
        fila.setBackground(esTotal ? fondo(AZUL_NOCHE, 0, 0, 6, 6) : fondo(NIEBLA, 0, 0, 0, 0));
        if (!esTotal) {
            fila.setBorder(new Border(new BorderStroke(LINEA, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0))));
        }
        return fila;
    }

    private String formatoCantidad(Double cantidad) {
        if (cantidad == null) return "";
        return cantidad == Math.floor(cantidad) ? String.valueOf(cantidad.longValue()) : String.valueOf(cantidad);
    }

    // Fondo con esquinas: arriba-izq, arriba-der, abajo-der, abajo-izq
    private Background fondo(Color color, double ai, double ad, double abd, double abi) {
        return new Background(new BackgroundFill(color, new CornerRadii(ai, ad, abd, abi, false), Insets.EMPTY));
    }
}
