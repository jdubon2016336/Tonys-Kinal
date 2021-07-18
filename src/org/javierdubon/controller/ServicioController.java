
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
import org.javierdubon.bean.Servicio;
import org.javierdubon.db.Conexion;
import org.javierdubon.report.GenerarReporte;
import org.javierdubon.system.Principal;

public class ServicioController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO,GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empresa> listaEmpresa;
    private ObservableList<Servicio> listaServicio;
    private DatePicker fecha;
    boolean vacio = false;
    
    @FXML private TextField txtCodigoServicio;
    @FXML private GridPane grpFechaServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtHoraServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa;
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
        grpFechaServicio.add(fecha, 0, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        btnNuevo.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnEditar.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnEliminar.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnReporte.getStylesheets().add("/org/javierdubon/resource/boton.css");
        tblServicios.getStylesheets().add("/org/javierdubon/resource/TableView.css");
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
        Servicio registro = new Servicio();
            if(registro.getTipoServicio().equals("") || registro.getHoraServicio().equals("") || registro.getLugarServicio().equals("") ||
                    registro.getTelefonoContacto().equals("") || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null || 
                        fecha.getSelectedDate() == null){
                vacio = true;
            }else{
                vacio = false;
            }
        return vacio;
    }
    
    public void guardar(){
        Servicio registro = new Servicio();
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setString(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
            cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
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
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro que quiere eliminar el registro?","Eliminar",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                        procedimiento.setInt(1,((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                        procedimiento.execute();
                        listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
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
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
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
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicio(?,?,?,?,?,?)}");
            Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(txtHoraServicio.getText());
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3, registro.getTipoServicio());
            procedimiento.setString(4, registro.getHoraServicio());
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());
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
                btnEditar.setDisable(false);
                btnReporte.setText("Reporte");
                btnReporte.setDisable(false);
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
            break;
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento primero");
                }
            break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int variable = Integer.valueOf(txtCodigoServicio.getText());
        parametros.put("variable", variable);
        GenerarReporte.mostrarReporte("ReporteServicioFinal.jasper", "Reporte de Servicios", parametros);
    }
    
    public void seleccionar(){
        if(tblServicios.getSelectionModel().getSelectedItem() != null){
        txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
        txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
        txtHoraServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio());
        txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
        txtTelefonoContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto()); 
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
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
    
    public void cargarDatos(){
        tblServicios.setItems(getServicios());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
    }
    
    public ObservableList<Servicio> getServicios(){
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

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void empresa(){
        escenarioPrincipal.empresa();
    }
    
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        grpFechaServicio.setDisable(true);
        txtTipoServicio.setEditable(false);
        txtHoraServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoServicio.setEditable(false);
        grpFechaServicio.setDisable(false);
        txtTipoServicio.setEditable(true);
        txtHoraServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoServicio.setText("");
        fecha.selectedDateProperty().set(null);
        txtTipoServicio.setText("");
        txtHoraServicio.setText("");
        txtLugarServicio.setText("");
        txtLugarServicio.setText("");
        txtTelefonoContacto.setText("");
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
    }
}
