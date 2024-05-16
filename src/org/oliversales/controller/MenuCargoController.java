
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
import org.oliversales.bean.Cargo;
import org.oliversales.db.Conexion;
import org.oliversales.system.Principal;

public class MenuCargoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Cargo> listaCargo;
    
    
   @FXML private Button btnRegresar;
    
   // Cuadro de texto 
   @FXML private TextField txtCodigoCa;
   @FXML private TextField txtNombreCa;
   @FXML private TextField txtDescripcionCa;
   
   // Columnas 
   @FXML private TableView tblCargo;
   @FXML private TableColumn colCodigoCa;
   @FXML private TableColumn colNombreCa;
   @FXML private TableColumn colDescripcionCa;
   
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
       tblCargo.setItems(getCargo());
        colCodigoCa.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargoEmpleado"));
        colNombreCa.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
        colDescripcionCa.setCellValueFactory(new PropertyValueFactory<Cargo, String>("descripcionCargo"));       
    }
    
// -----------------------------------------------------------------------------
    public void seleccionarElemento(){
        txtCodigoCa.setText(String.valueOf(((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        txtNombreCa.setText(((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getNombreCargo());
        txtDescripcionCa.setText(((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getDescripcionCargo());
    }
    
// -----------------------------------------------------------------------------
    public ObservableList<Cargo> getCargo(){
        ArrayList<Cargo> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargoEmpleado()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                Lista.add(new Cargo (resultado.getInt("codigoCargoEmpleado"),
                                       resultado.getString("nombreCargo"),
                                       resultado.getString("descripcionCargo")
                ));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCargo = FXCollections.observableArrayList(Lista);
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
        Cargo registro = new Cargo();
        registro.setCodigoCargoEmpleado(Integer.parseInt(txtCodigoCa.getText()));
        registro.setNombreCargo(txtNombreCa.getText());
        registro.setDescripcionCargo(txtDescripcionCa.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCargoEmpleado(?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
            procedimiento.execute();
            listaCargo.add(registro);
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
                if(tblCargo.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si elimina registro",
                            "Eliminar Cargo Empleado", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCargoEmpleado(?)}");
                            procedimiento.setInt(1, ((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                            procedimiento.execute();
                            listaCargo.remove(tblCargo.getSelectionModel().getSelectedItem());                         
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
                if(tblCargo.getSelectionModel().getSelectedItem() !=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/oliversales/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/oliversales/images/Borrar.png"));
                    activarControles();
                    txtCodigoCa.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCargoEmpleado(?, ?, ?)}");
            Cargo registro = (Cargo)tblCargo.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombreCa.getText());
            registro.setDescripcionCargo(txtDescripcionCa.getText());
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
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
        txtCodigoCa.setEditable(false);
        txtNombreCa.setEditable(false);
        txtDescripcionCa.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoCa.setEditable(true);
        txtNombreCa.setEditable(true);
        txtDescripcionCa.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoCa.clear();
        txtNombreCa.clear();
        txtDescripcionCa.clear();
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
