/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package products.product;

import entity.Product;
import entity.Purchase;
import entity.User;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jptv22shopfx.HomeController;

/**
 * FXML Controller class
 *
 * @author minec
 */
public class ProductController implements Initializable {
    private Image image;
    private HomeController homeController;
    private Button btnBuy;
    private Button btnClose;
    private Stage productWindow;
    @FXML
    private Pane pProductRoot;
    @FXML
    private ImageView ivCover;
    @FXML
    private TextField tfquantityToBuy;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tfquantityToBuy.setTextFormatter(new TextFormatter<>(this::decimalFilter));
    }    
    
    public void showProduct(Product product) {
        
        productWindow = new Stage();
        productWindow.setTitle(product.getName());
        productWindow.initModality(Modality.WINDOW_MODAL);
        productWindow.initOwner(homeController.getApp().getPrimaryStage());
        image = new Image(new ByteArrayInputStream(product.getCover()));
        ImageView ivCoverBig = new ImageView(image);
        ivCoverBig.setId("big_product_cover");
        VBox vbProduct = new VBox();
        vbProduct.setAlignment(Pos.CENTER);
        vbProduct.getChildren().add(ivCoverBig);
        btnBuy = new Button("Купить");
        btnClose = new Button("Закрыть");
        tfquantityToBuy = new TextField();
        btnClose.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Обработка события для левой кнопки мыши
                productWindow.close();
            }
        });
 
        btnClose.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
               productWindow.close();
            }
        });
       btnBuy.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Обработка события для левой кнопки мыши
                buyProduct(product);
            }
        });
        
        btnBuy.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
               buyProduct(product);
            }
        });        
        
       HBox hbButtons = new HBox();
       hbButtons.setPrefSize(Double.MAX_VALUE, 29);
       hbButtons.alignmentProperty().set(Pos.CENTER_RIGHT);
       hbButtons.setSpacing(10);
       hbButtons.setPadding(new Insets(10));
       hbButtons.getChildren().addAll(btnBuy,btnClose,tfquantityToBuy);
       vbProduct.getChildren().add(hbButtons);
       Scene scene = new Scene(vbProduct,450,700);
       scene.getStylesheets().add(getClass().getResource("/products/product/product.css")
               .toExternalForm());
       productWindow.setScene(scene);
       productWindow.show();               
                  
    }
    
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }


    private void buyProduct(Product product){
        if (tfquantityToBuy.getText().isEmpty()) {
            System.out.println("Все поля доджны быть заполнены");
            return;
        } else if (!tfquantityToBuy.getText().isEmpty()) {
            handleDecimalInput();
                System.out.println("Ok 0");
        } else {
            System.out.println("Ok 0");
        }        
        Purchase purchase = new Purchase();
        purchase.setProduct(product);
        purchase.setUser(jptv22shopfx.JPTV22ShopFX.currentUser);

        String strQuantity = tfquantityToBuy.getText();
        int intQuantity = Integer.parseInt(strQuantity);
        System.out.println(intQuantity);
        
        if(product.getQuantity()>0){
            while(true){
                if(intQuantity==0){
                    break;
                } else if (intQuantity <=0 || intQuantity > product.getQuantity()){
                    homeController.getLbInfoHome().setText("Неверное значение");
                } else{
                    double totalPrice = intQuantity * product.getPrice();
                    double roundedTotalPrice = Math.round(totalPrice*100.0)/100.0;
                    if(roundedTotalPrice<=jptv22shopfx.JPTV22ShopFX.currentUser.getBalance()){
                        jptv22shopfx.JPTV22ShopFX.currentUser.setBalance(Math.round((jptv22shopfx.JPTV22ShopFX.currentUser.getBalance() - roundedTotalPrice)*100.0)/100.0);
                        
                        product.setQuantity(product.getQuantity() - intQuantity);
                        purchase.setTotal(roundedTotalPrice);
                        
                        try {
                            homeController.getEntityManager().getTransaction().begin();
                            homeController.getEntityManager().persist(purchase);
                            homeController.getEntityManager().persist(product);
                            homeController.getEntityManager().persist(jptv22shopfx.JPTV22ShopFX.currentUser);
                            homeController.getEntityManager().getTransaction().commit();
                            homeController.getLbInfoHome().setText("Purchase successful. Total cost: " + roundedTotalPrice + " $");
                            
                                        homeController.setLbInfoUser(String.format(
                                        "Вы вошли как %s (%s %s) %.2f %s",
                                        jptv22shopfx.JPTV22ShopFX.currentUser.getLogin(),
                                        jptv22shopfx.JPTV22ShopFX.currentUser.getFirstname(), 
                                        jptv22shopfx.JPTV22ShopFX.currentUser.getLastname(),
                                        jptv22shopfx.JPTV22ShopFX.currentUser.getBalance(),
                                        "$"
                                        
                                ));
                            
                            productWindow.close();
                            
                        } catch (Exception e) {
                            homeController.getEntityManager().getTransaction().rollback();
                              System.out.println("Error 173");
                        }
                        
                    }else {
                        homeController.getLbInfoHome().setText("Not enough money to buy!");
                    }
                    break;
                }
            }
        }else{
            homeController.getLbInfoHome().setText("Товар распродан");
        }
        
        
    }
    
        private Change decimalFilter(Change change) {
            if (change.isContentChange()) {
                String newText = change.getControlNewText();
                if (!newText.matches("\\d*(\\.\\d{0,2})?")) {
                    return null; // Reject the change
                }
            }
            return change;
        }
    
    @FXML
    public void handleDecimalInput() {
        String text = tfquantityToBuy.getText();
        if (!text.matches("\\d*(\\.\\d{0,2})?")) {
            tfquantityToBuy.setText(text.replaceAll("[^\\d.]", ""));
            System.out.println("Неверный формат 1");
        }else{
            System.out.println("Ok 0");
        }
    }    
    
    
}
