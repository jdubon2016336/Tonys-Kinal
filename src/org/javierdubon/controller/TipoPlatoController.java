
package org.javierdubon.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.javierdubon.bean.TipoPlato;
import org.javierdubon.db.Conexion;
import org.javierdubon.system.Principal;

public class TipoPlatoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, ACTUALIZAR, CANCELAR, ELIMINAR, NINGUNO, EDITAR, GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
     cargarDatos();  
     btnNuevo.getStylesheets().add("/org/javierdubon/resource/boton.css");
     btnEditar.getStylesheets().add("/org/javierdubon/resource/boton.css");
     btnEliminar.getStylesheets().add("/org/javierdubon/resource/boton.css");
     btnReporte.getStylesheets().add("/org/javierdubon/resource/boton.css");
     tblTipoPlato.getStylesheets().add("/org/javierdubon/resource/TableView.css");
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setDisable(false);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setDisable(true);
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    public void guardar(){
        TipoPlato registro = new TipoPlato();
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
            if (registro.getDescripcion() == null){
                JOptionPane.showMessageDialog(null,"El apartado de Descripcion está vacio");
            }else{
                procedimiento.setString(1, registro.getDescripcion());
                procedimiento.execute();
                listaTipoPlato.add(registro);
                cargarDatos();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void seleccionar(){
        if (tblTipoPlato.getSelectionModel().getSelectedItem() != null){
        txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        txtDescripcion.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcion());
        } else{
        
        }
    }
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoPlato(
                                        registro.getInt("codigoTipoPlato"),
                                        registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
            break;
            default:
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro que quiere eliminar el registro? se eliminaran los platos enlazados con el registro","Eliminar",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                        procedimiento.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                        procedimiento.execute();
                        listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                  }
                }else{
                        JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento primero");
                }
            break;    
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento primero");
                }
            break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;    
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoPlato(?,?)}");
            TipoPlato registro = (TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
            desactivarControles();
            limpiarControles();
            btnEditar.setText("Editar");
            btnNuevo.setDisable(false);
            btnReporte.setText("Reporte");
            btnEliminar.setDisable(false);
            btnEditar.setDisable(false);
            btnReporte.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;
        }
    }
    
    public void cargarDatos(){
        tblTipoPlato.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcion"));
    }
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                        resultado.getString("descripcion") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void plato(){
        escenarioPrincipal.plato();
    }
    
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoTipoPlato.setText("");
        txtDescripcion.setText("");
    }
}
