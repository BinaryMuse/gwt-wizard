package net.binarymuse.gwt.client.ui.wizard;

import net.binarymuse.gwt.client.ui.wizard.event.NavigationEvent;

import com.google.gwt.user.client.ui.Widget;

/**
 * WizardPage is an abstract class that you can extend
 * to create your wizard pages. WizardPage contains
 * several useful methods that are called at certain
 * points in the lifetime of your {@link Wizard}.
 * @author Brandon Tilley
 *
 * @param <C> the {@link WizardContext} subclass type
 */
public abstract class WizardPage<C extends WizardContext> {

    /**
     * PageID is a class used to identify a page.
     * Implementations of {@link WizardPage} should
     * return it in {@link WizardPage#getPageID()}.
     * Pages with the same PageID should refer to the
     * same WizardPage instance.
     * @author Brandon Tilley
     *
     */
    public static class PageID {
        private static int nextHashCode;
        private final int index;

        public PageID() {
            index = ++nextHashCode;
        }

        public final int hashCode() {
            return index;
        }
    }

    /**
     * The {@link WizardContext} for this page.
     */
    protected C context;

    /**
     * The {@link Wizard} that owns this page.
     */
    protected Wizard<C> wizard;

    /**
     * Returns the {@link WizardPage.PageID} associated with this page.
     * @return the {@link WizardPage.PageID} associated with this page
     */
    public abstract PageID getPageID();

    /**
     * Gets the title of the page to use in the navigation view.
     * Use <code>null</code> or an empty string to keep this
     * page from showing up in the list of pages; any pages
     * with the same name will only be shown in the list once.
     * This is useful for pages that are the result of a branch
     * in logic.
     * @return the title of the page
     */
    public abstract String getTitle();

    /**
     * Returns a {@link Widget} to use as the visual layout for
     * the page.
     * @return a {@link Widget} to show in the {@link Wizard}'s main frame
     */
    public abstract Widget asWidget();

    /**
     * Called when the page is added to the Wizard, providing
     * a {@link WizardPageHelper}. The base WizardPage class
     * stores the parent {@link Wizard} and {@link WizardContext}
     * for later retrieval via {@link #getWizard()} and
     * {@link #getContext()}--if you override this method,
     * be sure to handle this yourself.
     * @param helper the Wizard's {@link WizardPageHelper}
     */
    public void onPageAdd(WizardPageHelper<C> helper) {
        this.context = helper.getContext();
        this.wizard = helper.getWizard();
    }

    /**
     * Returns the {@link WizardContext} for this page.
     * @return the {@link WizardContext} for this page
     */
    public C getContext() { return this.context; }

    /**
     * Returns the {@link Wizard} that owns this page.
     * @return the {@link Wizard} that owns this page
     */
    public Wizard<C> getWizard() { return this.wizard; }

    /**
     * Called before the first time a page is shown.
     * Allows for any setup the page may want to do.
     */
    public void beforeFirstShow() {}

    /**
     * Called just before each time the page is shown.
     * Allows the page to set up any elements based on
     * the current {@link WizardContext}. This function is
     * called before the prior page's {@link #afterNext()}.
     */
    public void beforeShow() {}

    /**
     * Called when the "Next" button is clicked, but
     * before the page is actually changed. Allows the
     * page to do any validation, etc. Call
     * {@link NavigationEvent#cancel()} to cancel the page change.
     */
    public void beforeNext(final NavigationEvent event) {}

    /**
     * Called after the page has disappeared due to the {@link Wizard}
     * moving to the next page. Allows the page to do any UI cleanup
     * without strange screen artifacts. This function should not
     * do any modification of the {@link WizardContext} that the next
     * page will need during its {@link #beforeShow()}, as
     * {@link #beforeShow()} is called before <code>afterNext()</code>.
     */
    public void afterNext() {}

}
