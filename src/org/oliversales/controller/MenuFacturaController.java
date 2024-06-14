/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oliversales.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.oliversales.bean.Factura;
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
    
    @FXML private TextField txtNumFact;
    @FXML private TextField txtEstado;
    @FXML private TextField txtTotalF;
    @FXML private TextField txtFechaF;
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
        // TODO
    }   
    
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
        txtFechaF.setEditable(false);
        cmbCodClien.setEditable(true);
        cmbCodEmp.setEditable(true);
    }
    
    public void activarControles(){
        txtNumFact.setEditable(true);
        txtEstado.setEditable(true);
        txtTotalF.setEditable(true);
        txtFechaF.setEditable(true);
        cmbCodClien.setEditable(false);
        cmbCodEmp.setEditable(false);
    }
    
    public void limpiarControles(){
        txtNumFact.clear();
        txtEstado.clear();
        txtTotalF.clear();
        txtFechaF.clear();
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
