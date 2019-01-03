package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import static javafx.application.Application.launch;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        GeografijaDAO ge = GeografijaDAO.getInstance();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader( getClass().getResource(
                "/izgled.fxml"), bundle);
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("izgled.fxml"));
        loader.setController(new IzgledController(ge));
        Parent root = loader.load();
        primaryStage.setTitle("Pretraga");
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
    }


    public static void main(String[] args) {
        Locale.setDefault(new Locale("bs", "BA"));
        launch(args);
        System.out.println("Gradovi su:\n" + ispisiGradove());
        glavniGrad();
        GeografijaDAO.removeInstance();
    }

    public static void glavniGrad () {
        System.out.println("Unesite naziv drzave: ");
        Scanner sc = new Scanner(System.in);
        String ime = sc.nextLine();
        Grad grad = GeografijaDAO.getInstance().glavniGrad(ime);

        if(grad == null) {
            System.out.println("Nepostojeca drzava");
            return;
        }

        System.out.println("Glavni grad drzave " + ime + " je " + grad.getNaziv() + ".");
    }

    public static String ispisiGradove () {
        ArrayList<Grad> gradovi = GeografijaDAO.getInstance().gradovi();
        String rezultat = "";
        if (gradovi != null) {
            for (Grad g : gradovi) {
                rezultat = rezultat + g.toString();
            }
        } else {
            System.out.println("Nema gradova");
        }
        return rezultat;
    }
}
