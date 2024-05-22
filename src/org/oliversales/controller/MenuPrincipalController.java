
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
    @FXML MenuItem btnMenuProductos;
    @FXML MenuItem btnMenuDetalleCompra;
    @FXML MenuItem btnMenuTelefonoProveedor;
    @FXML MenuItem btnMenuEmailProveedor;
    @FXML MenuItem btnMenuEmpleados;
    @FXML MenuItem btnMenuFactura;
    @FXML MenuItem btnMenuDetalleFactura;
    
    
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
    
    @FXML
    public void clicProductos (ActionEvent event){
        if (event.getSource() == btnMenuProductos){
            escenarioPrincipal.menuProductosView();
        }
    }
    
    @FXML
    public void clicDetalleCompra (ActionEvent event){
        if (event.getSource() == btnMenuDetalleCompra){
            escenarioPrincipal.menuDetalleCompraView();
        }
    }
    
    @FXML
    public void clicTelefonoProveedor (ActionEvent event){
        if (event.getSource() == btnMenuTelefonoProveedor){
            escenarioPrincipal.menuTelefonoProveedorView();
        }
    }
    
    @FXML
    public void clicEmailProveedor (ActionEvent event){
        if (event.getSource() == btnMenuEmailProveedor){
            escenarioPrincipal.menuEmailProveedorView();
        }
    }
    
    @FXML
    public void clicEmpleados (ActionEvent event){
        if (event.getSource() == btnMenuEmpleados){
            escenarioPrincipal.menuEmpleadosView();
        }
    }
    
    @FXML
    public void clicFactura (ActionEvent event){
        if (event.getSource() == btnMenuFactura){
            escenarioPrincipal.menuFacturaView();
        }
    }
    
    @FXML
    public void clicDetalleFactura (ActionEvent event){
        if (event.getSource() == btnMenuDetalleFactura){
            escenarioPrincipal.menuDetalleFacturaView();
        }
    }
    
    
}
