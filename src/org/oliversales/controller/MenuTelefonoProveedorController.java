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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.oliversales.bean.Proveedores;
import org.oliversales.bean.TelefonoProveedor;
import org.oliversales.db.Conexion;
import org.oliversales.system.Principal;

/**
 * FXML Controller class
 *
 * @author olive
 */
public class MenuTelefonoProveedorController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Proveedores> listaProv;
    private ObservableList <TelefonoProveedor> TProveedor;
    
    @FXML private TextField txtCodTeleProv;
    @FXML private TextField txtNumPrin;
    @FXML private TextField txtNumSec;
    @FXML private TextField txtObservaciones;
    @FXML private ComboBox cmbCodProv;
    
    @FXML private TableView tblTelefonoProveedor;
    @FXML private TableColumn colCodTeleProv;
    @FXML private TableColumn colNumPrin;
    @FXML private TableColumn colNumSec;
    @FXML private TableColumn colObservaciones;
    @FXML private TableColumn colCodProv;
    
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;    
    @FXML private Button btnRegresar;
    
    //Imagenes
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
// ----------------------------------------------------------------------------- 
    @Override
    public void initialize(URL url,  ResourceBundle resources) {
       cargarDatos();
       cmbCodProv.setItems(getProveedores());
    }  
    
    public void cargarDatos(){
        tblTelefonoProveedor.setItems(getTelefonoProveedor());
        colCodTeleProv.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("codigoTelefonoProveedor"));
        colNumPrin.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("numeroPrincipal"));
        colNumSec.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("numeroSecundario"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("observaciones"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("codigoProveedor"));
    }
    
// -----------------------------------------------------------------------------    
    public void selecionarElementos(){
       txtCodTeleProv.setText(String.valueOf(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor()));
       txtNumPrin.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumeroPrincipal());
       txtNumSec.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumeroSecundario());
       txtObservaciones.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getObservaciones());
       cmbCodProv.getSelectionModel().select(buscarProveedores(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
    }
    
// -----------------------------------------------------------------------------    
    public Proveedores buscarProveedores (int codigoProveedor ){
        Proveedores resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProveedores(?)}");
         procedimiento.setInt(1, codigoProveedor);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Proveedores (registro.getInt("codigoProveedor"),
                                            registro.getString("NITProveedor"),
                                            registro.getString("nombreProveedor"),
                                            registro.getString("apellidoProveedor"),
                                            registro.getString("direccionProveedor"),
                                            registro.getString("razonSocial"),
                                            registro.getString("contactoPrincipal"),
                                            registro.getString("paginaWeb")
             
             );
         }
        }catch (Exception e)
        {
            e.printStackTrace();
        }    
    
        return resultado;
    }
    
// ----------------------------------------------------------------------------/
    public ObservableList<TelefonoProveedor> getTelefonoProveedor(){
        ArrayList<TelefonoProveedor> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTelefonoProveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                Lista.add(new TelefonoProveedor (resultado.getInt("codigoTelefonoProveedor"),
                                       resultado.getString("numeroPrincipal"),
                                       resultado.getString("numeroSecundario"),
                                       resultado.getString("observaciones"),
                                       resultado.getInt("codigoProveedor")
                ));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return TProveedor = FXCollections.observableArrayList(Lista);
    }
    
// -----------------------------------------------------------------------------
    public ObservableList<Proveedores> getProveedores(){
        ArrayList<Proveedores> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                Lista.add(new Proveedores (resultado.getInt("codigoProveedor"),
                                       resultado.getString("NITProveedor"),
                                       resultado.getString("nombreProveedor"),
                                       resultado.getString("apellidoProveedor"),
                                       resultado.getString("direccionProveedor"),
                                       resultado.getString("razonSocial"),
                                       resultado.getString("contactoPrincipal"),
                                       resultado.getString("paginaWeb")
                ));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProv = FXCollections.observableArrayList(Lista);
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
