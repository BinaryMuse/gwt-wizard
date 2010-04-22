package net.binarymuse.gwt.client.ui.wizard;


/**
 * A WizardPageHelper is an object that is injected into
 * a {@link WizardPage} (via {@link WizardPage#onPageAdd(WizardPageHelper)})
 * to allow the page to access {@link Wizard} specific data, including
 * its {@link WizardContext}. A WizardPageHelper should never need
 * to be instantiated in your code.
 * @author Brandon Tilley
 *
 * @param <C> the {@link WizardContext} subclass type
 */
public class WizardPageHelper<C extends WizardContext> {

    private final Wizard<C> wizard;

    /**
     * Constructs a new WizardPageHelper.
     * @param parentWizard A reference to the parent {@link Wizard}
     */
    public WizardPageHelper(Wizard<C> parentWizard) {
        this.wizard = parentWizard;
    }

    /**
     * Return the {@link Wizard}'s {@link WizardContext}.
     * @return the {@link Wizard}'s {@link WizardContext}
     */
    public C getContext() {
        return this.wizard.getContext();
    }

    /**
     * Return the {@link Wizard} that owns this helper.
     * @return the {@link Wizard} that owns this helper
     */
    public Wizard<C> getWizard() {
        return this.wizard;
    }

}
