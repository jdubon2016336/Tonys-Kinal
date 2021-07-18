
package org.javierdubon.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.javierdubon.bean.Empresa;
import org.javierdubon.bean.Presupuesto;
import org.javierdubon.db.Conexion;
import org.javierdubon.report.GenerarReporte;
import org.javierdubon.system.Principal;


public class PresupuestoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,ELIMINAR,EDITAR,ACTUALIZAR,GUARDAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empresa> listaEmpresa;
    private ObservableList<Presupuesto> listaPresupuesto;
    private DatePicker fecha;
    boolean vacio = false;
    
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFechaSolicitud;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableView tblPresupuestos;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/javierdubon/resource/DatePicker.css");
        grpFechaSolicitud.add(fecha, 0, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        btnNuevo.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnEditar.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnEliminar.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnReporte.getStylesheets().add("/org/javierdubon/resource/boton.css");
        tblPresupuestos.getStylesheets().add("/org/javierdubon/resource/TableView.css");
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
        Presupuesto registro = new Presupuesto();
        if(fecha.getSelectedDate() == null || registro.getCantidadPresupuesto() == (0.00) || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null){
            vacio = true;
        }else{
            vacio = false;
        }
        return vacio;
    }
    
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
            cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void seleccionar(){
        if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
         txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
         fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
         txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
         cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        }else{
        
        }
    } 
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"),
                                        registro.getString("telefono") );
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
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro que quiere eliminar el registro?","Eliminar",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                        procedimiento.setInt(1,((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                        procedimiento.execute();
                        listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
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
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
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
            case NINGUNO:
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento primero");
                }
            break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int variable = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("variable", variable);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuestos", parametros);
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?)}");/*,?*/
            Presupuesto registro = (Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
       
    }
    
    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
    }
    
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuesto}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"), 
                                            resultado.getDate("fechaSolicitud"),
                                            resultado.getDouble("cantidadPresupuesto"),
                                            resultado.getInt("codigoEmpresa") ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                                        resultado.getString("nombreEmpresa"),
                                        resultado.getString("direccion"),
                                        resultado.getString("telefono") ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrinciapal) {
        this.escenarioPrincipal = escenarioPrinciapal;
    }
    
    public void empresa(){
        escenarioPrincipal.empresa();
    }
    
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(false);
        grpFechaSolicitud.setDisable(false);
        txtCantidadPresupuesto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        grpFechaSolicitud.setDisable(true);
        txtCantidadPresupuesto.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoPresupuesto.setText("");
        fecha.selectedDateProperty().set(null);
        txtCantidadPresupuesto.setText("");
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
    }
}
