package net.binarymuse.gwt.client.ui.wizard.event;

import net.binarymuse.gwt.client.ui.wizard.Wizard;
import net.binarymuse.gwt.client.ui.wizard.WizardPage;
import net.binarymuse.gwt.client.ui.wizard.WizardPage.PageID;

/**
 * An event passed to the {@link WizardPage} callbacks.
 * {@link WizardPage#beforeNext(NavigationEvent)} can cancel
 * the navigation by calling {@link #cancel()}, and can
 * redirect to a new page by calling {@link NavigationEvent#setDestinationPage(WizardPage.PageID)}.
 * @author Brandon Tilley
 *
 */
public class NavigationEvent {

    private boolean alive;
    private boolean fireBeforeNext;
    private boolean fireAfterNext;
    private boolean fireBeforeShow;
    private WizardPage.PageID sourcePage;
    private WizardPage.PageID destinationPage;

    /**
     * Construct a new NavigationEvent with a reference to the
     * parent {@link Wizard} and the source and destination
     * {@link WizardPage}s.
     * @param wizard the containing {@link Wizard}
     * @param source the {@link PageID} of the source {@link WizardPage}
     * @param destination the {@link PageID} of the destination {@link WizardPage}
     */
    public NavigationEvent(Wizard<?> wizard, WizardPage.PageID source, WizardPage.PageID destination) {
        alive = true;
        setFireBeforeNext(true);
        setFireAfterNext(true);
        setFireBeforeShow(true);
        this.sourcePage = source;
        setDestinationPage(destination);
    }

    /**
     * Cancels the navigation event if the event is cancelable.
     * The event is only cancelabe during {@link WizardPage#beforeNext(NavigationEvent)}.
     */
    public void cancel() {
        alive = false;
    }

    /**
     * Whether or not the event is still alive.
     * {@link #cancel()} kills the event.
     * @return <code>true</code> if the event is alive, <code>false</code> otherwise
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Returns the {@link PageID} of the source page.
     * @return the {@link PageID} of the source page
     */
    public WizardPage.PageID getSourcePage() {
        return sourcePage;
    }

    /**
     * Returns the {@link PageID} of the destination page.
     * @return the {@link PageID} of the destination page
     */
    public WizardPage.PageID getDestinationPage() {
        return destinationPage;
    }

    /**
     * Sets a new destination page. Has no effect if
     * the event is not being processed in
     * {@link WizardPage#beforeNext(NavigationEvent)}.
     * @param destinationPage
     */
    public void setDestinationPage(WizardPage.PageID destinationPage) {
        this.destinationPage = destinationPage;
    }

    // TODO: finish javadoc for NavigationEvent
    public boolean getFireBeforeNext() {
        return fireBeforeNext;
    }

    public void setFireBeforeNext(boolean fireBeforeNext) {
        this.fireBeforeNext = fireBeforeNext;
    }

    public boolean getFireAfterNext() {
        return fireAfterNext;
    }

    public void setFireAfterNext(boolean fireAfterNext) {
        this.fireAfterNext = fireAfterNext;
    }

    public boolean getFireBeforeShow() {
        return fireBeforeShow;
    }

    public void setFireBeforeShow(boolean fireBeforeShow) {
        this.fireBeforeShow = fireBeforeShow;
    }

}
