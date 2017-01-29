package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    JFXSnackbar snack;
    @FXML
    TextField text;
    @FXML
    JFXButton button;
    @FXML
    Pane pane;
    static int nbaffichage = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                System.out.println("ffff");
            }
        });
        snack.registerSnackbarContainer(pane);
        snack.refreshPopup();
        button.setOnAction(e -> {
            int delay = 10000;
            String toshow = "";
            String myString = "This text will be copied into clipboard when running this code!";
            StringSelection stringSelection = new StringSelection(myString);
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            if (nbaffichage == 0) {
                toshow = "Text copied into clipboard Press (CTRL+V) or (RightClick+Paste) to get it\n";
                nbaffichage = 1;
            } else {
                delay = 5000;
            }
            toshow = toshow + "" + myString;

            nbaffichage++;
            snack.show(toshow, 6000);
        });
    }
}
