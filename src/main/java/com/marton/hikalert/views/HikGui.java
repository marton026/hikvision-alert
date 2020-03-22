package com.marton.hikalert.views;


import com.marton.hikalert.MenuView;
import com.marton.hikalert.controllers.HikCommand;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;


@Route
public class HikGui extends VerticalLayout {

    private Label lMain;
    private Label lIp;
    private TextField ipAddressTextField;
    private Label lUser;
    private TextField userTextField;
    private Label lPass;
    private PasswordField passTextField;
    private Label lChangeText;
    private TextField textChangeFieldMessage;
    private Label lId;
    private TextField idTextField;
    private Label lEnabled;
    private Checkbox checkbox;
    private Label lposx;
    private TextField posxTextField;
    private Label lposy;
    private TextField posyTextField;
    private Button buttonTextChange;
    private Button buttonMenu;
    private HikCommand hikCommand;

    @Autowired
    public HikGui(HikCommand hikCommand) {
        this.hikCommand = hikCommand;
        lMain = new Label();
        lIp = new Label();
        ipAddressTextField = new TextField();
        lUser = new Label();
        userTextField = new TextField();
        lPass = new Label();
        passTextField = new PasswordField();
        lChangeText = new Label();
        textChangeFieldMessage = new TextField();
        lId = new Label();
        idTextField = new TextField();
        lEnabled = new Label();
        checkbox = new Checkbox();
        lposx = new Label();
        posxTextField = new TextField();
        lposy = new Label();
        posyTextField = new TextField();
        buttonTextChange = new Button("Zmień tekst");
        buttonMenu = new Button("Menu");
        add(lMain, lIp, ipAddressTextField, lUser, userTextField, lPass, passTextField, lChangeText, textChangeFieldMessage,
                lId, idTextField, lEnabled, checkbox, lposx, posxTextField, lposy, posyTextField, buttonTextChange,buttonMenu);

        lMain.setText("Ustawienia kamery Hikvision");
        lIp.setText("Podaj IP kamery: np:192.168.0.111:81");
        lUser.setText("Użytkownik:");
        lPass.setText("Hasło");
        lChangeText.setText("Wprowadź tekst do kamery");
        lId.setText("Wprowadź ID tekstu: 1-4");
        lEnabled.setText("Włącz , Wyłącz");
        lposx.setText("Podaj współrzędne tekstu na osi X (0-650)");
        lposy.setText("Podaj współrzędne tekstu na osi Y (0-600)");
        checkbox.setValue(true);

        buttonMenu.addClickListener(e -> UI.getCurrent().navigate(MenuView.class));

        buttonTextChange.addClickListener(buttonClickEvent -> {
            try {
                hikCommand.textChange(new URL("http://" + ipAddressTextField.getValue() +
                                "/Video/inputs/channels/2/overlays/text/2/"),
                        textChangeFieldMessage.getValue(),
                        idTextField.getValue(), checkbox.getValue(), posxTextField.getValue(),
                        posyTextField.getValue(), userTextField.getValue(), passTextField.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

