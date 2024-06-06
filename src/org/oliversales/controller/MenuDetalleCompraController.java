
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
import org.oliversales.bean.Compras;
import org.oliversales.bean.DetalleCompra;
import org.oliversales.bean.Producto;
import org.oliversales.db.Conexion;
import org.oliversales.system.Principal;

public class MenuDetalleCompraController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Producto> listaProducto;
    private ObservableList <Compras> listaCompras;
    private ObservableList <DetalleCompra> DetalleComp;
    
    @FXML private TextField txtCodDetalleComp;
    @FXML private TextField txtCostoU;
    @FXML private TextField txtCantidad;
    @FXML private ComboBox cmbCodProd;
    @FXML private ComboBox cmbNumeroDoc;
    
    @FXML private TableView tblDetalleCompra;
    @FXML private TableColumn colDetalleComp;
    @FXML private TableColumn colCostoU;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colCodProd;
    @FXML private TableColumn colNumeroDoc;
    
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
        cmbCodProd.setItems(getProducto());
        cmbNumeroDoc.setItems(getCompras());
    }  
    
    public void cargarDatos() {
        tblDetalleCompra.setItems(getDetalleCompra());
        colDetalleComp.setCellValueFactory(new PropertyValueFactory<>("codigoDetalleCompra"));
        colCostoU.setCellValueFactory(new PropertyValueFactory<>("costoUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCodProd.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colNumeroDoc.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));
    }
    
// -----------------------------------------------------------------------------
    public void seleccionarElementos() {
        txtCodDetalleComp.setText(String.valueOf(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra()));
        txtCostoU.setText(String.valueOf(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCostoUnitario()));
        txtCantidad.setText(String.valueOf(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbCodProd.getSelectionModel().select(buscarProducto(String.valueOf(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoProducto())));
        cmbNumeroDoc.getSelectionModel().select(buscarCompra(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
    }
    
// -----------------------------------------------------------------------------
    public Compras buscarCompra(int numeroDocumento) {
        Compras resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarCompras(?);");
            p.setInt(1, numeroDocumento);
            ResultSet registro = p.executeQuery();
            while (registro.next()) {
                resultado = new Compras(registro.getInt("numeroDocumento"),
                                        registro.getString("fechaDocumento"),
                                        registro.getString("descripcion"),
                                        registro.getDouble("totalDocumento"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }    
    
// ----------------------------------------------------------------------------
    public Producto buscarProducto(String codigoProducto) {
        Producto resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProductos(?);");
            p.setString(1, codigoProducto);
            ResultSet registro = p.executeQuery();
            while (registro.next()) {
                resultado = new Producto(registro.getString("codigoProducto"),
                                        registro.getString("descripcionProducto"),
                                        registro.getDouble("precioUnitario"),
                                        registro.getDouble("precioDocena"),
                                        registro.getDouble("precioMayor"),
                                        registro.getString("imagenProducto"),
                                        registro.getInt("existencia"),
                                        registro.getInt("codigoTipoProducto"),
                                        registro.getInt("codigoProveedor"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
// -----------------------------------------------------------------------------
    public ObservableList<DetalleCompra> getDetalleCompra(){
        ArrayList<DetalleCompra> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetalleCompra()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                Lista.add(new DetalleCompra (resultado.getInt("codigoDetalleCompra"),
                                       resultado.getDouble("costoUnitario"),
                                       resultado.getInt("cantidad"),
                                       resultado.getString("codigoProducto"),
                                       resultado.getInt("numeroDocumento")
                ));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return DetalleComp = FXCollections.observableArrayList(Lista);
    }
    
// -----------------------------------------------------------------------------    
    public ObservableList<Compras> getCompras(){
        ArrayList<Compras> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCompras()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                Lista.add(new Compras (resultado.getInt("numeroDocumento"),
                                       resultado.getString("fechaDocumento"),
                                       resultado.getString("descripcion"),
                                       resultado.getDouble("totalDocumento")
                ));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCompras = FXCollections.observableArrayList(Lista);
    }
    
// -----------------------------------------------------------------------------
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto (resultado.getString("codigoProducto"),
                                        resultado.getString("descripcionProducto"),
                                        resultado.getDouble("precioUnitario"),
                                        resultado.getDouble("precioDocena"),
                                        resultado.getDouble("precioMayor"),
                                        resultado.getString("imagenProducto"),
                                        resultado.getInt("existencia"),
                                        resultado.getInt("codigoTipoProducto"),
                                        resultado.getInt("codigoProveedor")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return listaProducto = FXCollections.observableArrayList(lista);
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
