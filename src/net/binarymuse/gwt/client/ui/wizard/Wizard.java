package net.binarymuse.gwt.client.ui.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.binarymuse.gwt.client.ui.wizard.WizardPage.PageID;
import net.binarymuse.gwt.client.ui.wizard.event.NavigationEvent;
import net.binarymuse.gwt.client.ui.wizard.event.handler.WizardHandlers;
import net.binarymuse.gwt.client.ui.wizard.exception.DuplicatePageException;
import net.binarymuse.gwt.client.ui.wizard.view.HasIndexedWidgets;
import net.binarymuse.gwt.client.ui.wizard.view.HasWizardButtonMethods;
import net.binarymuse.gwt.client.ui.wizard.view.HasWizardButtons;
import net.binarymuse.gwt.client.ui.wizard.view.HasWizardTitles;
import net.binarymuse.gwt.client.ui.wizard.view.LazyCompsite;
import net.binarymuse.gwt.client.ui.wizard.view.WidgetDisplay;
import net.binarymuse.gwt.client.ui.wizard.view.impl.WizardView;
import net.binarymuse.gwt.client.ui.wizard.view.widget.WizardDeckPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.LazyPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Wizard provides a flexible step-by-step GWT widget.
 * It provides its own view, but you can provide your own
 * by extending the {@link Display} interface and passing
 * it to Wizard's constructor.
 * @author Brandon Tilley
 *
 * @param <C> the {@link WizardContext} subclass type
 */
public class Wizard<C extends WizardContext> extends Composite {

    /**
     * Wizard.Display defines a Wizard's visual display
     * (or "view" in MVP terms). {@link Wizard} comes with its
     * own implementation of Display ({@link WizardView}),
     * but you may provide your own if you wish via
     * {@link Wizard#Wizard(String, WizardContext, Display)}.
     * @author Brandon Tilley
     *
     */
    public interface Display extends WidgetDisplay {
        /**
         * The {@link Wizard}'s title is handled by an object that implements
         * GWT's <code>HasHTML</code>.
         * @return implementation of GWT's <code>HasHTML</code> for the {@link Wizard}'s title
         */
        HasHTML getCaption();
        /**
         * The {@link Wizard} maintains a list of its pages via an
         * implementation of {@link HasWizardTitles}.
         * @return implementation of {@link HasWizardTitles} for the {@link Wizard}'s page titles
         */
        HasWizardTitles getPageList();
        /**
         * The {@link Wizard}'s button bar is an implementation of
         * {@link HasWizardButtons}. If you implement your own button
         * bar, you can remove certain buttons (e.g., "Cancel")
         * by returning <code>null</code> in their getter functions (e.g.
         * {@link HasWizardButtons#getCancelButton()}.
         * @return implementation of {@link HasWizardButtons} for the {@link Wizard}'s button bar
         */
        HasWizardButtons getButtonBar();
        /**
         * The main content area is implemented with a GWT DeckPanel.
         * To keep this decision from being forced upon user-created
         * views, we extend DeckPanel into {@link WizardDeckPanel},
         * which implements {@link HasIndexedWidgets}. If you use a
         * panel that doesn't implement GWT's IndexedPanel, you have to
         * keep track of index-to-Widget relationships yourself,
         * since {@link Wizard} uses <code>getContent().showWidget(int index)</code>.
         * @return implementation of {@link HasIndexedWidgets} for the {@link Wizard}'s main content area
         */
        HasIndexedWidgets getContent();
        /**
         * Indicate to the view that work is being done. The
         * default view shows an animated "working" icon.
         */
        void startProcessing();
        /**
         * Indicate to the view that work is no longer being
         * done (used after calling {@link #startProcessing()}.
         */
        void stopProcessing();
        /**
         * {@link Wizard} is a composite that. <code>asWidget()</code>
         * returns the Widget that the Wizard will wrap using
         * <code>initWidget</code>.
         * @return the Widget the {@link Wizard} Composite will wrap
         */
        Widget asWidget();
    }

    /**
     * Defines the four types of Buttons a {@link Wizard}
     * can contain. Used in
     * @author Brandon Tilley
     *
     */
    public enum ButtonType {
        BUTTON_CANCEL,
        BUTTON_FINISH,
        BUTTON_PREVIOUS,
        BUTTON_NEXT
    }

    // helper objects
    protected C context;
    protected final WizardPageHelper<C> helper;
    protected final WizardHandlers<Wizard<C>> handlers;
    protected boolean buttonOverride = false;

    // data objects
    protected List<WizardPage<C>> pages;
    protected Map<WizardPage<C>,WizardPage<C>> pageLinks;
    protected int pageNum = -1;
    protected List<Integer> shownPages;
    protected boolean useLazyPageLoading;

    // ui objects
    protected Display display;

    /**
     * Construct a new Wizard, specifying a caption and
     * {@link WizardContext}.
     * @param caption the caption for the Wizard
     * @param context the contet for the Wizard
     */
    public Wizard(String caption, C context) {
        this(caption, context, null);
    }

    /**
     * Construct a new Wizard, specifying a caption,
     * {@link WizardContext}, and custom {@link Display}.
     * @param caption the caption for the Wizard
     * @param context the contet for the Wizard
     * @param view the custom {@link Display} for the Wizard
     */
    public Wizard(String caption, C context, Display view) {
        this.context = context;
        helper = new WizardPageHelper<C>(this);
        handlers = new WizardHandlers<Wizard<C>>(this);

        pages = new LinkedList<WizardPage<C>>();
        pageLinks = new HashMap<WizardPage<C>,WizardPage<C>>();
        shownPages = new ArrayList<Integer>();
        useLazyPageLoading = false;

        // initialize view now, as setCaption depends on it.
        display = (view != null) ? view : new WizardView();

        setCaption(caption);
        initUi();
    }

    private void initUi() {
        // wrap the display in the composite
        initWidget(display.asWidget());
        this.setStylePrimaryName("muse-gwt-Wizard");

        display.getButtonBar().getNextButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showNextPage();
            }
        });
        display.getButtonBar().getPreviousButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showPreviousPage();
            }
        });
    }

    /**
     * Returns the Wizard page being displayed.
     * @return the page being displayed, or <code>null</code> if none
     */
    public WizardPage<C> getCurrentPage() {
        return (pageNum != -1) ? pages.get(pageNum) : null;
    }

    /**
     * Convenience method to get the {@link PageID} of the
     * current {@link WizardPage}.
     * @return the {@link PageID} of the current {@link WizardPage}, or <code>null</code> if none
     */
    public WizardPage.PageID getCurrentPageID() {
        WizardPage<C> currentPage = getCurrentPage();
        return (currentPage != null) ? currentPage.getPageID() : null;
    }

    /**
     * Show a specific page, firing all {@link WizardPage} hooks.
     * @param pageId the {@link PageID} of the page to display
     */
    public void showPage(WizardPage.PageID pageId) {
        showPage(pageId, true, true, true);
    }

    /**
     * Show a specific page, firing only specific {@link WizardPage} hooks.
     * @param pageId the {@link PageID} of the page to display
     * @param fireBeforeNext whether or not to call {@link WizardPage#beforeNext(NavigationEvent)}
     * @param fireAfterNext whether or not to call {@link WizardPage#afterNext()}
     * @param fireBeforeShow whether or not to call {@link WizardPage#beforeShow()}
     */
    public void showPage(WizardPage.PageID pageId, boolean fireBeforeNext, boolean fireAfterNext,
            boolean fireBeforeShow) {
        WizardPage<C> page = getPageByPageID(pageId);
        if(page != null)
            showPage(pages.indexOf(page), fireBeforeNext, fireAfterNext, fireBeforeShow);
    }

    /**
     * Show a page based on its numeric index in the list,
     * firing all {@link WizardPage} hooks.
     * @param num the index of the page
     */
    protected void showPage(int num) {
        showPage(num, true, true, true);
    }

    /**
     * Show a page based on its numeric index in the list,
     * firing only specific {@link WizardPage} hooks. Contains
     * the logic for actually showing a specific page--all other
     * page changing methods should call this method.
     * @param num the index of the page
     * @param fireBeforeNext whether or not to call {@link WizardPage#beforeNext(NavigationEvent)}
     * @param fireAfterNext whether or not to call {@link WizardPage#afterNext()}
     * @param fireBeforeShow whether or not to call {@link WizardPage#beforeShow()}
     */
    protected void showPage(int num, boolean fireBeforeNext, boolean fireAfterNext,
            boolean fireBeforeShow) {

        // First, make sure the page we want to get to even exists
        if(pages.size() <= num) {
            throw new IndexOutOfBoundsException();
        }

        final WizardPage<C> currentPage = getCurrentPage();
        final WizardPage<C> targetPage = pages.get(num);
        final WizardPage.PageID currentPageId;
        if(currentPage == null)
            currentPageId = null;
        else
            currentPageId = currentPage.getPageID();

        final NavigationEvent event = new NavigationEvent(this, currentPageId, targetPage.getPageID());
        event.setFireBeforeNext(fireBeforeNext);
        event.setFireAfterNext(fireAfterNext);
        event.setFireBeforeShow(fireBeforeShow);

        // Before we can leave the current page, fire beforeNext
        // if it cancels the event, we return.
        if(currentPage != null && event.getFireBeforeNext()) {
            currentPage.beforeNext(event);
            if(!event.isAlive())
                return;
        }

        // beforeNext() is the only method that can cancel the event
        // it is also the only method that can redirect it
        WizardPage.PageID newDestination = event.getDestinationPage();
        if(newDestination != targetPage.getPageID()) {
            // beforeNext() changed the destination page.
            showPage(event.getDestinationPage(), event.getFireBeforeNext(),
                    event.getFireAfterNext(), event.getFireBeforeShow());
            return;
        }

        // If the target page was never shown before, call
        // beforeFirstShow() and add it to the list of previously
        // shown pages so we don't call it a second time.
        Integer targetPageNum = num;
        if(!shownPages.contains(targetPageNum)) {
            targetPage.beforeFirstShow();
            shownPages.add(targetPageNum);
        }

        // fire beforeShow
        if(event.getFireBeforeShow()) {
            targetPage.beforeShow();
        }

        // Show the page
        display.getContent().showWidget(num);
        display.getPageList().setCurrentPage(targetPage.getTitle());

        // fire afterNext on the last page after a delay so we don't get
        // glitches during the animation
        if(currentPage != null && event.getFireAfterNext()) {
            Timer t = new Timer() {
                @Override
                public void run() {
                    currentPage.afterNext();
                }
            };
            t.schedule(500);
        }
        pageNum = num;

        // Set the values of our buttons based on
        // the current page position
        calculateButtonStates();
    }

    /**
     * Show the next page, firing all {@link WizardPage} hooks.
     */
    public void showNextPage() {
        showNextPage(true, true, true);
    }

    /**
     * Show the next page, firing only specific {@link WizardPage} hooks.
     * @param fireBeforeNext whether or not to call {@link WizardPage#beforeNext(NavigationEvent)}
     * @param fireAfterNext whether or not to call {@link WizardPage#afterNext()}
     * @param fireBeforeShow whether or not to call {@link WizardPage#beforeShow()}
     */
    public void showNextPage(boolean fireBeforeNext, boolean fireAfterNext,
            boolean fireBeforeShow) {
        showPage(getNextPageID(), fireBeforeNext, fireAfterNext, fireBeforeShow);
    }

    /**
     * Show the previous page.
     */
    public void showPreviousPage() {
        showPage(getPreviousPageID(), false, false, true);
    }

    /**
     * Get the {@link PageID} of the next page.
     * @return the {@link PageID} of the next page, or <code>null</code> if none.
     */
    public WizardPage.PageID getNextPageID() {
        WizardPage<C> page = getCurrentPage();
        if(pageLinks.containsKey(page)) {
           return pageLinks.get(page).getPageID();
        } else if(pageNum == pages.size() - 1) {
            // return null if this is the last page
            return null;
        } else {
            return pages.get(pageNum + 1).getPageID();
        }
    }

    /**
     * Get the {@link PageID} of the previous page.
     * @return the {@link PageID} of the previous page, or <code>null</code> if none.
     */
    public WizardPage.PageID getPreviousPageID() {
        WizardPage<C> page = getCurrentPage();
        if(pageLinks.containsValue(page)) {
            Iterator<Entry<WizardPage<C>, WizardPage<C>>> iter = pageLinks.entrySet().iterator();
            while(iter.hasNext()) {
                Entry<WizardPage<C>, WizardPage<C>> entry = iter.next();
                if(entry.getValue() == page) {
                    return entry.getKey().getPageID();
                }
            }
        } else if(pageNum == -1 || pageNum == 0) {
            // return null if this is the first page,
            // or if the current page is not defined
            return null;
        } else {
            return pages.get(pageNum - 1).getPageID();
        }

        return null; // should never get this, but to make the compiler happy
    }

    /**
     * This method enables or disables the previous and
     * next buttons based on the current page position and
     * whether there are any pages before or after
     * the current page. The user can request that this
     * functionality be disabled for the <em>next page change</em>
     * by passing true to {@link #setButtonOverride(boolean)}.
     */
    protected void calculateButtonStates() {
        // if the user has requested that they be allowed
        // to override button states this transition, return,
        // but take control back next time.
        if(buttonOverride) {
            buttonOverride = false;
            return;
        }

        if(pages.size() == pageNum + 1) {
            display.getButtonBar().getNextButton().setEnabled(false);
        } else {
            display.getButtonBar().getNextButton().setEnabled(true);
        }

        if(pageNum == 0) {
            display.getButtonBar().getPreviousButton().setEnabled(false);
        } else {
            display.getButtonBar().getPreviousButton().setEnabled(true);
        }
    }

    /**
     * Retrieve a {@link WizardPage} using the page's {@link PageID}.
     * @param pageId the {@link PageID} to look up
     * @return the {@link WizardPage} specified by the {@link PageID}
     */
    protected WizardPage<C> getPageByPageID(WizardPage.PageID pageId) {
        Iterator<WizardPage<C>> iter = pages.iterator();
        while(iter.hasNext()) {
            WizardPage<C> page = iter.next();
            WizardPage.PageID iterId = page.getPageID();
            if(iterId == pageId) {
                return page;
            }
        }

        return null;
    }

    // === Page Links ====================

    // TODO: Refactor the page link mechanism to be more flexible
    protected void createPageLink(WizardPage<C> page, WizardPage<C> otherPage) {
        destroyPageLinksToPage(otherPage);
        pageLinks.put(page, otherPage);
    }

    /**
     * Create a two-way next/previous link between two pages by
     * specifying their {@link PageID}s. You must pass the earlier (first)
     * page as the first parameter, and the later (second) page as the
     * second parameter.
     * @param pageId the {@link PageID} of the page to create a link from
     * @param otherPageId the {@link PageID} of the page to create a link to
     */
    public void createPageLink(WizardPage.PageID pageId, WizardPage.PageID otherPageId)
    {
        createPageLink(getPageByPageID(pageId), getPageByPageID(otherPageId));
    }

    protected void destroyPageLink(WizardPage<C> page) {
        if(pageLinks.containsKey(page))
            pageLinks.remove(page);
    }

    /**
     * Destroy the link from the specified page. Does not destroy reverse links.
     * @param pageId the {@link PageID} of the page to destroy links from
     * @see #destroyPageLinksToPage(WizardPage.PageID)
     */
    public void destroyPageLink(WizardPage.PageID pageId)
    {
        destroyPageLink(getPageByPageID(pageId));
    }

    protected void destroyPageLinksToPage(WizardPage<C> page) {
        if(!pageLinks.containsValue(page))
            return;

        WizardPage<C> pageToRemove = null;
        Iterator<Entry<WizardPage<C>, WizardPage<C>>> iter = pageLinks.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<WizardPage<C>, WizardPage<C>> entry = iter.next();
            if(entry.getValue() == page) {
                pageToRemove = entry.getKey();
                break;
            }
        }

        if(pageToRemove != null)
            pageLinks.remove(pageToRemove);
    }

    /**
     * Destroy reverse links from the specified page. Does not destroy forward links.
     * Called by {@link #createPageLink(WizardPage.PageID, WizardPage.PageID)}.
     * @param pageId the {@link PageID} of the page to destroy reverse links from
     * @see #destroyPageLink(WizardPage.PageID)
     */
    public void destroyPageLinksToPage(WizardPage.PageID pageId) {
        destroyPageLinksToPage(getPageByPageID(pageId));
    }

    // === WizardContext ====================

    /**
     * Sets the context.
     * @param context the context to use
     * @see WizardContext
     */
    public void setContext(C context) {
        this.context = context;
    }

    /**
     * Gets the current context.
     * @return the current context
     * @see WizardContext
     */
    public C getContext() {
        return context;
    }

    // === WizardHandlers ====================

    public WizardHandlers<Wizard<C>> getHandlerFactory() {
        return handlers;
    }

    // === WizardPages ====================

    /**
     * Adds a {@link WizardPage} to the Wizard.
     */
    public void addPage(final WizardPage<C> page) throws DuplicatePageException {
        // make sure the page hasn't been added already
        if(pages.contains(page)) {
            throw new DuplicatePageException();
        }

        // add the title for so addPageTitle()
        // doesn't fail due to a duplicate title.
        addPageTitle(page.getTitle());
        // logical addition
        pages.add(page);
        // add the content
        if(useLazyPageLoading) { // TODO: Adding content in Wizard#addPage should depend on view for DOM attachment
            LazyPanel p = new LazyPanel() {
                @Override
                protected Widget createWidget() {
                    return page.asWidget();
                }
            };
            display.getContent().add(p);
        } else {
            display.getContent().add(page.asWidget());
        }
        // callback to inject the helper
        page.onPageAdd(helper);

        // show the page if it's the only one
        if(pages.size() == 1) {
            showPage(0);
        }

        calculateButtonStates();
    }

    /**
     * Get a list of the pages that have been added to the Wizard.
     * @return a <code>List</code> of the pages that have been added to the Wizard
     */
    public List<WizardPage<C>> getPages() {
        return pages;
    }

    /**
     * Adds a page title to the list of page titles.
     * <code>null</code> or an empty string results in
     * no title being added (since this function is
     * used by {@link #addPage(WizardPage)}). Also,
     * each title can only appear in the list once
     * (so that multiple pages may be added, even if
     * only one page will actually be shown due to
     * logic in the {@link WizardPage}s).
     * @param title the title to add to the list
     */
    public void addPageTitle(String title) {
        // Add the title to the list... maybe.
        boolean addTitle = true;
        if(title == null || title.isEmpty())
            addTitle = false;
        Iterator<WizardPage<C>> iter = pages.iterator();
        while(iter.hasNext() && addTitle) {
            WizardPage<C> pageToCheck = iter.next();
            String titleToCheck = pageToCheck.getTitle();
            if(titleToCheck.equals(title)) {
                addTitle = false;
            }
        }
        if(addTitle)
            display.getPageList().addPage(title);
    }

    /**
     * Whether or not lazy page loading and DOM attachment is enabled.
     * @return <code>true</code> if lazy loading is enabled, <code>false</code> otherwise
     * @see LazyCompsite
     */
    public boolean getUseLazyPageLoading() {
        return useLazyPageLoading;
    }

    /**
     * Turn lazy page loading and DOM attachment on or off.
     * @param useLazyPageLoading <code>true</code> to enable lazy loading, <code>false</code> to disable it
     * @see LazyCompsite
     */
    public void setUseLazyPageLoading(boolean useLazyPageLoading) {
        this.useLazyPageLoading = useLazyPageLoading;
    }

    // === Caption ====================

    /**
     * Sets the text in the Wizard's title bar.
     * @param caption the caption to use for the Wizard's title bar
     * @see #getCaption()
     */
    public void setCaption(String caption) {
        display.getCaption().setHTML(caption);
    }

    /**
     * Gets the text in the Wizard's title bar.
     * @return the Wizard's caption
     * @see #setCaption(String)
     */
    public String getCaption() {
        return display.getCaption().getHTML();
    }

    // === Buttons ====================

    /**
     * Changes the visible status of one of the Wizard's buttons.
     * @param button the {@link ButtonType} of the button to modify
     * @param visible whether or not the button should be visible
     */
    public void setButtonVisible(ButtonType button, boolean visible) {
        HasWizardButtonMethods gwtButton = getButton(button);
        if(gwtButton != null)
            gwtButton.setVisible(visible);
    }

    /**
     * Changes the enabled status of one of the Wizard's buttons.
     * @param button the {@link ButtonType} of the button to modify
     * @param enabled whether or not the button should be enabled
     */
    public void setButtonEnabled(ButtonType button, boolean enabled) {
        HasWizardButtonMethods gwtButton = getButton(button);
        if(gwtButton != null)
            gwtButton.setEnabled(enabled);
    }

    /**
     * Click one of the Wizard's buttons programatically.
     * @param button the {@link ButtonType} of the button to click
     */
    public void clickButton(ButtonType button) {
        HasWizardButtonMethods gwtButton = getButton(button);
        if(gwtButton == null)
            return;

        gwtButton.click();
    }

    /**
     * Get one of the Wizard's button's {@link HasWizardButtonMethods} implementation.
     * @param type the {@link ButtonType} of the button to return {@link HasWizardButtonMethods} implementation for
     * @return the GWT Button specified by <code>type</code>
     */
    public HasWizardButtonMethods getButton(ButtonType type) {
        switch(type) {
        case BUTTON_CANCEL:
            return display.getButtonBar().getCancelButton();
        case BUTTON_FINISH:
            return display.getButtonBar().getFinishButton();
        case BUTTON_NEXT:
            return display.getButtonBar().getNextButton();
        case BUTTON_PREVIOUS:
            return display.getButtonBar().getPreviousButton();
        default:
            return null;
        }
    }

    /**
     * Turning on the Wizard's button override means that
     * the Wizard will not attempt to automatically calculate
     * button states based on the current page's position after
     * the next page change. This is used for {@link WizardPage#beforeShow()}
     * and other {@link WizardPage} callbacks that need to manually modify
     * the button state. The {@link WizardPage} must <strong>completely</strong>
     * handle the buttons' states after setting this to <code>true</code>.
     * This value is reset to <code>false</code> after each page change.
     * @param override whether or not to override the button state detection during the next page change
     */
    public void setButtonOverride(boolean override) {
        this.buttonOverride = override;
    }

    // === Working Indicator ====================

    /**
     * Indicates whether or not the Wizard is busy processing data
     * (e.g., busy with network communication, etc). This method
     * simply calls {@link Display#startProcessing()} and
     * {@link Display#stopProcessing()} as appropriate. The
     * status of the Wizard's buttons are not changed.
     * @param working whether or not the Wizard is busy
     * @see #setButtonEnabled(ButtonType, boolean)
     */
    public void setWorking(boolean working) {
        if(working)
            this.display.startProcessing();
        else
            this.display.stopProcessing();
    }
}
