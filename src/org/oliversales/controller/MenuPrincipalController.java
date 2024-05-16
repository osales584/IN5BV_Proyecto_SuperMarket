
package org.oliversales.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.oliversales.system.Principal;

// Herencia multiple concepto, vamos a uar interfacez, POO

public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    
    @FXML MenuItem btnMenuClientes;
    @FXML MenuItem btnMenuProgramador;
    @FXML MenuItem btnMenuProveedores;
    @FXML MenuItem btnMenuCompras;
    @FXML MenuItem btnMenuCargo;
    @FXML MenuItem btnMenuTipoDeProductos;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    @FXML
    public void clicClientes (ActionEvent event){
        if (event.getSource() == btnMenuClientes){
            escenarioPrincipal.menuClientesView();
        }
    }
    
    @FXML
    public void clicProgramador (ActionEvent event){
        if (event.getSource() == btnMenuProgramador){
            escenarioPrincipal.menuProgramadorView();
        }
    }
    
    @FXML
    public void clicProveedores (ActionEvent event){
        if (event.getSource() == btnMenuProveedores){
            escenarioPrincipal.menuProveedoresView();
        }
    }

    @FXML
    public void clicCompras (ActionEvent event){
        if (event.getSource() == btnMenuCompras){
            escenarioPrincipal.menuComprasView();
        }
    }
    
    @FXML
    public void clicCargo (ActionEvent event){
        if (event.getSource() == btnMenuCargo){
            escenarioPrincipal.menuCargoView();
        }
    }
    
    @FXML
    public void clicTipoDeProductos (ActionEvent event){
        if (event.getSource() == btnMenuTipoDeProductos){
            escenarioPrincipal.menuTipoDeProductosView();
        }
    }
    
}
