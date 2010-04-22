package net.binarymuse.gwt.client.ui.wizard.view;

import net.binarymuse.gwt.client.ui.wizard.WizardPage;

/**
 * An interface that indicates that the implementing
 * class has the ability to add and remove {@link WizardPage} titles
 * (as {@link String}s) to and from its display and also has the ability
 * to indicate that a specific page title is the currently
 * active one.
 * @author Brandon Tilley
 *
 */
public interface HasWizardTitles {

    /**
     * Adds a page title.
     * @param title the title of the page to add
     */
    public void addPage(String title);

    /**
     * Sets a page title as the currently active title.
     * @param title the title to set as active
     */
    public void setCurrentPage(String title);

}
