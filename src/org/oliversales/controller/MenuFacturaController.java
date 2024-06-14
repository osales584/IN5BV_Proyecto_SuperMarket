/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oliversales.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import org.oliversales.bean.Clientes;
import org.oliversales.bean.Empleados;
import org.oliversales.bean.Factura;
import org.oliversales.db.Conexion;
import org.oliversales.report.GenerarReportes;
import org.oliversales.system.Principal;

/**
 * FXML Controller class
 *
 * @author olive
 */
public class MenuFacturaController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Factura> listarFacturas;
    private ObservableList<Factura> buscarFacturas;
    private ObservableList<Clientes> listarClientes;
    private ObservableList<Empleados> listarEmpleados;
    
    @FXML private TextField txtNumFact;
    @FXML private TextField txtEstado;
    @FXML private TextField txtTotalF;
    @FXML private TextField txtBuscar;
    @FXML private DatePicker dpFechaF;
    @FXML private ComboBox cmbCodClien;
    @FXML private ComboBox cmbCodEmp;
    
    @FXML private TableView tblFactura;
    @FXML private TableColumn colNumFact;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colTotalF;
    @FXML private TableColumn colFechaF;
    @FXML private TableColumn colCodClien;
    @FXML private TableColumn colCodEmp;

    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnRegresar;
    
    //Imagenes
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        cargarDatos();
        cmbCodClien.setItems(getClientes());

    }   
    
    public void cargarDatos() {
        tblFactura.setItems(getFactura());
        colNumFact.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTotalF.setCellValueFactory(new PropertyValueFactory<>("totalFactura"));
        colFechaF.setCellValueFactory(new PropertyValueFactory<>("fechaFactura"));
        colCodClien.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
        colCodEmp.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
    }
    
    public void seleccionarElementos() {
        Factura compraSeleccionada = (Factura) tblFactura.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            txtNumFact.setText(String.valueOf(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura()));
            txtEstado.setText(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getEstado());
            txtTotalF.setText((String.valueOf(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getTotalFactura())));
            dpFechaF.setValue(compraSeleccionada.getFechaFactura().toLocalDate());
            cmbCodClien.getSelectionModel().select(buscarCliente(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            cmbCodEmp.getSelectionModel().select(buscarEmpleados(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        }
    }
    
    
    public Empleados buscarEmpleados(int codigoEmpleado) {
        Empleados resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleados(?);");
            p.setInt(1, codigoEmpleado);
            ResultSet registro = p.executeQuery();
            while (registro.next()) {
                resultado = new Empleados(registro.getInt("codigoEmpleado"),
                        registro.getString("nombreEmpleado"),
                        registro.getString("apellidoEmpleado"),
                        registro.getDouble("sueldo"),
                        registro.getString("direccion"),
                        registro.getString("turno"),
                        registro.getInt("codigoCargoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Clientes buscarCliente(int codigoCliente) {
        Clientes resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall(" call sp_BuscarClientes(?);");
            p.setInt(1, codigoCliente);
            ResultSet registro = p.executeQuery();
            while (registro.next()) {
                resultado = new Clientes(registro.getInt("codigoCliente"),
                        registro.getString("nombreCliente"),
                        registro.getString("apellidoCliente"),
                        registro.getString("NITCliente"),
                        registro.getString("direccionCliente"),
                        registro.getString("telefonoCliente"),
                        registro.getString("correoCliente")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
// ----------------------------------------------------------------------------
public ObservableList<Factura> getFactura() {
        ArrayList<Factura> lista = new ArrayList<>();
        ResultSet resultado = null;
        java.sql.Date fechaDocumento = null;
        if (dpFechaF.getValue() != null) {
            fechaDocumento = java.sql.Date.valueOf(dpFechaF.getValue());
        }
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_listarFactura();");
            resultado = p.executeQuery();
            while (resultado.next()) {
                lista.add(new Factura(resultado.getInt("numeroFactura"),
                        resultado.getString("estado"),
                        resultado.getDouble("totalFactura"),
                        resultado.getDate("fechaFactura"),
                        resultado.getInt("codigoCliente"),
                        resultado.getInt("codigoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarFacturas = FXCollections.observableList(lista);
    }
// -----------------------------------------------------------------------------
    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarClientes();");
            resultado = procedimiento.executeQuery();
            /**
             * El bucle while agrega los datos a la lista.
             */
            while (resultado.next()) {
                lista.add(new Clientes(resultado.getInt("codigoCliente"),
                        resultado.getString("nombreCliente"),
                        resultado.getString("apellidoCliente"),
                        resultado.getString("NITCliente"),
                        resultado.getString("direccionCliente"),
                        resultado.getString("telefonoCliente"),
                        resultado.getString("correoCliente")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarClientes = FXCollections.observableList(lista);
    } 
    
// -----------------------------------------------------------------------------
    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados();");
            resultado = p.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleados(resultado.getInt("codigoEmpleado"),
                        resultado.getString("nombreEmpleado"),
                        resultado.getString("apellidoEmpleado"),
                        resultado.getDouble("sueldo"),
                        resultado.getString("direccion"),
                        resultado.getString("turno"),
                        resultado.getInt("codigoCargoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarEmpleados = FXCollections.observableList(lista);
    }
    
// -----------------------------------------------------------------------------
    public void reportes() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("/org/oliversales/images/Agregar.png"));
                imgEliminar.setImage(new Image("/org/oliversales/images/Eliminar.png"));         
                tipoDeOperaciones = operaciones.NINGUNO;
        }

    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int factId = ((Factura)tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura();
        parametros.put("numeroFactura", factId);
        GenerarReportes.mostrarReportes("ReporteFacturaVespertina.jasper", "Factura de la tarde", parametros);
    }
    
// -----------------------------------------------------------------------------    
    public void desactivarControles(){
        txtNumFact.setEditable(false);
        txtEstado.setEditable(false);
        txtTotalF.setEditable(false);
        dpFechaF.setEditable(false);
        cmbCodClien.setEditable(true);
        cmbCodEmp.setEditable(true);
    }
    
    public void activarControles(){
        txtNumFact.setEditable(true);
        txtEstado.setEditable(true);
        txtTotalF.setEditable(true);
        dpFechaF.setEditable(true);
        cmbCodClien.setEditable(false);
        cmbCodEmp.setEditable(false);
    }
    
    public void limpiarControles(){
        txtNumFact.clear();
        txtEstado.clear();
        txtTotalF.clear();
        dpFechaF.setValue(null);
        cmbCodClien.getSelectionModel().getSelectedItem();
        cmbCodEmp.getSelectionModel().getSelectedItem();
    } 
    
    
// -----------------------------------------------------------------------------    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    
    @FXML
    public void regresar (ActionEvent event){
        if (event.getSource() == btnRegresar){
        escenarioPrincipal.menuPrincipalView();
        }
    }    
    
}
