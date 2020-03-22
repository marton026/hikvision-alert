package com.marton.hikalert.views;

import com.marton.hikalert.MenuView;
import com.marton.hikalert.controllers.ModemConnection;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@Route
public class ReadSmsGui extends VerticalLayout {

    private TextField textFieldMemoryPlace;
    private Button buttonReadSms;
    private Label label;
    private ModemConnection modemConnection;
    private Button buttonMenu;

    @Autowired
    public ReadSmsGui(ModemConnection modemConnection) {
        this.modemConnection = modemConnection;
        textFieldMemoryPlace = new TextField();
        label = new Label();
        buttonReadSms = new Button("Czytaj SMS");
        buttonMenu = new Button("Menu");
        add(textFieldMemoryPlace, buttonReadSms, label,buttonMenu);
        textFieldMemoryPlace.setPlaceholder("Nr sms-a z pamięci");
        buttonMenu.addClickListener(e -> UI.getCurrent().navigate(MenuView.class));
        buttonReadSms.addClickListener(buttonClickEvent -> {
            String sms = modemConnection.readSms(textFieldMemoryPlace.getValue());
            label.setText(sms);
        });
    }
}

