package net.binarymuse.gwt.client.ui.wizard.view;

import net.binarymuse.gwt.client.ui.wizard.Wizard.Display;

import com.google.gwt.user.client.ui.Widget;

/**
 * The base display interface for {@link Display} and
 * other views that must expose themselves as a GWT Widget.
 * @author Brandon Tilley
 *
 */
public interface WidgetDisplay {
    /**
     * Get this display as a GWT Widget.
     * @return a GWT Widget to attach to the DOM.
     */
    public Widget asWidget();
}
