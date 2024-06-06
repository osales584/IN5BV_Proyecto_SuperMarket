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
import org.oliversales.bean.EmailProveedor;
import org.oliversales.bean.Proveedores;
import org.oliversales.db.Conexion;
import org.oliversales.system.Principal;

/**
 * FXML Controller class
 *
 * @author olive
 */
public class MenuEmailProveedorController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Proveedores> listaProv;
    private ObservableList <EmailProveedor> EProveedor;
    
    @FXML private TextField txtCodEmProv;
    @FXML private TextField txtEmProv;
    @FXML private TextField txtDescripcion;
    @FXML private ComboBox cmbCodProv;
    
    @FXML private TableView tblEmailProveedor;
    @FXML private TableColumn colCodEmProv;
    @FXML private TableColumn colEmProv;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colCodProv;
    
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
    
// -----------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        cargarDatos();
        cmbCodProv.setItems(getProveedores());
    } 
    
    public void cargarDatos(){
        tblEmailProveedor.setItems(getEmailProveedor());
        colCodEmProv.setCellValueFactory(new PropertyValueFactory<EmailProveedor, String>("codigoEmailProveedor"));
        colEmProv.setCellValueFactory(new PropertyValueFactory<EmailProveedor, String>("emailProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<EmailProveedor, String>("descripcion"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<EmailProveedor, String>("codigoProveedor"));
    }
    
// -----------------------------------------------------------------------------    
    public void selecionarElementos(){
       txtCodEmProv.setText(String.valueOf(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor()));
       txtEmProv.setText(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getEmailProveedor());
       txtDescripcion.setText(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getDescripcion());
       cmbCodProv.getSelectionModel().select(buscarProveedores(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
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
    public ObservableList<EmailProveedor> getEmailProveedor(){
        ArrayList<EmailProveedor> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmailProveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                Lista.add(new EmailProveedor (resultado.getInt("codigoEmailProveedor"),
                                       resultado.getString("emailProveedor"),
                                       resultado.getString("descripcion"),
                                       resultado.getInt("codigoProveedor")
                ));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return EProveedor = FXCollections.observableArrayList(Lista);
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
