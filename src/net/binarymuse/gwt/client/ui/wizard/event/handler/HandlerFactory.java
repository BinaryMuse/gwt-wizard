package net.binarymuse.gwt.client.ui.wizard.event.handler;

import net.binarymuse.gwt.client.ui.wizard.Wizard;
import net.binarymuse.gwt.client.ui.wizard.WizardPage;
import net.binarymuse.gwt.client.ui.wizard.Wizard.ButtonType;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

/**
 * HandlerFactory provides some common event handlers
 * for use in user-created {@link Wizard}s and {@link WizardPage}s.
 * @author Brandon Tilley
 *
 * @param <W> the {@link Wizard} type
 * (<code>W extends Wizard<?></code> matches any valid {@link Wizard})
 */
public class HandlerFactory<W extends Wizard<?>> {

    private W wizard;

    private final KeyPressHandler enterKeyNextHandler = new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent event) {
            if(event.getCharCode() == KeyCodes.KEY_ENTER)
                wizard.clickButton(ButtonType.BUTTON_NEXT);
        }
    };

    /**
     * Construct a new HandlerFactory, providing the parent
     * {@link Wizard}.
     * @param wizard the parent {@link Wizard}
     */
    public HandlerFactory(W wizard) {
        this.wizard = wizard;
    }

    /**
     * Returns a KeyPressHandler that simlulates a click on the {@link Wizard}'s
     * next button when the enter key is pressed.
     * @return GWT KeyPressHandler for moving to the next page on enter key
     */
    public KeyPressHandler getEnterKeyNextHandler() {
        return enterKeyNextHandler;
    }

}
