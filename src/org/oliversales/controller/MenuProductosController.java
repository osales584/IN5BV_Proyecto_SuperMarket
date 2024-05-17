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
    private ObservableList <Producto> listaProducto;
    private ObservableList <Proveedores> listaProveedor;
    private ObservableList <TipoDeProducto> listaP;
    
    @FXML private TextField txtCodigoProd;
    @FXML private TextField txtDescProd;
    @FXML private TextField txtPrecioU;
    @FXML private TextField txtPrecioD;
    @FXML private TextField txtPrecioM;
    @FXML private TextField txtExistencia;
    @FXML private ComboBox cmbCodigoTipoP;
    @FXML private ComboBox cmbCodProv;
    
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodProd;
    @FXML private TableColumn colDescProd;
    @FXML private TableColumn colPrecioU;
    @FXML private TableColumn colPrecioD;
    @FXML private TableColumn colPrecioM;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colCodTipoProd;
    @FXML private TableColumn colCodProv;
    
    @FXML private Button btnRegresar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
// -----------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoP.setItems(getTipoDeProducto());
        cmbCodProv.setItems(getProveedores());
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
    
// -----------------------------------------------------------------------------    
    public void selecionarElementos(){
       txtCodigoProd.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
       txtDescProd.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
       txtPrecioU.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
       txtPrecioD.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
       txtPrecioM.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
       txtExistencia.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
       cmbCodigoTipoP.getSelectionModel().select(buscarTipoProducto(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
    }    
    
// -----------------------------------------------------------------------------
    public TipoDeProducto buscarTipoProducto (int codigoTipoProducto ){
        TipoDeProducto resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoProducto(?)}");
         procedimiento.setInt(1, codigoTipoProducto);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new TipoDeProducto(registro.getInt("codigoTipoProducto"),
                                            registro.getString("descripcionProducto")
             
             );
         }
        }catch (Exception e)
        {
            e.printStackTrace();
        }    
    
        return resultado;
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
        return listaP = FXCollections.observableArrayList(Lista);
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
        return listaProveedor = FXCollections.observableArrayList(Lista);
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
    
     public void guardar (){
         Producto registro = new Producto();
         registro.setCodigoProducto(txtCodigoProd.getText());
         registro.setCodigoProveedor(((Proveedores)cmbCodProv.getSelectionModel().getSelectedItem()
                 ).getCodigoProveedor());
         registro.setCodigoTipoProducto(((TipoDeProducto)cmbCodigoTipoP.getSelectionModel().getSelectedItem())
                 .getCodigoTipoProducto());
         registro.setDescripcionProducto(txtDescProd.getText());
         registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
         registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
         registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
         registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
         try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall
        ("{call sp_AgregarProductos(?, ?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setString(1, registro.getCodigoProducto());
        procedimiento.setString(2, registro.getDescripcionProducto());
        procedimiento.setDouble(3, registro.getPrecioUnitario());
        procedimiento.setDouble(4, registro.getPrecioDocena());
        procedimiento.setDouble(5, registro.getPrecioMayor());
        procedimiento.setInt(6, registro.getExistencia());
        procedimiento.setInt(7, registro.getCodigoProveedor());
        procedimiento.setInt(8, registro.getCodigoTipoProducto());
        procedimiento.execute();
        
        listaProducto.add(registro);

         }catch (Exception e){
             e.printStackTrace();
         }
     
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
        cmbCodProv.setEditable(true);
    }
    
    public void activarControles(){
        txtCodigoProd.setEditable(true);
        txtDescProd.setEditable(true);
        txtPrecioU.setEditable(true);
        txtPrecioD.setEditable(true);
        txtPrecioM.setEditable(true);
        txtExistencia.setEditable(true);
        cmbCodigoTipoP.setEditable(false);
        cmbCodProv.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoProd.clear();
        txtDescProd.clear();
        txtPrecioU.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtExistencia.clear();
        cmbCodigoTipoP.getSelectionModel().getSelectedItem();
        cmbCodProv.getSelectionModel().getSelectedItem();
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
