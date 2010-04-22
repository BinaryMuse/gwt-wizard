package net.binarymuse.gwt.client.ui.wizard;

/**
 * A WizardContext is a shared object that is passed
 * from page to page via a {@link WizardPageHelper}.
 * It is used to store a set of data that multiple
 * pages of the {@link Wizard} may access.
 * @author Brandon Tilley
 *
 */
public abstract class WizardContext {

    /**
     * Indicates whether or not the {@link Wizard} is complete.
     * @return <code>true</code> if the {@link Wizard} is complete, <code>false</code> otherwise
     */
    public abstract boolean isComplete();

    /**
     * Called when the {@link Wizard} is cancelled.
     * Should fully reset the context so that the {@link Wizard}
     * can be run again.
     */
    public abstract void reset();

}
