/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package products.tableproducts;

import entity.Product;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javax.persistence.EntityManager;
import jptv22shopfx.HomeController;
import jptv22shopfx.JPTV22ShopFX;
import products.addquantity.AddquantityController;
import products.product.ProductController;
//import tools.ShoppingCart;

/**
 * FXML Controller class
 *
 * @author minec
 */
public class TableproductsController implements Initializable {

    @FXML private TableView tvProductsRoot;
    private HomeController homeController;
//    private ShoppingCart cart = new ShoppingCart();
    
    private JPTV22ShopFX app;
    
    @FXML
    private TableView<Product> tvProductsRoot1;

    
    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    private EntityManager em;
    
    
    public TableproductsController() {
        
    }
            
    
    
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }   
    
  
    
    
    
    public void initTable() {
        
        tvProductsRoot.setItems(FXCollections.observableArrayList(
                homeController.getEntityManager()
                    .createQuery("SELECT p FROM Product p")
                    .getResultList()
        ));
        
        TableColumn<Product, String> idProductCol = new TableColumn<>("#");
        idProductCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        
        TableColumn<Product, String> nameProductCol = new TableColumn<>("Name");
        nameProductCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        
        TableColumn<Product, String> priceProductCol = new TableColumn<>("Price");
        priceProductCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        
        TableColumn<Product, String> quantityProductCol = new TableColumn<>("Quantity");
        quantityProductCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());

        TableColumn<Product, Image> coverProductCol = new TableColumn<>("Photo");
        coverProductCol.setCellValueFactory(cellData -> {
            Image coverImage = new Image(cellData.getValue().getCoverAsStream());
            return new SimpleObjectProperty<>(coverImage);
        });   
        coverProductCol.setCellFactory(param -> new ImageViewTableCell<>());
        tvProductsRoot.getColumns().addAll(idProductCol,nameProductCol,coverProductCol, priceProductCol, quantityProductCol);
//        tvProductsRoot.setRowFactory(tv ->{
//            TableRow<Product> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                
//                    if (event.getClickCount() == 2 && (!row.isEmpty())){
//                    Product product = row.getItem();
//                    System.out.println("Выбран продукт с ID: " + product.getId());
//                    
//                    
//                    if(JPTV22ShopFX.currentUser == null){
//                      homeController.clickMenuLogin();
//                      return;
//                   }
//                   if(!JPTV22ShopFX.currentUser.getRoles().contains(JPTV22ShopFX.roles.MANAGER.toString())){
//                       homeController.clickMenuLogin("Вы должны иметь роль "+JPTV22ShopFX.roles.MANAGER.toString());
//                       return;
//                   }
//                    
//                    AddquantityController addquantityController = new AddquantityController();
//                    addquantityController.setHomeController(homeController);
//                    addquantityController.showAdd(product);
//                    
//                }else if(event.getClickCount() == 3 && (!row.isEmpty())){
//                    Product product = row.getItem();
//                    System.out.println("Выбран продукт с ID: " + product.getId());
//                    
//                    ProductController productController = new ProductController();
//                    productController.setHomeController(homeController);
//                    productController.showProduct(product);
//                    
//                }
//            
//
//            });
//            return row;
//        });

tvProductsRoot.setRowFactory(tv ->{
    TableRow<Product> row = new TableRow<>();
    row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && !row.isEmpty()) {
            Product product = row.getItem();
            System.out.println("Выбран продукт с ID: " + product.getId());
            

            
            if (event.getButton() == MouseButton.SECONDARY) {

                if (JPTV22ShopFX.currentUser == null) {
                    homeController.clickMenuLogin();
                    return;
                }
                if (!JPTV22ShopFX.currentUser.getRoles().contains(JPTV22ShopFX.roles.MANAGER.toString())) {
                    homeController.clickMenuLogin("Вы должны иметь роль " + JPTV22ShopFX.roles.MANAGER.toString());
                    return;
                }                
                
                AddquantityController addquantityController = new AddquantityController();
                addquantityController.setHomeController(homeController);
                addquantityController.showAdd(product);
                
            } else if (event.getButton() == MouseButton.PRIMARY) {
                ProductController productController = new ProductController();
                productController.setHomeController(homeController);
                productController.showProduct(product);
            }
        } 
    });
    return row;
});


    }
    
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }    
    
    
    class ImageViewTableCell<S, T> extends TableCell<S, T> {
        private final ImageView imageView = new ImageView();
        public ImageViewTableCell() {
            imageView.setFitWidth(50);
            imageView.setFitHeight(80);
        }
        
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                imageView.setImage((Image) item);
                setGraphic(imageView);
            }
        }
    }
    
    public EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }    
}
