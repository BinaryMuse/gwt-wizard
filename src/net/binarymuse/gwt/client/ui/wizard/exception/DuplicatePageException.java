package net.binarymuse.gwt.client.ui.wizard.exception;

import net.binarymuse.gwt.client.ui.wizard.Wizard;
import net.binarymuse.gwt.client.ui.wizard.WizardPage;

/**
 * Indicates that the {@link WizardPage} being added to the {@link Wizard}
 * has already been added.
 * @author Brandon Tilley
 *
 */
public class DuplicatePageException extends RuntimeException {

    private static final long serialVersionUID = -2121855831796245251L;

}
