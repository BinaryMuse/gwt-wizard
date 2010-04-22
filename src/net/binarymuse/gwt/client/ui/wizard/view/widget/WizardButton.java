package net.binarymuse.gwt.client.ui.wizard.view.widget;

import net.binarymuse.gwt.client.ui.wizard.view.HasWizardButtonMethods;

import com.google.gwt.user.client.ui.Button;

/**
 * An extension of the GWT Button
 * that implements {@link HasWizardButtonMethods}.
 * @author Brandon Tilley
 *
 */
public class WizardButton extends Button implements HasWizardButtonMethods {

    public WizardButton() {
        super();
    }

    public WizardButton(String html) {
        super(html);
    }

}
