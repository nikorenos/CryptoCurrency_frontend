package com.vaadin.ui;

import com.vaadin.domain.Wallet;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route(value = "walletForm", layout = MainLayout.class)
public class WalletForm extends FormLayout {

    TextField name = new TextField("Name");

    Button save = new Button("Create");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Wallet> binder = new BeanValidationBinder<>(Wallet.class);
    private Wallet wallet;

    public WalletForm() {
        addClassName("wallet-form");
        binder.bindInstanceFields(this);

        add(
                name,
                createButtonsLayout()
        );
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
        binder.readBean(wallet);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, wallet)));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {

        try {
            binder.writeBean(wallet);
            fireEvent(new SaveEvent(this, wallet));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class WalletFormEvent extends ComponentEvent<WalletForm> {
        private Wallet wallet;

        protected WalletFormEvent(WalletForm source, Wallet wallet) {
            super(source, false);
            this.wallet = wallet;
        }

        public Wallet getWallet() {
            return wallet;
        }
    }

    public static class SaveEvent extends WalletFormEvent {
        SaveEvent(WalletForm source, Wallet wallet) {
            super(source, wallet);
        }
    }

    public static class DeleteEvent extends WalletFormEvent {
        DeleteEvent(WalletForm source, Wallet wallet) {
            super(source, wallet);
        }

    }

    public static class CloseEvent extends WalletFormEvent {
        CloseEvent(WalletForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
