
package org.oliversales.system;


import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.oliversales.controller.MenuCargoController;
import org.oliversales.controller.MenuClientesController;
import org.oliversales.controller.MenuComprasController;
import org.oliversales.controller.MenuDetalleCompraController;
import org.oliversales.controller.MenuDetalleFacturaController;
import org.oliversales.controller.MenuEmailProveedorController;
import org.oliversales.controller.MenuEmpleadosController;
import org.oliversales.controller.MenuFacturaController;
import org.oliversales.controller.MenuPrincipalController;
import org.oliversales.controller.MenuProductosController;
import org.oliversales.controller.MenuProgramadorController;
import org.oliversales.controller.MenuProveedoresController;
import org.oliversales.controller.MenuTelefonoProveedorController;
import org.oliversales.controller.MenuTipoDeProductosController;


/*

Documentacion
*Nombre Oliver Sales
*Fecha de creacion 11/04/2024
*Fecha de modificacion 10/05/2024

 */

public class Principal extends Application {
  private Stage escenarioPrincipal;
  private Scene escena;
  private final String URLVIEW = "/org/oliversales/view/";
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
       this.escenarioPrincipal = escenarioPrincipal;
       this.escenarioPrincipal.setTitle("MEGAMARKET");
       escenarioPrincipal.getIcons().add(new Image("/org/oliversales/images/LogoMegaMarket (1).png"));
       menuPrincipalView();
      //Parent root = FXMLLoader.load(getClass().getResource("/org/luishernandez/view/MenuPrincipalView.fxml"));
      // Scene escena = new Scene(root);
      // escenarioPrincipal.setScene(escena);
       escenarioPrincipal.show();      
    }

    
    
    
     public Initializable cambiarEscena(String fxmlName, int width, int heigth) throws Exception{
         Initializable resultado;
         FXMLLoader loader = new FXMLLoader();
         
         InputStream file = Principal.class.getResourceAsStream( URLVIEW + fxmlName);
         loader.setBuilderFactory(new JavaFXBuilderFactory());
         loader.setLocation(Principal.class.getResource(URLVIEW + fxmlName));
         
         escena = new Scene ((AnchorPane)loader.load(file), width, heigth);
         escenarioPrincipal.setScene(escena);
         escenarioPrincipal.sizeToScene();
         
         resultado = (Initializable)loader.getController();
         
        return resultado;
              }
    
    public void menuPrincipalView (){
        try{
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 730,413);
            menuPrincipalView.setEscenarioPrincipal(this);  
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuClientesView(){
        try{
            MenuClientesController menuClienteView = (MenuClientesController)cambiarEscena("MenuClientesView.fxml", 808,455);
            menuClienteView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void menuProgramadorView(){
            try{
            MenuProgramadorController menuProgramadorView = (MenuProgramadorController)cambiarEscena("MenuProgramadorView.fxml", 764,431);
            menuProgramadorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }   
    }
 
    public void menuProveedoresView(){
        try{
            MenuProveedoresController menuProveedoresView = (MenuProveedoresController)cambiarEscena("MenuProveedoresView.fxml", 932,522);
            menuProveedoresView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void menuComprasView(){
        try{
            MenuComprasController menuComprasView = (MenuComprasController)cambiarEscena("MenuComprasView.fxml", 734,410);
            menuComprasView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
     
    public void menuCargoView(){
        try{
            MenuCargoController menuCargoView = (MenuCargoController)cambiarEscena("MenuCargoView.fxml", 672,377);
            menuCargoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void menuTipoDeProductosView(){
        try{
            MenuTipoDeProductosController menuTipoDeProductosView = (MenuTipoDeProductosController)cambiarEscena("MenuTipoDeProductosView.fxml", 759,428);
            menuTipoDeProductosView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void menuProductosView(){
        try{
            MenuProductosController menuProductosView = (MenuProductosController)cambiarEscena("MenuProductosView.fxml", 779,431);
            menuProductosView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void menuDetalleCompraView(){
        try{
            MenuDetalleCompraController menuDetalleCompraView = (MenuDetalleCompraController)cambiarEscena("MenuDetalleCompraView.fxml", 708,399);
            menuDetalleCompraView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    } 
    
    public void menuTelefonoProveedorView(){
        try{
            MenuTelefonoProveedorController menuTelefonoProveedorView = (MenuTelefonoProveedorController)cambiarEscena("MenuTelefonoProveedorView.fxml", 706,398);
            menuTelefonoProveedorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void menuEmailProveedorView(){
        try{
            MenuEmailProveedorController menuEmailProveedorView = (MenuEmailProveedorController)cambiarEscena("MenuEmailProveedorView.fxml", 685,390);
            menuEmailProveedorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void menuEmpleadosView(){
        try{
            MenuEmpleadosController menuEmpleadosView = (MenuEmpleadosController)cambiarEscena("MenuEmpleadosView.fxml", 761,433);
            menuEmpleadosView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void menuFacturaView(){
        try{
            MenuFacturaController menuFacturaView = (MenuFacturaController)cambiarEscena("MenuFacturaView.fxml", 755,438);
            menuFacturaView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void menuDetalleFacturaView(){
        try{
            MenuDetalleFacturaController menuDetalleFacturaView = (MenuDetalleFacturaController)cambiarEscena("MenuDetalleFacturaView.fxml", 749,421);
            menuDetalleFacturaView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
