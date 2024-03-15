/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package products.newproduct;

import entity.Product;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import jptv22shopfx.JPTV22ShopFX;
import org.imgscalr.Scalr;

/**
 * FXML Controller class
 *
 * @author minec
 */
public class NewproductController implements Initializable {
    
    private EntityManager em;
    private JPTV22ShopFX app;
    private File selectedFile;
    @FXML
    private TextField tfNameProduct;
    @FXML
    private TextField tfPriceProduct;
    @FXML
    private TextField tfQuantityProduct;
    @FXML
    private Label lbInfo;
    @FXML
    private Label lbInfo1;
    @FXML
    private Label lbInfo2;
    @FXML
    private Button btSelectPhoto;
    @FXML
    private Button btAddNewProduct;

    public NewproductController() {
    }
    
    @FXML
    public void selectPhoto(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор фото");
        selectedFile = fileChooser.showOpenDialog(new Stage());
        btSelectPhoto.setText("Выбран файл: " + selectedFile.getName());
        btSelectPhoto.disableProperty().set(true);
    }
    
    
    @FXML
    public void addNewProduct() {
        if (tfNameProduct.getText().isEmpty() || (tfPriceProduct.getText().isEmpty() || tfQuantityProduct.getText().isEmpty())) {
            lbInfo.setText("Все поля доджны быть заполнены");
            return;
        } else if ((!tfPriceProduct.getText().isEmpty() || !tfQuantityProduct.getText().isEmpty()) && !tfNameProduct.getText().isEmpty()) {
            handleDecimalInput();
            handleIntegerInput();
            lbInfo.setText("Ok 0");
        } else {
            lbInfo.setText("Ok 0");
        }
        
        Product product = new Product();
        product.setName(tfNameProduct.getText());
        
        String strPrice = tfPriceProduct.getText();
        double dblPrice = Double.parseDouble(strPrice);
        System.out.println(dblPrice);
        product.setPrice(dblPrice);
        
        String strQuantity = tfQuantityProduct.getText();
        int intQuantity = Integer.parseInt(strQuantity);
        System.out.println(intQuantity);
        product.setQuantity(intQuantity);
        
        
        try{
            BufferedImage biProductPhoto = ImageIO.read(selectedFile);
            BufferedImage biScaledProductPhoto = Scalr.resize(biProductPhoto, Scalr.Mode.FIT_TO_WIDTH,400);
            ByteArrayOutputStream baos = new ByteArrayOutputStream ();
            ImageIO.write(biScaledProductPhoto, "jpg", baos);
            product.setCover(baos.toByteArray());
            em.getTransaction().begin();
            em.persist(product);
            em.getTransaction().commit();
            lbInfo.setText("Продукт успешно добавлен");
            lbInfo1.setText("");
            lbInfo2.setText("");
        } catch(IOException ex){
            lbInfo.setText("Продукт добавить не удалось");
            lbInfo1.setText("");
            lbInfo2.setText("");            
            Logger.getLogger(NewproductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        btSelectPhoto.disableProperty().set(false);
        selectedFile = null;
        tfNameProduct.setText("");
        tfPriceProduct.setText("");
        tfQuantityProduct.setText("");
        btSelectPhoto.setText("Выбрать фото");
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tfPriceProduct.setTextFormatter(new TextFormatter<>(this::decimalFilter));

        // Set up text formatter for integerField
        tfQuantityProduct.setTextFormatter(new TextFormatter<>(this::integerFilter));
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

        private Change integerFilter(Change change) {
            if (change.isContentChange()) {
                String newText = change.getControlNewText();
                if (!newText.matches("\\d*")) {
                    return null; // Reject the change
                }
            }
            return change;
    }   
    
    @FXML
    public void handleDecimalInput() {
        String text = tfPriceProduct.getText();
        if (!text.matches("\\d*(\\.\\d{0,2})?")) {
            tfPriceProduct.setText(text.replaceAll("[^\\d.]", ""));
            lbInfo1.setText("Неверный формат 1");
        }else{
            lbInfo1.setText("Ok 1");
        }
    }

    @FXML
    public void handleIntegerInput() {
        String text = tfQuantityProduct.getText();
        if (!text.matches("\\d+")) {
            tfQuantityProduct.setText(text.replaceAll("[^\\d]", ""));
            lbInfo2.setText("Неверный формат 2");
        }else{
            lbInfo2.setText("Ok 2");
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
    }
    public static BufferedImage convertToBufferedImage(File file) {
        try {
            // Чтение изображения из файла с использованием ImageIO
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
