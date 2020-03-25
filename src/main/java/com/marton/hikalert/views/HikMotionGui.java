package com.marton.hikalert.views;

import com.marton.hikalert.MenuView;
import com.marton.hikalert.controllers.HikCommand;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;


@Route
public class HikMotionGui extends VerticalLayout {

    private Label lMain;
    private TextField ipAddressTextField;
    private TextField userTextField;
    private PasswordField passTextField;
    private Checkbox motionCheck;
    private Label lsensitive;
    private NumberField sensNumberField;
    private Label lfrequency;
    private NumberField frequencyNumberField;
    private Button buttonSave;
    private Label ltest;
    private TextField phoneNoTextField;
    private TextField messageTextField;
    private HikCommand hikCommand;
    private Button buttonStartStop;
    private Button buttonMenu;

    @Autowired
    public HikMotionGui(HikCommand hikCommand) {
        this.hikCommand = hikCommand;
        lMain = new Label();
        ipAddressTextField = new TextField("IP kamery: np:192.168.0.111:81");
        userTextField = new TextField("Użytkownik:","admin","");
        passTextField = new PasswordField("Hasło:");
        motionCheck = new Checkbox("Detekcja ruchu:");
        lsensitive = new Label();
        sensNumberField = new NumberField();
        lfrequency = new Label();
        frequencyNumberField = new NumberField();
        buttonSave = new Button("Zatwierdź");
        ltest = new Label();
        phoneNoTextField = new TextField("Numer telefonu na który wysyłamy alert","","Numer telefonu");
        messageTextField = new TextField("Wiadomość jaka ma być wysłana","Kamera KAM1 wykryła ruch","");
        buttonStartStop = new Button();
        buttonMenu = new Button("Menu");

        add(lMain, ipAddressTextField, userTextField, passTextField,
                motionCheck, lsensitive, sensNumberField, buttonSave,lfrequency,
                frequencyNumberField, ltest,phoneNoTextField,messageTextField, buttonStartStop,buttonMenu);

        lMain.setText("Ustawienia kamery Hikvision");
       // ipAddressTextField.setSizeFull();
        ipAddressTextField.setRequired(true);
        ipAddressTextField.setErrorMessage("Podaj adres IP kamery");
        userTextField.setPlaceholder("Użytkownik");
        passTextField.setPlaceholder("Hasło");
        passTextField.setRequired(true);
        passTextField.setErrorMessage("Podaj hasło");
        lsensitive.setText("Wybierz czułość detekcji ruchu:");
        sensNumberField.setValue(1d);
        sensNumberField.setHasControls(true);
        sensNumberField.setMin(1);
        sensNumberField.setMax(5);
        lfrequency.setText("Wybierz częstotliwość powiadomień sms");
        frequencyNumberField.setValue(1d);
        frequencyNumberField.setHasControls(true);
        frequencyNumberField.setValue(20.);
        frequencyNumberField.setMin(20);
        frequencyNumberField.setMax(100);
        ltest.setText("Włacz/Wyłącz detekcję ruchu z wysyłaniem alertów sms");

        if (hikCommand.ifStart) {
            buttonStartStop.setText("Stop");
        } else {
            buttonStartStop.setText("Start");
        }

        buttonMenu.addClickListener(e -> UI.getCurrent().navigate(MenuView.class));

        buttonSave.addClickListener(buttonClickEvent -> {
            try {
                double aDouble = Double.valueOf(sensNumberField.getValue());
                hikCommand.motionDetection(new URL("http://" + ipAddressTextField.getValue() +
                                "/MotionDetection/1"), motionCheck.getValue(),
                        userTextField.getValue(), passTextField.getValue(), String.valueOf((int) aDouble));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Wyjątek "+e);
            }
        });

        buttonStartStop.addClickListener(buttonClickEvent -> {
            ipAddressTextField.setRequired(true);
            ipAddressTextField.setErrorMessage("Podaj adres IP kamery");
            ipAddressTextField.addValueChangeListener(e ->{});
            passTextField.setRequired(true);
            if (!ipAddressTextField.getValue().isEmpty()) {
                if (hikCommand.ifStart==false) {
                    try {
                        double freqDouble = Double.valueOf(frequencyNumberField.getValue());
                        hikCommand.stopStart(new URL("http://" + ipAddressTextField.getValue() +
                                        "/Event/notification/alertStream"),
                                userTextField.getValue(), passTextField.getValue(),
                                Integer.parseInt(String.valueOf((int) freqDouble)),phoneNoTextField.getValue(),
                                messageTextField.getValue());
                        hikCommand.ifStart=true;
                        buttonStartStop.setText("Stop");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    hikCommand.ifStart=false;
                    buttonStartStop.setText("Start");
                }
            }
        });
    }
}

