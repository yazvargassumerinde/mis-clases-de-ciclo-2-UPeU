package pe.edu.upeu.coolbox.enums;

import lombok.Getter;

@Getter
public enum TipoProducto {
    PRODUCTO("Prducto"),
    PREPARADO("Preparado"),
    SERVICIO("Servicio");

    String descripcion;
    TipoProducto(String descripcion){
        this.descripcion=descripcion;
    }
}
