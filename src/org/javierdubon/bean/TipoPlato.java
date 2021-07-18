
package org.javierdubon.bean;

public class TipoPlato {
    private int codigoTipoPlato;
    private String Descripcion;

    public TipoPlato() {
    }

    public TipoPlato(int codigoTipoPlato, String Descripcion) {
        this.codigoTipoPlato = codigoTipoPlato;
        this.Descripcion = Descripcion;
    }

    public int getCodigoTipoPlato() {
        return codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    
    public String toString(){
        return getCodigoTipoPlato() + " | " + getDescripcion();
    }
}
