package net.binarymuse.gwt.client.ui.wizard.event.handler;

import net.binarymuse.gwt.client.ui.wizard.Wizard;
import net.binarymuse.gwt.client.ui.wizard.WizardPage;

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
public class HandlerFactory<D extends Wizard.Display> {

    private D display;

    private final KeyPressHandler enterKeyNextHandler = new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent event) {
            if(event.getCharCode() == KeyCodes.KEY_ENTER)
                display.getButtonBar().getNextButton().click();
        }
    };

    /**
     * Construct a new HandlerFactory, providing the parent
     * {@link Wizard}.
     * @param wizard the parent {@link Wizard}
     */
    public HandlerFactory(D display) {
        this.display = display;
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
