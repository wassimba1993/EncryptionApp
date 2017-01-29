package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.paint.Paint;

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
    @FXML
    JFXPasswordField pwd;
    @FXML
    JFXToggleButton toggle;
    Scene scene;

    public void setScene(Scene scene) {
        this.scene = scene;
    }

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
            String enteredText = toggle.isSelected() ? pwd.getText().toString() : text.getText().toString();
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
            if (toggle.isSelected())
                enteredText = "PWD";
            toshow = toshow + "SHA1(" + enteredText + ") -> " + myString;

            nbaffichage++;
            if (toggle.isSelected()) {
                pwd.requestFocus();
            } else {
                text.requestFocus();
            }
            snack.show(toshow, 6000);
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
    }

    public void handleChanges() {
        text.setText("");
        pwd.setText("");
        //
        text.setVisible(!toggle.isSelected());
        pwd.setVisible(toggle.isSelected());
        if (toggle.isSelected()) {
            //secure mode,pwd is active
            System.out.println(scene.getHeight());
            scene.getStylesheets().clear();
            scene.getStylesheets().add("sample/style/style2.css");
            pwd.requestFocus();
            button.setStyle("-fx-background-color: #a10d81;");
            toggle.setToggleColor(Paint.valueOf("#a10d81"));
        } else {
            //not secure mode,text is active
            scene.getStylesheets().clear();
            scene.getStylesheets().add("sample/style/style.css");
            text.requestFocus();
            button.setStyle("-fx-background-color: teal;");
            toggle.setToggleColor(Paint.valueOf("teal"));

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        snack.registerSnackbarContainer(pane);
        snack.refreshPopup();
        button.setOnAction(e -> {
            operate();
        });

        toggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            handleChanges();
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
