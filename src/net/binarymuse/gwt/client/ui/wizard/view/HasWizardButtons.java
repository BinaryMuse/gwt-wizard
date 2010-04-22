package net.binarymuse.gwt.client.ui.wizard.view;


/**
 * An interface that indicates that the implementing class
 * contains four button-like elements:
 * <ul>
 * <li>Cancel</li>
 * <li>Finish</li>
 * <li>Previous</li>
 * <li>Next</li>
 * </ul>
 * The interface provides methods for getting the
 * {@link HasWizardButtonMethods} implementation
 * of the Widgets.
 * @author Brandon Tilley
 *
 */
public interface HasWizardButtons {
    public HasWizardButtonMethods getCancelButton();
    public HasWizardButtonMethods getFinishButton();
    public HasWizardButtonMethods getPreviousButton();
    public HasWizardButtonMethods getNextButton();
}
