/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jptv22shopfx;

import admin.adminpane.AdminpaneController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import products.newproduct.NewproductController;
import products.tableproducts.TableproductsController;
import users.login.LoginController;
import users.newuser.NewuserController;
import users.profile.ProfileController;
import entity.Product;
import javafx.scene.layout.Pane;

/**
 *
 * @author minec
 */
public class HomeController implements Initializable {
    private JPTV22ShopFX app;
    private EntityManager em;
    private MenuItem menuItem;
    @FXML private VBox vbHomeContent;
    @FXML private Label lbInfoHome;
    @FXML private Label lbInfoUser;
    @FXML private MenuItem mbEditPrfile;
    @FXML private MenuItem mbProductsAddNewProduct;
    @FXML private MenuItem mbProductsTableProducts;
    @FXML private MenuItem mbAdminPane;
    @FXML private TableView tvProductsRoot;
    @FXML private Pane tvProductsRootROOT;
    
    
    public HomeController() {
        tvProductsRoot = new TableView<Product>();
    }    
    
    @FXML public void clickMenuAddNewUser(){
                

        lbInfoHome.setText("");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/users/newuser/newuser.fxml"));
            VBox vbNewuserRoot = loader.load();
            NewuserController newuserController = loader.getController();
            newuserController.setEntityManager(getEntityManager());
            app.getPrimaryStage().setTitle("JPTV22ShopFX-регистрация пользователя");
            vbHomeContent.getChildren().clear();
            vbHomeContent.getChildren().add(vbNewuserRoot);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "Не загружен //users/newuser/newuser.fxml", ex);
        }
    }   
    
    
    @FXML public void clickMenuLogin(){
        clickMenuLogin("");
    }
    @FXML public void clickMenuLogin(String massage){
        lbInfoHome.setText(massage);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/users/login/login.fxml"));
            VBox vbLoginRoot = loader.load();
            LoginController loginController = loader.getController();
            loginController.setEntityManager(getEntityManager());
            loginController.setHomeController(this);
            app.getPrimaryStage().setTitle("JPTV22ShopFX-Вход");
            vbHomeContent.getChildren().clear();
            vbHomeContent.getChildren().add(vbLoginRoot);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "Не загружен /users/login/login.fxml", ex);
        }
    }    
    
    
    @FXML public void clickMenuLogout(){
        this.menuItem=null;
        jptv22shopfx.JPTV22ShopFX.currentUser = null;
        vbHomeContent.getChildren().clear();
        lbInfoHome.setText("Вы вышли!");
        lbInfoUser.setText("");
        clickMenuLogin();
    }    
    
    
    
    @FXML public void clickMenuEditProfile(){
        setMenuItem(mbEditPrfile);
        if(JPTV22ShopFX.currentUser == null){
           clickMenuLogin();
           return;
        }
        if(!JPTV22ShopFX.currentUser.getRoles().contains(JPTV22ShopFX.roles.USER.toString())){
            clickMenuLogin("Авторизуйтесь");
            return;
        }
        lbInfoHome.setText("");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/users/profile/profile.fxml"));
            VBox vbProfileRoot = loader.load();
            ProfileController profileController = loader.getController();
            profileController.setHomeController(this);
            profileController.initProfileForm();
            app.getPrimaryStage().setTitle("JPTV22Library - профайл пользователя");
            vbHomeContent.getChildren().clear();
            vbHomeContent.getChildren().add(vbProfileRoot);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "Не загружен /users/profile/profile.fxml", ex);
        }
    } 
    
    
    @FXML 
    public void clickMenuShowAdminpane(){
        setMenuItem(mbAdminPane);
         if(JPTV22ShopFX.currentUser == null){
           clickMenuLogin();
           return;
        }
        if(!JPTV22ShopFX.currentUser.getRoles().contains(JPTV22ShopFX.roles.ADMINISTRATOR.toString())){
            clickMenuLogin("Вы должны иметь роль "+JPTV22ShopFX.roles.ADMINISTRATOR.toString());
            return;
        }
        
        lbInfoHome.setText("");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/admin/adminpane/adminpane.fxml"));
            app.getPrimaryStage().setTitle("JPTV22ShopFX - Панель администратора");
            AnchorPane apAdminRoot = loader.load();
            AdminpaneController adminpaneController = loader.getController();
            adminpaneController.setEntityManager(getApp().getEntityManager());
            adminpaneController.setCbUsers();
            adminpaneController.setCbRoles();
            apAdminRoot.setPrefWidth(JPTV22ShopFX.WIDTH);
            apAdminRoot.setPrefHeight(JPTV22ShopFX.HEIGHT);
            this.vbHomeContent.getChildren().clear();
            vbHomeContent.getChildren().add(apAdminRoot);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "Не загружен /admin/adminpane/adminpane.fxml", ex);
        }
    }    
    
    
    @FXML public void clickMenuAddNewProduct(){
        setMenuItem(mbProductsAddNewProduct);
        if(JPTV22ShopFX.currentUser == null){
           clickMenuLogin();
           return;
        }
        if(!JPTV22ShopFX.currentUser.getRoles().contains(JPTV22ShopFX.roles.MANAGER.toString())){
            clickMenuLogin("Вы должны иметь роль "+JPTV22ShopFX.roles.MANAGER.toString());
            return;
        }
        
        
        lbInfoHome.setText("");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/products/newproduct/newproduct.fxml"));
            VBox vbNewProductRoot = loader.load();
            NewproductController newproductController = loader.getController();
            newproductController.setEntityManager(getEntityManager());
            app.getPrimaryStage().setTitle("JPTV22ShopFX-добавление новой книги");
            vbHomeContent.getChildren().clear();
            vbHomeContent.getChildren().add(vbNewProductRoot);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "/products/newproduct/newproduct.fxml", ex);
        }
    }
    
    
    @FXML 
    public void clickMenuTableProducts(){
        setMenuItem(mbProductsTableProducts);
         if(JPTV22ShopFX.currentUser == null){
           clickMenuLogin();
           return;
        }
        if(!JPTV22ShopFX.currentUser.getRoles().contains(JPTV22ShopFX.roles.USER.toString())){
            clickMenuLogin();
            return;
        }
        
        lbInfoHome.setText("");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/products/tableproducts/tableproducts.fxml"));
            tvProductsRootROOT = loader.load();
            TableproductsController tableproductsController = loader.getController();
            app.getPrimaryStage().setTitle("JPTV22Library-список книг");
            tableproductsController.setHomeController(this);
            tableproductsController.initTable();
            vbHomeContent.getChildren().clear();
            vbHomeContent.getChildren().add(tvProductsRootROOT);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "/products/tableproducts/tableproducts.fxml", ex);
        }
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vbHomeContent.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        if(jptv22shopfx.JPTV22ShopFX.currentUser == null){
            lbInfoUser.setText("Авторизуйтесь!");
        }else{
            lbInfoUser.setText("Управление программой от имени пользователя: "+jptv22shopfx.JPTV22ShopFX.currentUser.getLogin());
        }
    }    
    
    public EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    public JPTV22ShopFX getApp() {
        return app;
    }

    public void setApp(JPTV22ShopFX app) {
        this.app = app;
        this.em = app.getEntityManager();
    }
    
    public void setLbInfoUser(String message){
        this.lbInfoUser.setText(message);
    }

    public void setLbInfoHome(String massage) {
       this.lbInfoHome.setText(massage);
    }

    public VBox getVbHomeContent() {
        return this.vbHomeContent;
    }    
    
    private void authorizationInfo(String role, MenuItem menuItem) {
        if(jptv22shopfx.JPTV22ShopFX.currentUser == null){
            lbInfoHome.setText("");
            vbHomeContent.getChildren().clear();
            clickMenuLogin("Авторизуйтесь");
            return;
        }
        if(!jptv22shopfx.JPTV22ShopFX.currentUser.getRoles().contains(role)){
            clickMenuLogin(jptv22shopfx.JPTV22ShopFX.currentUser.getLogin() + " не имеет права на эту операцию");
            
        }
    }
    
    public Label getLbInfoHome() {
        return lbInfoHome;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }    
}
