package net.binarymuse.gwt.client.ui.wizard.view.impl;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * A GWT CLientBundle that defines the images
 * used in the default view, {@link WizardView}.
 * @author Brandon Tilley
 *
 */
public interface WizardViewImages extends ClientBundle {

    /**
     * Indicates that the Wizard is web 2.0 compatable.
     */
    @Source("ajax-loader.gif")
    public ImageResource workingIndicator();

}
