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
import org.oliversales.bean.Producto;
import org.oliversales.bean.Proveedores;
import org.oliversales.bean.TipoDeProducto;
import org.oliversales.db.Conexion;
import org.oliversales.system.Principal;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuProductosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Producto> ListaProducto;
    private ObservableList <Proveedores> ListaProveedor;
    private ObservableList <TipoDeProducto> ListaP;
    
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodProd;
    @FXML private TableColumn colDescProd;
    @FXML private TableColumn colPrecioU;
    @FXML private TableColumn colPrecioD;
    @FXML private TableColumn colPrecioM;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colCodTipoProd;
    @FXML private TableColumn colCodProv;
    @FXML private ComboBox cmbCodigoTipoP;
    @FXML private ComboBox cmbCodigoProv;
    
    @FXML private TextField txtCodigoProd;
    @FXML private TextField txtDescProd;
    @FXML private TextField txtPrecioU;
    @FXML private TextField txtPrecioD;
    @FXML private TextField txtPrecioM;
    @FXML private TextField txtExistencia;
    
    @FXML private Button btnRegresar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoP.setItems(getTipoDeProducto());
        cmbCodigoProv.setItems(getProveedores());
    }    
    
    public void cargarDatos(){
        tblProductos.setItems(getProducto());
        colCodProd.setCellValueFactory(new PropertyValueFactory<Producto, String>("codigoProducto"));
        colDescProd.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Producto, String>("precioUnitario"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<Producto, String>("precioDocena"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Producto, String>("precioMayor"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Producto, String>("existencia"));
        colCodTipoProd.setCellValueFactory(new PropertyValueFactory<Producto, String>("codigoTipoProducto"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<Producto, String>("codigoProveedor"));
    }
    
    
    
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> Lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Producto (resultado.getInt("codigoProducto"),
                                        resultado.getString("descripcionProducto"),
                                        resultado.getDouble("precioUnitario"),
                                        resultado.getDouble("precioDocena"),
                                        resultado.getDouble("precioMayor"),
                                        resultado.getInt("existencia"),
                                        resultado.getInt("codigoTipoProducto"),
                                        resultado.getInt("codigoProveedor")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return ListaProducto = FXCollections.observableArrayList(Lista);
    }

    public ObservableList<TipoDeProducto> getTipoDeProducto(){
        ArrayList<TipoDeProducto> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                Lista.add(new TipoDeProducto (resultado.getInt("codigoTipoProducto"),
                                       resultado.getString("descripcion")
                ));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return ListaP = FXCollections.observableArrayList(Lista);
    }

    
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
        return ListaProveedor = FXCollections.observableArrayList(Lista);
    }
    
// -----------------------------------------------------------------------------
// Agregar    
    public void agregar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);        
                tipoDeOperaciones = operaciones.NINGUNO;
                break;                
        }
    }
    
    public void guardar(){
        Producto registro = new Producto();
        registro.setCodigoProducto(txtCodigoProd.getText());
        registro.setCodigoProveedor(((Proveedores)cmbCodigoProv.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setCodigoTipoProducto(((TipoDeProducto)cmbCodigoProv.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
        registro.setDescripcionProducto(txtDescProd.getText());
        registro.setPrecioUnitario(txtPrecioU.getText());
        registro.setPrecioDocena(txtPrecioD.getText());
        registro.setPrecioMayor(txtPrecioM.getText());
        registro.setDescripcionProducto(txtDescProd.getText());
        
        
    }
    
// -----------------------------------------------------------------------------    
    public void desactivarControles(){
        txtCodigoProd.setEditable(false);
        txtDescProd.setEditable(false);
        txtPrecioU.setEditable(false);
        txtPrecioD.setEditable(false);
        txtPrecioM.setEditable(false);
        txtExistencia.setEditable(false);
        cmbCodigoTipoP.setEditable(true);
        cmbCodigoProv.setEditable(true);
    }
    
    public void activarControles(){
        txtCodigoProd.setEditable(true);
        txtDescProd.setEditable(true);
        txtPrecioD.setEditable(true);
        txtPrecioM.setEditable(true);
        txtExistencia.setEditable(true);
        cmbCodigoTipoP.setEditable(false);
        cmbCodigoProv.setEditable(false);
    }
    
    public void limpiarControles(){
        cmbCodigoTipoP.getSelectionModel().getSelectiondItem();
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
