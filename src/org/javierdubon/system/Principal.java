
package org.javierdubon.system;

import java.io.InputStream;
import java.util.HashSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.javierdubon.controller.DatosProgramadorController;
import org.javierdubon.controller.EmpleadoController;
import org.javierdubon.controller.EmpresaController;
import org.javierdubon.controller.MenuPrincipalController;
import org.javierdubon.controller.PlatoController;
import org.javierdubon.controller.PresupuestoController;
import org.javierdubon.controller.ProductoController;
import org.javierdubon.controller.Producto_has_PlatoController;
import org.javierdubon.controller.ServicioController;
import org.javierdubon.controller.Servicio_has_EmpleadoController;
import org.javierdubon.controller.Servicio_has_PlatoController;
import org.javierdubon.controller.TipoEmpleadoController;
import org.javierdubon.controller.TipoPlatoController;


public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/javierdubon/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception{
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal App");
        escenarioPrincipal.getIcons().add(new Image("/org/javierdubon/image/icono.png"));
        menuPrincipal();
        escenarioPrincipal.show();
    }

    public void menuPrincipal(){
        try {
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",492,383);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     public void datosProgramador(){
         try {
             DatosProgramadorController datosProgramador = (DatosProgramadorController) cambiarEscena("DatosProgramadorView.fxml",492,383);
             datosProgramador.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         }
    }
     public void empresa(){
         try{
             EmpresaController empresa = (EmpresaController) cambiarEscena("EmpresaView.fxml",600,475);
             empresa.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         } 
    }
     
     public void presupuesto(){
         try{
             PresupuestoController presupuesto = (PresupuestoController) cambiarEscena("PresupuestoView.fxml",600,475);
             presupuesto.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         }
    }    
     public void tipoEmpleado(){
         try{
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml",600,475);
            tipoEmpleado.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
    }
     
     public void empleado(){
         try{
             EmpleadoController empleado = (EmpleadoController) cambiarEscena("EmpleadoView.fxml",800,500);
             empleado.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void servicio(){
         try{
             ServicioController servicio = (ServicioController) cambiarEscena("ServicioView.fxml",600,475);
             servicio.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void tipoPlato(){
         try{
             TipoPlatoController tipoPlato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml",600,475);
             tipoPlato.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
    
     public void plato(){
         try{
             PlatoController plato = (PlatoController) cambiarEscena("PlatoView.fxml",600,475);
             plato.setEscenarioPrincipal(this); 
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void producto(){
         try{
             ProductoController producto = (ProductoController) cambiarEscena("ProductoView.fxml",600,475);
             producto.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void Servicio_has_Empleado(){
         try{
             Servicio_has_EmpleadoController she = (Servicio_has_EmpleadoController) cambiarEscena("Servicio_has_EmpleadoView.fxml",600,475);
             she.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void Servicio_has_Plato(){
         try{
             Servicio_has_PlatoController shp = (Servicio_has_PlatoController) cambiarEscena("Servicio_has_PlatoView.fxml",600,475);
             shp.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void Producto_has_Plato(){
         try{
             Producto_has_PlatoController php = (Producto_has_PlatoController) cambiarEscena("Producto_has_PlatoView.fxml",600,475);
             php.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
   
    public static void main(String[] args) {
        launch(args);
    }
    public Initializable cambiarEscena (String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;
    }
    
}
