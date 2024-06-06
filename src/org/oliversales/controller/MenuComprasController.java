
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
import org.oliversales.bean.Compras;
import org.oliversales.db.Conexion;
import org.oliversales.system.Principal;

public class MenuComprasController implements Initializable {

    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Compras> listaCompras;
    
    @FXML private Button btnRegresar;
    
   // Cuadro de texto 
   @FXML private TextField txtNumeroDoc;
   @FXML private TextField txtFechaDoc;
   @FXML private TextField txtDescripcion;
   @FXML private TextField txtTotalDoc;
   
   // Columnas 
   @FXML private TableView tblCompras;
   @FXML private TableColumn colnumeroDoc;
   @FXML private TableColumn colFechaDoc;
   @FXML private TableColumn colDescripcion;
   @FXML private TableColumn colTotalDoc;
   
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
       tblCompras.setItems(getCompras());
        colnumeroDoc.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
        colFechaDoc.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcion"));
        colTotalDoc.setCellValueFactory(new PropertyValueFactory<Compras,Double>("totalDocumento"));
    }
    
// -----------------------------------------------------------------------------    
    public void seleccionarElemento(){
        txtNumeroDoc.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        txtFechaDoc.setText(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getFechaDocumento());
        txtDescripcion.setText(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());
        txtTotalDoc.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).isTotalDocumento()));

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
        Compras registro = new Compras();
        registro.setNumeroDocumento(Integer.parseInt(txtNumeroDoc.getText()));
        registro.setFechaDocumento(txtFechaDoc.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setTotalDocumento(Double.parseDouble(txtNumeroDoc.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCompras(?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.isTotalDocumento());
            procedimiento.execute();
            listaCompras.add(registro);
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
                if(tblCompras.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si elimina registro",
                            "Eliminar Cargo Empleado", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCompras(?)}");
                            procedimiento.setInt(1, ((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listaCompras.remove(tblCompras.getSelectionModel().getSelectedItem());                         
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
                if(tblCompras.getSelectionModel().getSelectedItem() !=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/oliversales/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/oliversales/images/Borrar.png"));
                    activarControles();
                    txtNumeroDoc.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCompras(?, ?, ?, ?)}");
            Compras registro = (Compras)tblCompras.getSelectionModel().getSelectedItem();
            registro.setFechaDocumento(txtFechaDoc.getText());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setTotalDocumento(Double.parseDouble(txtNumeroDoc.getText()));
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.isTotalDocumento());
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
        txtNumeroDoc.setEditable(false);
        txtFechaDoc.setEditable(false);
        txtDescripcion.setEditable(false);
        txtTotalDoc.setEditable(false);
    }
    
    public void activarControles(){
        txtNumeroDoc.setEditable(true);
        txtFechaDoc.setEditable(true);
        txtDescripcion.setEditable(true);
        txtTotalDoc.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNumeroDoc.clear();
        txtFechaDoc.clear();
        txtDescripcion.clear();
        txtTotalDoc.clear();
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
