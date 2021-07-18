
package org.javierdubon.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.javierdubon.bean.Empresa;
import org.javierdubon.report.GenerarReporte;
import org.javierdubon.system.Principal;


public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void datosProgramador() {
        escenarioPrincipal.datosProgramador();
    }
   
    public void empresa() {
        escenarioPrincipal.empresa();
    }
    
    public void presupuesto() {
        escenarioPrincipal.presupuesto();
    }
    
    public void tipoEmpleado(){
        escenarioPrincipal.tipoEmpleado();
    }
    
    public void empleado(){
        escenarioPrincipal.empleado();
    }
    
    public void servicio(){
        escenarioPrincipal.servicio();
    }
    
    public void tipoPlato(){
        escenarioPrincipal.tipoPlato();
    }
    public void plato(){
        escenarioPrincipal.plato();
    }
    
    public void producto(){
        escenarioPrincipal.producto();
    }
    
    public void Servicio_has_Empleado(){
        escenarioPrincipal.Servicio_has_Empleado();
    }
    
    public void Servicio_has_Plato(){
        escenarioPrincipal.Servicio_has_Plato();
    }
    
    public void Producto_has_Plato(){
        escenarioPrincipal.Producto_has_Plato();
    }
}
