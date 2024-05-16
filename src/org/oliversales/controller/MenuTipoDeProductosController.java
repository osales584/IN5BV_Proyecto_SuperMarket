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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.oliversales.bean.TipoDeProducto;
import org.oliversales.db.Conexion;
import org.oliversales.system.Principal;

/**
 * FXML Controller class
 *
 * @author olive
 */
public class MenuTipoDeProductosController implements Initializable {

    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<TipoDeProducto> listaTipoDeProducto;
    
   @FXML private Button btnRegresar;
   
   // Cuadro de texto 
   @FXML private TextField txtCodigoPo;
   @FXML private TextField txtDescripcionPo;

   // Columnas 
   @FXML private TableView tblTipoDeProducto;
   @FXML private TableColumn colCodigoPo;
   @FXML private TableColumn colDescripcionPo;

   
   // Botones para el CRUD 
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
   // Imagenes
   @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;

// -----------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        cargarDatos();
    }    
    
    public void cargarDatos(){
       tblTipoDeProducto.setItems(getTipoDeProducto());
        colCodigoPo.setCellValueFactory(new PropertyValueFactory<TipoDeProducto, Integer>("codigoTipoProducto"));
        colDescripcionPo.setCellValueFactory(new PropertyValueFactory<TipoDeProducto, String>("descripcion"));
    }
    
// -----------------------------------------------------------------------------    
    public void seleccionarElemento(){
        txtCodigoPo.setText(String.valueOf(((TipoDeProducto)tblTipoDeProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        txtDescripcionPo.setText(((TipoDeProducto)tblTipoDeProducto.getSelectionModel().getSelectedItem()).getDescripcion());
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
        return listaTipoDeProducto = FXCollections.observableArrayList(Lista);
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
                imgAgregar.setImage(new Image("/org/oliversales/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/oliversales/images/Cancelar.png")); 
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("/org/oliversales/images/Agregar.png"));
                imgEliminar.setImage(new Image("/org/oliversales/images/Eliminar.png"));         
                tipoDeOperaciones = operaciones.NINGUNO;
                break;                
        }
        
        //Image 
        
    }
    
    public void guardar(){
        TipoDeProducto registro = new TipoDeProducto();
        registro.setCodigoTipoProducto(Integer.parseInt(txtCodigoPo.getText()));
        registro.setDescripcion(txtDescripcionPo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoProducto(?, ?)}");
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            listaTipoDeProducto.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }    
    
// -----------------------------------------------------------------------------
// Eliminar    
    public void eliminar(){
        switch(tipoDeOperaciones){
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
                break;
            default:
                if(tblTipoDeProducto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si elimina registro",
                            "Eliminar Cargo Empleado", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoProducto(?)}");
                            procedimiento.setInt(1, ((TipoDeProducto)tblTipoDeProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            listaTipoDeProducto.remove(tblTipoDeProducto.getSelectionModel().getSelectedItem());                         
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    limpiarControles();
                }else 
                    JOptionPane.showMessageDialog(null,"De ve selccionar un elemento");
        } 
    }
    
// -----------------------------------------------------------------------------
// Editar    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblTipoDeProducto.getSelectionModel().getSelectedItem() !=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/oliversales/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/oliversales/images/Borrar.png"));
                    activarControles();
                    txtCodigoPo.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                    
                }else 
                    JOptionPane.showMessageDialog(null, "Debe seleccionar algun elemento");
                break;
            case ACTUALIZAR:
                    actualizar();
                    desactivarControles();
                    limpiarControles();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnAgregar.setDisable(false);
                    btnEliminar.setDisable(false);
                    imgEditar.setImage(new Image ("/org/oliversales/images/Editar.png"));
                    imgReporte.setImage(new Image("/org/oliversales/images/Reportes.png"));
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTipoProducto(?, ?)}");
            TipoDeProducto registro = (TipoDeProducto)tblTipoDeProducto.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionPo.getText());
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();

        }catch (Exception e){
            e.printStackTrace();
        }
    }  

// -----------------------------------------------------------------------------
// Reporte    
    public void reporte(){
        switch (tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/og/oliversales/images/Editar.png"));
                imgReporte.setImage(new Image("/org/oliversales/images/Reportes.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                        
        }
    }
// -----------------------------------------------------------------------------    
    public void desactivarControles(){
        txtCodigoPo.setEditable(false);
        txtDescripcionPo.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoPo.setEditable(true);
        txtDescripcionPo.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoPo.clear();
        txtDescripcionPo.clear();
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
