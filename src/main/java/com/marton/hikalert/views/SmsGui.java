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

    private TextField textFieldNo;
    private TextField textFieldMessage;
    private Button buttonSendSms;
    private Button buttonMenu;
    private ModemConnection modemConnection;

    @Autowired
    public SmsGui(ModemConnection modemConnection) {
        this.modemConnection = modemConnection;
        textFieldNo = new TextField();
        textFieldMessage = new TextField();
        buttonSendSms = new Button("Send SMS !!!");
        buttonMenu = new Button("Menu");
        Notification notification = new Notification("  Wiadomość wysłana  ",3000, Notification.Position.MIDDLE);
        Notification notificationData =new Notification("Wprowadź brakujące dane",2000, Notification.Position.MIDDLE);

        add(textFieldNo, textFieldMessage, buttonSendSms,buttonMenu);

        textFieldNo.setPlaceholder("nr telefonu");
        textFieldMessage.setPlaceholder("Wiadomość...");

        buttonMenu.addClickListener(e -> UI.getCurrent().navigate(MenuView.class));

        buttonSendSms.addClickListener(buttonClickEvent -> {
            if (textFieldNo.getValue().isEmpty() || textFieldMessage.getValue().isEmpty()) {
                notificationData.open();
            } else {
                modemConnection.sendSMS(textFieldNo.getValue(), textFieldMessage.getValue());
                notification.open();
            }
        });
    }
}

