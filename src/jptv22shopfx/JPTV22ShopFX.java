/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jptv22shopfx;

import entity.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import tools.PassEncrypt;

/**
 *
 * @author minec
 */
public class JPTV22ShopFX extends Application {
    public static enum roles {ADMINISTRATOR, MANAGER, USER};
    public static User currentUser;
    private final EntityManager em;
    private Stage primaryStage;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    
    public JPTV22ShopFX() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPTV22ShopFXPU");
        em = emf.createEntityManager();
        createSuperUser();
    }
    
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("JPTV22ShopFX");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("home.fxml"));
        Parent root = loader.load();
        HomeController homeController = loader.getController();
        homeController.setApp(this);
        Scene scene = new Scene(root, WIDTH,HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/jptv22shopfx/home.css").toExternalForm());
        primaryStage.centerOnScreen();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public EntityManager getEntityManager() {
        return em;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }    
    
    
    private void createSuperUser() {
        try {
            em.createQuery("SELECT user FROM User user WHERE user.login = :login")
                            .setParameter("login", "admin")
                            .getSingleResult();
            
        } catch (Exception e) {
            User user = new User();
            user.setFirstname("Nikita");
            user.setLastname("Karpov");
            user.setLogin("admin");
            user.setBalance(0);
            PassEncrypt pe = new PassEncrypt();
            user.setPassword(pe.getEncryptPassword("12345",pe.getSalt()));
            user.getRoles().add(jptv22shopfx.JPTV22ShopFX.roles.ADMINISTRATOR.toString());
            user.getRoles().add(jptv22shopfx.JPTV22ShopFX.roles.MANAGER.toString());
            user.getRoles().add(jptv22shopfx.JPTV22ShopFX.roles.USER.toString());
            try {
                em.getTransaction().begin();
                em.persist(user);
                em.getTransaction().commit();
            } catch (Exception ex) {
                em.getTransaction().setRollbackOnly();
            }
        }
    }    
    
}
