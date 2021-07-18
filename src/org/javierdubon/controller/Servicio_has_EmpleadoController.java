
package org.javierdubon.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.javierdubon.bean.Empleado;
import org.javierdubon.bean.Servicio;
import org.javierdubon.bean.Servicio_has_Empleado;
import org.javierdubon.db.Conexion;
import org.javierdubon.system.Principal;

public class Servicio_has_EmpleadoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,ELIMINAR,EDITAR,ACTUALIZAR,GUARDAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<Servicio_has_Empleado> listaSHE;
    private DatePicker fecha;
    boolean vacio = false;
    
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private GridPane grpFechaEvento;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private TableView tblServicio_has_Empleado;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/javierdubon/resource/DatePicker.css");
        grpFechaEvento.add(fecha, 0, 0);
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
        btnNuevo.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnEditar.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnEliminar.getStylesheets().add("/org/javierdubon/resource/boton.css");
        btnReporte.getStylesheets().add("/org/javierdubon/resource/boton.css");
        tblServicio_has_Empleado.getStylesheets().add("/org/javierdubon/resource/TableView.css");
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
        Servicio_has_Empleado registro = new Servicio_has_Empleado();
        if(cmbCodigoServicio.getSelectionModel().getSelectedItem() == null || cmbCodigoEmpleado.getSelectionModel().getSelectedItem() == null || 
                fecha.getSelectedDate() == null || txtHoraEvento.equals("") || txtLugarEvento.equals("")){
            vacio = true;
        }else{
            vacio = false;
        }
        return vacio;
    }
    
    public void guardar(){
        Servicio_has_Empleado registro = new Servicio_has_Empleado();
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios_has_Empleados(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setInt(2, registro.getCodigoEmpleado());
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(4, registro.getHoraEvento());
            procedimiento.setString(5, registro.getLugarEvento());
            procedimiento.execute();
            listaSHE.add(registro);
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
                if(tblServicio_has_Empleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro que quiere eliminar el registro?","Eliminar",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Empleados(?)}");
                        procedimiento.setInt(1,((Servicio_has_Empleado)tblServicio_has_Empleado.getSelectionModel().getSelectedItem()).getCodigoServicio());
                        procedimiento.execute();
                        listaSHE.remove(tblServicio_has_Empleado.getSelectionModel().getSelectedIndex());
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
                if(tblServicio_has_Empleado.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios_has_Empleados(?,?,?,?)}");
            Servicio_has_Empleado registro = (Servicio_has_Empleado)tblServicio_has_Empleado.getSelectionModel().getSelectedItem();
            registro.setFechaEvento(fecha.getSelectedDate());
            registro.setHoraEvento(txtHoraEvento.getText());
            registro.setLugarEvento(txtLugarEvento.getText());
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(3, registro.getHoraEvento());
            procedimiento.setString(4, registro.getLugarEvento());
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
        }
    }
    
    public void seleccionar(){
        if(tblServicio_has_Empleado.getSelectionModel().getSelectedItem() != null){
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicio_has_Empleado)tblServicio_has_Empleado.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicio_has_Empleado)tblServicio_has_Empleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            fecha.selectedDateProperty().set(((Servicio_has_Empleado)tblServicio_has_Empleado.getSelectionModel().getSelectedItem()).getFechaEvento());
            txtHoraEvento.setText(((Servicio_has_Empleado)tblServicio_has_Empleado.getSelectionModel().getSelectedItem()).getHoraEvento());
            txtLugarEvento.setText(((Servicio_has_Empleado)tblServicio_has_Empleado.getSelectionModel().getSelectedItem()).getLugarEvento());
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
    
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
           procedimiento.setInt(1, codigoEmpleado);
           ResultSet registro = procedimiento.executeQuery();
           while(registro.next()){
                resultado = new Empleado(registro.getInt("codigoEmpleado"), 
                                            registro.getInt("numeroEmpleado"),
                                            registro.getString("apellidosEmpleado"),
                                            registro.getString("nombresEmpleado"),
                                            registro.getString("direccionEmpleado"),
                                            registro.getString("telefonoContacto"),
                                            registro.getString("gradoCocinero"),
                                            registro.getInt("codigoTipoEmpleado") );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void cargarDatos(){
        tblServicio_has_Empleado.setItems(getServicio_has_Empleado());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, String>("lugarEvento"));
    }
    
    public ObservableList<Servicio_has_Empleado> getServicio_has_Empleado(){
        ArrayList<Servicio_has_Empleado> lista = new ArrayList<Servicio_has_Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio_has_Empleado(resultado.getInt("codigoServicio"),
                                                    resultado.getInt("codigoEmpleado"),
                                                    resultado.getDate("fechaEvento"),
                                                    resultado.getString("horaEvento"),
                                                    resultado.getString("lugarEvento") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaSHE = FXCollections.observableArrayList(lista);
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
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void activarControles(){
        grpFechaEvento.setDisable(false);
        txtHoraEvento.setEditable(true);
        txtLugarEvento.setEditable(true);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
    }
    
    public void desactivarControles(){
        grpFechaEvento.setDisable(true);
        txtHoraEvento.setEditable(false);
        txtLugarEvento.setEditable(false);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
    }
    
    public void limpiarControles(){
        fecha.selectedDateProperty().set(null);
        txtHoraEvento.setText("");
        txtLugarEvento.setText("");
        cmbCodigoServicio.getSelectionModel().clearSelection();
    }

}
