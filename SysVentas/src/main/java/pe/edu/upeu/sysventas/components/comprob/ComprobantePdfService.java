package pe.edu.upeu.sysventas.components.comprob;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import pe.edu.upeu.sysventas.dto.comprobante.Comprobante;
import pe.edu.upeu.sysventas.dto.comprobante.ItemComprobante;

import java.awt.*;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Boleta en PDF con el estilo "La noche estrellada" de Coolbox.
 */
public class ComprobantePdfService {
    private static final Color AZUL_NOCHE = new Color(0x0F, 0x1F, 0x47);
    private static final Color AZUL       = new Color(0x1E, 0x3A, 0x8A);
    private static final Color DORADO     = new Color(0xE8, 0xB9, 0x23);
    private static final Color DORADO_OSC = new Color(0xB8, 0x86, 0x0B);
    private static final Color NEGRO      = new Color(0x0A, 0x0D, 0x14);
    private static final Color GRIS       = new Color(0x6B, 0x74, 0x88);
    private static final Color NIEBLA     = new Color(0xF3, 0xF6, 0xFC);
    private static final Color LINEA      = new Color(0xC9, 0xD3, 0xEA);

    private static final Font F_NORMAL  = FontFactory.getFont(FontFactory.HELVETICA, 10, NEGRO);
    private static final Font F_GRIS    = FontFactory.getFont(FontFactory.HELVETICA, 9, GRIS);
    private static final Font F_BOLD    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, NEGRO);
    private static final Font F_AZUL    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, AZUL);
    private static final Font F_TITULO  = FontFactory.getFont(FontFactory.TIMES_BOLD, 20, AZUL_NOCHE);
    private static final Font F_SERIE   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, DORADO_OSC);
    private static final Font F_BLANCO  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
    private static final Font F_TIPO    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
    private static final Font F_SECCION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, AZUL_NOCHE);
    private static final Font F_TOTAL_E = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, DORADO);
    private static final Font F_TOTAL_V = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
    private static final Font F_GRACIAS = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12, AZUL_NOCHE);

    public void exportar(Comprobante boleta, Path destino) throws Exception {
        try (OutputStream os = Files.newOutputStream(destino)) {
            exportar(boleta, os);
        }
    }

    public void exportar(Comprobante boleta, OutputStream os) throws Exception {
        Document doc = new Document(PageSize.A4, 36, 36, 30, 36);
        PdfWriter.getInstance(doc, os);
        doc.open();

        doc.add(franja("/img/vg-boleta.png", 70));
        doc.add(lineaDorada());
        doc.add(espacio(8));
        doc.add(tablaCabecera(boleta));
        doc.add(espacio(10));
        doc.add(tituloSeccion("DATOS DEL CLIENTE"));
        doc.add(tablaCliente(boleta));
        doc.add(espacio(10));
        doc.add(tituloSeccion("DETALLE DE LA COMPRA"));
        doc.add(tablaItems(boleta));
        doc.add(espacio(10));
        doc.add(tablaTotales(boleta));
        doc.add(espacio(14));
        doc.add(pie(boleta));
        doc.add(franja("/img/vg-banner.png", 10));

        doc.close();
    }

    // Pintura de remolinos a todo el ancho
    private PdfPTable franja(String recurso, float alto) {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        PdfPCell c;
        try {
            URL url = getClass().getResource(recurso);
            Image img = Image.getInstance(url);
            c = new PdfPCell(img, true);
        } catch (Exception e) {
            c = new PdfPCell(new Phrase(" "));
            c.setBackgroundColor(AZUL_NOCHE);
        }
        c.setFixedHeight(alto);
        c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(0);
        t.addCell(c);
        return t;
    }

    private PdfPTable lineaDorada() {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        PdfPCell c = new PdfPCell(new Phrase(""));
        c.setFixedHeight(3);
        c.setBackgroundColor(DORADO);
        c.setBorder(Rectangle.NO_BORDER);
        t.addCell(c);
        return t;
    }

    private PdfPTable tablaCabecera(Comprobante b) {
        PdfPTable tabla = new PdfPTable(new float[]{58, 42});
        tabla.setWidthPercentage(100);

        PdfPCell emisor = new PdfPCell();
        emisor.setBorder(Rectangle.NO_BORDER);
        emisor.setPaddingRight(12);
        emisor.addElement(new Paragraph(b.getRazonSocial(), F_TITULO));
        emisor.addElement(new Paragraph("RUC: " + b.getRucEmisor(), F_BOLD));
        emisor.addElement(new Paragraph(b.getDireccion() + " - " + b.getUbigeo(), F_GRIS));
        emisor.addElement(new Paragraph("Tel: " + b.getTelefono(), F_GRIS));

        // Recuadro del documento: banda azul con el tipo y datos debajo
        PdfPTable caja = new PdfPTable(1);
        caja.setWidthPercentage(100);
        PdfPCell tipo = new PdfPCell(new Phrase(b.getTipoDocumento(), F_TIPO));
        tipo.setBackgroundColor(AZUL_NOCHE);
        tipo.setHorizontalAlignment(Element.ALIGN_CENTER);
        tipo.setPadding(6);
        tipo.setBorderColor(DORADO);
        tipo.setBorderWidth(1.5f);
        caja.addCell(tipo);

        PdfPCell datos = new PdfPCell();
        datos.setBorderColor(DORADO);
        datos.setBorderWidth(1.5f);
        datos.setPadding(8);
        agregarCentrado(datos, "R.U.C. " + b.getRucEmisor(), F_BOLD);
        agregarCentrado(datos, b.getSerie() + " - " + b.getNumero(), F_SERIE);
        agregarCentrado(datos, "Fecha: " + b.getFechaEmision() + "  ·  Moneda: " + b.getMoneda(), F_GRIS);
        caja.addCell(datos);

        PdfPCell docCelda = new PdfPCell(caja);
        docCelda.setBorder(Rectangle.NO_BORDER);

        tabla.addCell(emisor);
        tabla.addCell(docCelda);
        return tabla;
    }

    private PdfPTable tablaCliente(Comprobante b) {
        PdfPTable tabla = new PdfPTable(new float[]{20, 80});
        tabla.setWidthPercentage(100);
        agregarFila(tabla, b.getTipoDocCliente(), b.getNroDocCliente());
        agregarFila(tabla, "Cliente", b.getNombreCliente());
        agregarFila(tabla, "Dirección", b.getDireccionCliente());
        return tabla;
    }

    private PdfPTable tablaItems(Comprobante b) {
        PdfPTable tabla = new PdfPTable(new float[]{10, 52, 19, 19});
        tabla.setWidthPercentage(100);
        tabla.setHeaderRows(1);
        tabla.addCell(celdaHeader("CANT.", Element.ALIGN_CENTER));
        tabla.addCell(celdaHeader("DESCRIPCIÓN", Element.ALIGN_LEFT));
        tabla.addCell(celdaHeader("P. UNIT", Element.ALIGN_RIGHT));
        tabla.addCell(celdaHeader("SUBTOTAL", Element.ALIGN_RIGHT));

        for (int i = 0; i < b.getItems().size(); i++) {
            ItemComprobante item = b.getItems().get(i);
            Color bg = (i % 2 == 0) ? Color.WHITE : NIEBLA;
            tabla.addCell(celdaDato(formatoCantidad(item.getCantidad()), Element.ALIGN_CENTER, bg, F_NORMAL));
            tabla.addCell(celdaDato(item.getDescripcion(), Element.ALIGN_LEFT, bg, F_NORMAL));
            tabla.addCell(celdaDato("S/ " + item.getPrecioUnitario(), Element.ALIGN_RIGHT, bg, F_NORMAL));
            tabla.addCell(celdaDato("S/ " + item.getSubtotal(), Element.ALIGN_RIGHT, bg, F_BOLD));
        }
        return tabla;
    }

    private PdfPTable tablaTotales(Comprobante b) {
        PdfPTable tabla = new PdfPTable(new float[]{56, 44});
        tabla.setWidthPercentage(100);

        // Monto en letras
        PdfPCell izq = new PdfPCell();
        izq.setBackgroundColor(NIEBLA);
        izq.setBorder(Rectangle.LEFT);
        izq.setBorderColorLeft(DORADO);
        izq.setBorderWidthLeft(3);
        izq.setPadding(10);
        izq.addElement(new Paragraph("SON:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, DORADO_OSC)));
        izq.addElement(new Paragraph(NumeroALetras.soles(b.getTotal()), F_AZUL));

        PdfPCell sep = new PdfPCell();
        sep.setBorder(Rectangle.NO_BORDER);
        sep.setPaddingLeft(12);
        PdfPTable montos = new PdfPTable(new float[]{55, 45});
        montos.setWidthPercentage(100);
        agregarMonto(montos, "Op. Gravadas", "S/ " + b.getOpGravadas(), false);
        agregarMonto(montos, "IGV (18%)", "S/ " + b.getIgv(), false);
        agregarMonto(montos, "TOTAL", "S/ " + b.getTotal(), true);
        sep.addElement(montos);

        tabla.addCell(izq);
        tabla.addCell(sep);
        return tabla;
    }

    private PdfPTable pie(Comprobante b) {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        PdfPCell c = new PdfPCell();
        c.setBorder(Rectangle.TOP);
        c.setBorderColorTop(LINEA);
        c.setPaddingTop(10);
        c.setPaddingBottom(8);
        Paragraph gracias = new Paragraph("¡Gracias por tu compra en Coolbox!", F_GRACIAS);
        gracias.setAlignment(Element.ALIGN_CENTER);
        Paragraph rep = new Paragraph("Representación impresa de la " + b.getTipoDocumento()
                + "  ·  Generado el " + b.getFechaEmision(), F_GRIS);
        rep.setAlignment(Element.ALIGN_CENTER);
        c.addElement(gracias);
        c.addElement(rep);
        t.addCell(c);
        return t;
    }

    // -- Ayudantes --

    private Paragraph espacio(float alto) {
        Paragraph p = new Paragraph(" ");
        p.setLeading(alto);
        return p;
    }

    private PdfPTable tituloSeccion(String texto) {
        PdfPTable t = new PdfPTable(new float[]{1.2f, 98.8f});
        t.setWidthPercentage(100);
        PdfPCell barra = new PdfPCell(new Phrase(""));
        barra.setBackgroundColor(DORADO);
        barra.setBorder(Rectangle.NO_BORDER);
        PdfPCell txt = new PdfPCell(new Phrase(texto, F_SECCION));
        txt.setBorder(Rectangle.NO_BORDER);
        txt.setPaddingLeft(6);
        txt.setPaddingBottom(4);
        t.addCell(barra);
        t.addCell(txt);
        t.setSpacingAfter(4);
        return t;
    }

    private void agregarCentrado(PdfPCell celda, String texto, Font font) {
        Paragraph p = new Paragraph(texto, font);
        p.setAlignment(Element.ALIGN_CENTER);
        celda.addElement(p);
    }

    private PdfPCell celdaHeader(String texto, int align) {
        PdfPCell c = new PdfPCell(new Phrase(texto, F_BLANCO));
        c.setBackgroundColor(AZUL_NOCHE);
        c.setPadding(6);
        c.setHorizontalAlignment(align);
        c.setBorder(Rectangle.BOTTOM);
        c.setBorderColorBottom(DORADO);
        c.setBorderWidthBottom(2);
        return c;
    }

    private PdfPCell celdaDato(String texto, int align, Color bg, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(texto, font));
        c.setBackgroundColor(bg);
        c.setPadding(5);
        c.setHorizontalAlignment(align);
        c.setBorder(Rectangle.BOTTOM);
        c.setBorderColor(LINEA);
        return c;
    }

    private void agregarFila(PdfPTable tabla, String etiqueta, String valor) {
        PdfPCell e = new PdfPCell(new Phrase(etiqueta, F_AZUL));
        e.setBackgroundColor(NIEBLA);
        e.setPadding(5);
        e.setBorder(Rectangle.NO_BORDER);
        PdfPCell v = new PdfPCell(new Phrase(valor, F_NORMAL));
        v.setBackgroundColor(NIEBLA);
        v.setPadding(5);
        v.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(e);
        tabla.addCell(v);
    }

    private void agregarMonto(PdfPTable tabla, String etiqueta, String valor, boolean esTotal) {
        Font fe = esTotal ? F_TOTAL_E : F_AZUL;
        Font fv = esTotal ? F_TOTAL_V : F_BOLD;
        Color bg = esTotal ? AZUL_NOCHE : NIEBLA;

        PdfPCell lEtiq = new PdfPCell(new Phrase(etiqueta, fe));
        lEtiq.setBackgroundColor(bg);
        lEtiq.setPadding(esTotal ? 7 : 5);
        lEtiq.setBorder(Rectangle.BOTTOM);
        lEtiq.setBorderColor(esTotal ? DORADO : LINEA);

        PdfPCell lVal = new PdfPCell(new Phrase(valor, fv));
        lVal.setBackgroundColor(bg);
        lVal.setPadding(esTotal ? 7 : 5);
        lVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        lVal.setBorder(Rectangle.BOTTOM);
        lVal.setBorderColor(esTotal ? DORADO : LINEA);

        tabla.addCell(lEtiq);
        tabla.addCell(lVal);
    }

    private String formatoCantidad(Double cantidad) {
        if (cantidad == null) return "";
        return cantidad == Math.floor(cantidad) ? String.valueOf(cantidad.longValue()) : String.valueOf(cantidad);
    }
}
