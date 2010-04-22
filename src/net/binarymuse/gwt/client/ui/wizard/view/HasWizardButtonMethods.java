package net.binarymuse.gwt.client.ui.wizard.view;

import net.binarymuse.gwt.client.ui.wizard.Wizard;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * An interface that indicates that the
 * implementing class has button-like properties.
 * Since custom views may not use actual GWT
 * Buttons, this interface marks any Widgets
 * that will be used as buttons for the {@link Wizard}.
 * @author Brandon Tilley
 *
 */
public interface HasWizardButtonMethods extends HasClickHandlers {

    /**
     * Whether or not the button is enabled.
     * @return <code>true</code> if enabled, <code>false</code> otherwise
     */
    public boolean isEnabled();
    /**
     * Set the enabled state of the button.
     * @param enabled <code>true</code> to enable the button, <code>false</code> to disable it
     */
    public void setEnabled(boolean enabled);
    /**
     * Whether or not the button is visible.
     * @return <code>true</code> if visible, <code>false</code> otherwise
     */
    public boolean isVisible();
    /**
     * Set the visibility of the button.
     * @param visible <code>true</code> to show the button, <code>false</code> to hide it
     */
    public void setVisible(boolean visible);
    /**
     * Simulate a click on the button.
     */
    public void click();

}
