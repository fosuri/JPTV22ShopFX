/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package products.addquantity;

import entity.Product;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jptv22shopfx.HomeController;

/**
 * FXML Controller class
 *
 * @author pupil
 */
public class AddquantityController implements Initializable {
    private HomeController homeController;
    private Stage productaddWindow;
    private Image image;
    private Button btAdd;
    private Button btClose;    
    @FXML
    private TextField tfAddQuantity;
    @FXML Label lbQInfo;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tfAddQuantity.setTextFormatter(new TextFormatter<>(this::decimalFilter));
    }    
    
    public void showAdd(Product product){
        productaddWindow = new Stage();
        productaddWindow.setTitle(product.getName());
        productaddWindow.initModality(Modality.WINDOW_MODAL);
        productaddWindow.initOwner(homeController.getApp().getPrimaryStage());
        image = new Image(new ByteArrayInputStream(product.getCover()));
        ImageView ivCoverBig = new ImageView(image);
        ivCoverBig.setId("big_product_cover");
        
        VBox vbProduct = new VBox();
        vbProduct.setAlignment(Pos.CENTER);
        btAdd = new Button("Пополнить");
        btClose = new Button("Закрыть");
        tfAddQuantity = new TextField();
        lbQInfo = new Label();
        lbQInfo.setText("Пополнить (кол-во)");
        
        btClose.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Обработка события для левой кнопки мыши
                productaddWindow.close();
            }
        });
 
        btClose.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
               productaddWindow.close();
            }
        });
        
       btAdd.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Обработка события для левой кнопки мыши
                addQuantity(product);
            }
        });
        
        btAdd.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
               addQuantity(product);
            }
        });   
        
       HBox hbButtons = new HBox();
       hbButtons.setPrefSize(Double.MAX_VALUE, 29);
       hbButtons.alignmentProperty().set(Pos.CENTER_RIGHT);
       hbButtons.setSpacing(10);
       hbButtons.setPadding(new Insets(10));
       hbButtons.getChildren().addAll(lbQInfo,tfAddQuantity,btAdd,btClose);
       vbProduct.getChildren().add(hbButtons);
       Scene scene = new Scene(vbProduct,450,700);
       scene.getStylesheets().add(getClass().getResource("/products/addquantity/addquantity.css")
               .toExternalForm());
       productaddWindow.setScene(scene);
       productaddWindow.show();       
    }
    
    
    private void addQuantity(Product product){
        if (tfAddQuantity.getText().isEmpty()) {
            System.out.println("Все поля доджны быть заполнены");
            return;
        } else if (!tfAddQuantity.getText().isEmpty()) {
            handleDecimalInput();
                System.out.println("Ok 0");
        } else {
            System.out.println("Ok 0");
        }  

        String strQuantity = tfAddQuantity.getText();
        int intQuantity = Integer.parseInt(strQuantity);
        System.out.println(intQuantity);   
        
        product.setQuantity(product.getQuantity() + intQuantity);
        
        try {
            homeController.getEntityManager().getTransaction().begin();
            homeController.getEntityManager().persist(product);
            homeController.getEntityManager().getTransaction().commit();
            
            homeController.getLbInfoHome().setText("К товару " + product.getName() + " было добавлено: " +intQuantity +" шт.");
            
            productaddWindow.close();
        } catch (Exception e) {
            homeController.getEntityManager().getTransaction().rollback();
            System.out.println("Error 108");            
        }
        
        
    }
    
    
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
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
        String text = tfAddQuantity.getText();
        if (!text.matches("\\d*(\\.\\d{0,2})?")) {
            tfAddQuantity.setText(text.replaceAll("[^\\d.]", ""));
            System.out.println("Неверный формат 1");
        }else{
            System.out.println("Ok 0");
        }
    }     
    
}
