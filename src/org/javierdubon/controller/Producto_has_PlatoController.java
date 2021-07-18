
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
import org.javierdubon.bean.Producto;
import org.javierdubon.bean.Producto_has_Plato;
import org.javierdubon.db.Conexion;
import org.javierdubon.system.Principal;

public class Producto_has_PlatoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO, AGREGAR, GUARDAR, ELIMINAR, CANCELAR, EDITAR, ACTUALIZAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<Producto_has_Plato> listaPHP;
    ObservableList<Producto> listaProducto;
    ObservableList<Plato> listaPlato;
    
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblProducto_has_Plato;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colCodigoPlato;
    @FXML private Button btnEliminar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        limpiarControles();
        cargarDatos();
        btnEliminar.getStylesheets().add("/org/javierdubon/resource/boton.css");
        tblProducto_has_Plato.getStylesheets().add("/org/javierdubon/resource/TableView.css");
    }
    
    public void seleccionar(){
        if(tblProducto_has_Plato.getSelectionModel().getSelectedItem() != null){
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Producto_has_Plato)tblProducto_has_Plato.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Producto_has_Plato)tblProducto_has_Plato.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        }else{
        
        }
    }  
    
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Producto(registro.getInt("codigoProducto"),
                                            registro.getString("nombreProducto"),
                                            registro.getInt("cantidad") );
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
                if(tblProducto_has_Plato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro que quiere eliminar el registro?","Eliminar",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos_has_Platos(?)}");
                        procedimiento.setInt(1,((Producto_has_Plato)tblProducto_has_Plato.getSelectionModel().getSelectedItem()).getCodigoProducto());
                        procedimiento.execute();
                        listaPHP.remove(tblProducto_has_Plato.getSelectionModel().getSelectedIndex());
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
        tblProducto_has_Plato.setItems(getProducto_has_Plato());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto_has_Plato, Integer>("codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Producto_has_Plato, Integer>("codigoPlato"));
    }
    
    public ObservableList<Producto_has_Plato> getProducto_has_Plato(){
        ArrayList<Producto_has_Plato> lista = new ArrayList<Producto_has_Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPHP}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto_has_Plato(resultado.getInt("codigoProducto"),
                                                    resultado.getInt("codigoPlato") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPHP = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                                        resultado.getString("nombreProducto"),
                                        resultado.getInt("cantidad") ));   
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return listaProducto = FXCollections.observableArrayList(lista);
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
        cmbCodigoProducto.getSelectionModel().clearSelection();
        cmbCodigoPlato.getSelectionModel().clearSelection();
    }
}
