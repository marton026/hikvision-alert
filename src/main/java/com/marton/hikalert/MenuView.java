package com.marton.hikalert;

import com.marton.hikalert.views.HikGui;
import com.marton.hikalert.views.HikMotionGui;
import com.marton.hikalert.views.ReadSmsGui;
import com.marton.hikalert.views.SmsGui;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route(value = "appMenu")
@PageTitle("Menu")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MenuView extends VerticalLayout {

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */


    public MenuView() {


        // Use TextField for standard text input


        Button writeSmsButton = new Button("Bramka sms");
        Button readSmsButton = new Button("Odczyt sms");
        Button detectionButton = new Button("Detekcja ruchu");
        Button osdButton = new Button("Ustawienia OSD");


        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        writeSmsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        readSmsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        detectionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        osdButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        // button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        writeSmsButton.addClickListener(e -> UI.getCurrent().navigate(SmsGui.class));
        readSmsButton.addClickListener(e -> UI.getCurrent().navigate(ReadSmsGui.class));
        detectionButton.addClickListener(e -> UI.getCurrent().navigate(HikMotionGui.class));
        osdButton.addClickListener(e -> UI.getCurrent().navigate(HikGui.class));

        add(writeSmsButton, readSmsButton, detectionButton, osdButton);


    }

}
