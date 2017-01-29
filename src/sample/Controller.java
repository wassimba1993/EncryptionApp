package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public void operate() {
        try {
            int delay = 10000;
            String toshow = "";
            String enteredText = text.getText().toString();
            String myString = Controller.sha1(enteredText);
            StringSelection stringSelection = new StringSelection(myString);
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            if (nbaffichage == 0) {
                toshow = "Text copied into clipboard Press (CTRL+V) or (RightClick+Paste) to get it\n";
                nbaffichage = 1;
            } else {
                delay = 5000;
            }
            toshow = toshow + "SHA1(" + enteredText + ") -> " + myString;

            nbaffichage++;
            snack.show(toshow, 6000);
        } catch (Exception ex1) {

        }
    }

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
            operate();
        });
        text.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    operate();
                }
            }
        });
    }
}
