
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.javierdubon.bean.Plato;
import org.javierdubon.bean.Servicio;
import org.javierdubon.bean.Servicio_has_Plato;
import org.javierdubon.db.Conexion;
import org.javierdubon.system.Principal;

public class Servicio_has_PlatoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, ELIMINAR, AGREGAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<Servicio_has_Plato> listaSHP;
    ObservableList<Servicio> listaServicio;
    ObservableList<Plato> listaPlato;
    
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblServicio_has_Plato;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoPlato;
    @FXML private Button btnEliminar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        limpiarControles();
        cargarDatos();
        btnEliminar.getStylesheets().add("/org/javierdubon/resource/boton.css");
        tblServicio_has_Plato.getStylesheets().add("/org/javierdubon/resource/TableView.css");
    }
    
    public void seleccionar(){
        if(tblServicio_has_Plato.getSelectionModel().getSelectedItem() != null){
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicio_has_Plato)tblServicio_has_Plato.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Servicio_has_Plato)tblServicio_has_Plato.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        }else{
        
        }
    }
    
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicio(registro.getInt("codigoServicio"),
                                            registro.getDate("fechaServicio"),
                                            registro.getString("tipoServicio"),
                                            registro.getString("horaServicio"),
                                            registro.getString("lugarServicio"),
                                            registro.getString("telefonoContacto"),
                                            registro.getInt("codigoEmpresa") );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Plato(registro.getInt("codigoPlato"),
                                        registro.getInt("cantidad"),
                                        registro.getString("nombrePlato"),
                                        registro.getString("descripcionPlato"),
                                        registro.getDouble("precioPlato"),
                                        registro.getInt("codigoTipoPlato") );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblServicio_has_Plato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro que quiere eliminar el registro?","Eliminar",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Platos(?)}");
                        procedimiento.setInt(1,((Servicio_has_Plato)tblServicio_has_Plato.getSelectionModel().getSelectedItem()).getCodigoServicio());
                        procedimiento.execute();
                        listaSHP.remove(tblServicio_has_Plato.getSelectionModel().getSelectedIndex());
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
    
    public void cargarDatos(){
        tblServicio_has_Plato.setItems(getServicio_has_Plato());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory <Servicio_has_Plato, Integer>("codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Servicio_has_Plato, Integer>("codigoPlato"));
    }
    
    public ObservableList<Servicio_has_Plato> getServicio_has_Plato(){
        ArrayList<Servicio_has_Plato> lista = new ArrayList<Servicio_has_Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarSHP}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio_has_Plato(resultado.getInt("codigoServicio"),
                                                                    resultado.getInt("codigoPlato") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaSHP = FXCollections.observableArrayList(lista);
    }
    
     public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                                        resultado.getDate("fechaServicio"),
                                        resultado.getString("tipoServicio"),
                                        resultado.getString("horaServicio"),
                                        resultado.getString("lugarServicio"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicio = FXCollections.observableArrayList(lista);
    }
     
     public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                                        resultado.getInt("cantidad"),
                                        resultado.getString("nombrePlato"),
                                        resultado.getString("descripcionPlato"),
                                        resultado.getDouble("precioPlato"),
                                        resultado.getInt("codigoTipoPlato") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPlato = FXCollections.observableArrayList(lista);
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
    
    public void limpiarControles(){
        cmbCodigoServicio.getSelectionModel().clearSelection();
        cmbCodigoPlato.getSelectionModel().clearSelection();
    }
}
