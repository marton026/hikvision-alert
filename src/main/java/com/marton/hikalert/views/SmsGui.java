package com.marton.hikalert.views;

import com.marton.hikalert.MenuView;
import com.marton.hikalert.controllers.ModemConnection;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "smsWrite", absolute = true)
@PageTitle("Bramka SMS")
public class SmsGui extends VerticalLayout {


    private Button changeText;
    private TextField textFieldNo;
    private TextField textFieldMessage;
    private Button buttonSendSms;
    private Label notific;
    private Button buttonMenu;
    private ModemConnection modemConnection;

    @Autowired
    public SmsGui(ModemConnection modemConnection) {
        this.modemConnection = modemConnection;
        textFieldNo = new TextField();
        textFieldMessage = new TextField();
        buttonSendSms = new Button("Send SMS !!!");
        changeText = new Button("Zmień tekst");
        notific = new Label("  Wiadomość wysłana  ");
        buttonMenu = new Button("Menu");
        NativeButton buttonInside = new NativeButton("OK");
        Notification notification = new Notification(notific, buttonInside);

        add(textFieldNo, textFieldMessage, buttonSendSms, changeText,buttonMenu);

        textFieldNo.setPlaceholder("nr telefonu");
        textFieldMessage.setPlaceholder("Wiadomość...");

        buttonMenu.addClickListener(e -> UI.getCurrent().navigate(MenuView.class));

        buttonSendSms.addClickListener(buttonClickEvent -> {
            modemConnection.sendSMS(textFieldNo.getValue(), textFieldMessage.getValue());

            notification.setDuration(2000);
            buttonInside.addClickListener(event -> notification.close());
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        });
    }
}

