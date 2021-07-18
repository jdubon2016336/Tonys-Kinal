
package org.javierdubon.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.javierdubon.bean.Empleado;
import org.javierdubon.bean.TipoEmpleado;
import org.javierdubon.db.Conexion;
import org.javierdubon.report.GenerarReporte;
import org.javierdubon.system.Principal;

public class EmpleadoController implements Initializable{
        private Principal escenarioPrincipal;
        private enum operaciones{NUEVO,ELIMINAR,EDITAR,ACTUALIZAR,GUARDAR,CANCELAR,NINGUNO};
        private operaciones tipoDeOperacion = operaciones.NINGUNO;
        private ObservableList<Empleado> listaEmpleado;
        private ObservableList<TipoEmpleado> listaTipoEmpleado;
        boolean vacio = false;
        
        @FXML private TextField txtCodigoEmpleado;
        @FXML private TextField txtNumeroEmpleado;
        @FXML private TextField txtApellidosEmpleado;
        @FXML private TextField txtNombresEmpleado;
        @FXML private TextField txtDireccionEmpleado;
        @FXML private TextField txtTelefonoContacto;
        @FXML private TextField txtGradoCocinero;
        @FXML private ComboBox cmbCodigoTipoEmpleado;
        @FXML private TableColumn colCodigoEmpleado;
        @FXML private TableColumn colNumeroEmpleado;
        @FXML private TableColumn colApellidosEmpleado;
        @FXML private TableColumn colNombresEmpleado;
        @FXML private TableColumn colDireccionEmpleado;
        @FXML private TableColumn colTelefonoContacto;
        @FXML private TableColumn colGradoCocinero;
        @FXML private TableColumn colCodigoTipoEmpleado;
        @FXML private TableView tblEmpleados;
        @FXML private Button btnNuevo;
        @FXML private Button btnEditar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;

        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
        btnNuevo.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnEditar.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnEliminar.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnReporte.getStylesheets().add("/org/javierdubon/resource/boton.css");
        tblEmpleados.getStylesheets().add("/org/javierdubon/resource/TableView.css");
    }
    
     public void nuevo(){
        switch (tipoDeOperacion){
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
                validacion();
                if(vacio == false){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(true);
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                } else{
                    JOptionPane.showMessageDialog(null, "Debe llenar todos los datos");
                }
            break;
        }
    }
     
     private boolean validacion(){
        Empleado registro = new Empleado();
            if(registro.getNumeroEmpleado() == (0) || registro.getApellidosEmpleado().equals("") || registro.getNombresEmpleado().equals("") ||
                    registro.getDireccionEmpleado().equals("") || registro.getTelefonoContacto().equals("") || 
                        cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem() == null){
                vacio = true;
            }else{
                vacio = false;
            }
        return vacio;
    }
     
     public void guardar(){
         Empleado registro = new Empleado();
         registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
         registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
         registro.setNombresEmpleado(txtNombresEmpleado.getText());
         registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
         registro.setTelefonoContacto(txtTelefonoContacto.getText());
         registro.setGradoCocinero(txtGradoCocinero.getText());
         registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?,?,?,?,?,?,?)}");
             procedimiento.setInt(1, registro.getNumeroEmpleado());
             procedimiento.setString(2, registro.getApellidosEmpleado());
             procedimiento.setString(3, registro.getNombresEmpleado());
             procedimiento.setString(4, registro.getDireccionEmpleado());
             procedimiento.setString(5, registro.getTelefonoContacto());
             procedimiento.setString(6, registro.getGradoCocinero());
             procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
            cargarDatos();
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void seleccionar(){
         if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
         txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
         txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
         txtApellidosEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
         txtNombresEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
         txtDireccionEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
         txtTelefonoContacto.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
         txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
         cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
         }else{
        
        }
     }
     
     public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                            registro.getString("descripcion") );
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
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro que quiere eliminar el registro?","Eliminar",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                        procedimiento.setInt(1,((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                        procedimiento.execute();
                        listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
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
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento primero");
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
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles(); 
                limpiarControles();
                btnEditar.setText("Editar");
                btnEditar.setDisable(false);
                btnReporte.setText("Reporte");
                btnReporte.setDisable(false);
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
            break;
            default:
                imprimirReporte();
            break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpleado", null);
        GenerarReporte.mostrarReporte("ReporteEmpleados.jasper", "Reporte de Empleados", parametros);
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleado(?,?,?,?,?,?,?)}");/*,?,?*/
            Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
    }
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"), 
                                            resultado.getInt("numeroEmpleado"),
                                            resultado.getString("apellidosEmpleado"),
                                            resultado.getString("nombresEmpleado"),
                                            resultado.getString("direccionEmpleado"),
                                            resultado.getString("telefonoContacto"),
                                            resultado.getString("gradoCocinero"),
                                            resultado.getInt("codigoTipoEmpleado") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                            resultado.getString("descripcion") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void tipoEmpleado(){
        escenarioPrincipal.tipoEmpleado();
    }
    
     public void activarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbCodigoTipoEmpleado.setDisable(false);
    }

    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.setText("");
        txtNumeroEmpleado.setText("");
        txtApellidosEmpleado.setText("");
        txtNombresEmpleado.setText("");
        txtDireccionEmpleado.setText("");
        txtTelefonoContacto.setText("");
        txtGradoCocinero.setText("");    
        cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
    }
    
}
