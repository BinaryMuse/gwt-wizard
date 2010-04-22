package net.binarymuse.gwt.client.ui.wizard.event.handler;

import net.binarymuse.gwt.client.ui.wizard.Wizard;
import net.binarymuse.gwt.client.ui.wizard.Wizard.ButtonType;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

public class WizardHandlers<W extends Wizard<?>> {

    private W wizard;

    private final KeyPressHandler enterKeyNextHandler = new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent event) {
            if(event.getCharCode() == KeyCodes.KEY_ENTER)
                wizard.clickButton(ButtonType.BUTTON_NEXT);
        }
    };

    public WizardHandlers(W wizard) {
        this.wizard = wizard;
    }

    public KeyPressHandler getEnterKeyNextHandler() {
        return enterKeyNextHandler;
    }

}
