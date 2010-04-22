package net.binarymuse.gwt.client.ui.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.binarymuse.gwt.client.ui.wizard.WizardPage.PageID;

/**
 * WizardPageLinkManager manages forward and reverse links
 * to and from different {@link WizardPage}s in a {@link Wizard}.
 * All pages are identified by their {@link PageID}. It is not
 * necessary to set up links between pages that fall naturally
 * next to each other; if no links are created via the manager,
 * pages will flow in the order they were added.
 * <p>
 * The manager manages two types of links betewen pages:
 * forward links and reverse links. If a forward link
 * is created between pageA and pageB, then clicking
 * the Wizard's "next" button while on pageA
 * will take the user to pageB; however, clicking
 * "previous" while on pageB does not necessarily
 * take the user back to pageA, unless a reverse
 * link has also been set up between pageA and pageB.
 * <p>
 * When specifying arguments to methods that take two
 * PageIDs as parameters, always list the earlier
 * page first--for example, if you want to set up a forward
 * link between pageA and pageB, and then a reverse link
 * leading from pageB to pageA, you would first call
 * <code>createForwardPageLink(pageA.getPageID(), pageB.getPageID())</code>
 * followed by
 * <code>createReversePageLink(pageA.getPageID(), pageB.getPageID())</code>
 * (note that <code>createTwoWayLink</code> is a shortcut
 * for this operation). All methods that take the PageIDs of two pages
 * as parameters should accept the earlier page first and the later page second.
 * @author Brandon Tilley
 *
 */
public class WizardPageLinkManager {

    private Map<PageID,PageID> forwardPageLinks;
    private Map<PageID,PageID> reversePageLinks;

    public WizardPageLinkManager() {
        forwardPageLinks = new LinkedHashMap<PageID,PageID>();
        reversePageLinks = new LinkedHashMap<PageID,PageID>();
    }

    /**
     * A convenience method that calls <code>createForwardLink()</code>
     * followed by <code>createReverseLink()</code>.
     * @param earlierPage
     * @param laterPage
     */
    public void createTwoWayLink(PageID earlierPage, PageID laterPage) {
        createForwardLink(earlierPage, laterPage);
        createReverseLink(earlierPage, laterPage);
    }

    /**
     * Creates a forward link from <code>earlierPage</code> to <code>laterPage</code>.
     * @param earlierPage
     * @param laterPage
     */
    public void createForwardLink(PageID earlierPage, PageID laterPage) {
        forwardPageLinks.put(earlierPage, laterPage);
    }

    /**
     * Creates a reverse link from </code>laterPage</code> to <code>earlierPage</code>.
     * @param earlierPage
     * @param laterPage
     */
    public void createReverseLink(PageID earlierPage, PageID laterPage) {
        reversePageLinks.put(laterPage, earlierPage);
    }

    /**
     * Destroys forward links originating from <code>earlierPage</code> and reverse
     * links originating from <code>laterPage</code>. If <code>verify</code> is
     * <code>true</code>, the links are only destroyed if the target page is
     * the other parameter; otherwise, the links are unconditionally broken.
     * @param earlierPage
     * @param laterPage
     * @param verify
     */
    public void destroyLinksBetween(PageID earlierPage, PageID laterPage, boolean verify) {
        if(verify) {
            if(forwardLinkExists(earlierPage, laterPage))
                destroyForwardLinkFrom(earlierPage);
            if(reverseLinkExists(earlierPage, laterPage))
                destroyReverseLinkFrom(laterPage);
        } else {
            destroyForwardLinkFrom(earlierPage);
            destroyReverseLinkFrom(laterPage);
        }
    }

    /**
     * Destroys the forward link, if any, that originates from the given page.
     * @param earlierPage the source page to destroy a forward link from
     */
    public void destroyForwardLinkFrom(PageID earlierPage) {
        forwardPageLinks.remove(earlierPage);
    }

    /**
     * Destroys the reverse link, if any, that originates from the given page.
     * @param laterPage the source page to destroy a reverse link from
     */
    public void destroyReverseLinkFrom(PageID laterPage) {
        reversePageLinks.remove(laterPage);
    }

    /**
     * Destroys all foward links, if any, that end at the given page.
     * @param laterPage the destination page to destroy forward links to
     */
    public void destroyForwardLinksTo(PageID laterPage) {
        Iterator<Entry<PageID,PageID>> links = forwardPageLinks.entrySet().iterator();
        while(links.hasNext()) {
            Entry<PageID,PageID> entry = links.next();
            if(entry.getValue() == laterPage) {
                links.remove();
            }
        }
    }

    /**
     * Destroys all reverse links, if any, that end at the given page.
     * @param earlierPage the destination page to destroy reverse links to
     */
    public void destroyReverseLinksTo(PageID earlierPage) {
        Iterator<Entry<PageID,PageID>> links = reversePageLinks.entrySet().iterator();
        while(links.hasNext()) {
            Entry<PageID,PageID> entry = links.next();
            if(entry.getValue() == earlierPage) {
                links.remove();
            }
        }
    }

    /**
     * Find the PageID of the page that the given page foward links to, if any.
     * @param earlierPage the source page
     * @return the page the given page forward links to, or <code>null</code> if none
     */
    public PageID getForwardLinkDestination(PageID earlierPage) {
        return forwardPageLinks.get(earlierPage);
    }

    /**
     * Find the PageID of the page that the given page reverse links to, if any.
     * @param laterPage the source page
     * @return the page the given page reverse links to, or <code>null</code> if none
     */
    public PageID getReverseLinkDestination(PageID laterPage) {
        return reversePageLinks.get(laterPage);
    }

    /**
     * Find all pages that forward link to the given destination page, if any.
     * @param laterPage the destination page
     * @return a List of PageIDs that forward link to the given page
     */
    public List<PageID> getForwardLinkSources(PageID laterPage) {
        List<PageID> sources = new ArrayList<PageID>(forwardPageLinks.size());
        Iterator<Entry<PageID,PageID>> links = forwardPageLinks.entrySet().iterator();
        while(links.hasNext()) {
            Entry<PageID,PageID> entry = links.next();
            if(entry.getValue() == laterPage) {
                sources.add(entry.getValue());
            }
        }

        return sources;
    }

    /**
     * Find all pages that reverse link to the given destination page, if any.
     * @param earlierPage the destination page
     * @return a List of PageIDs that reverse link to the given page
     */
    public List<PageID> getReverseLinkSources(PageID earlierPage) {
        List<PageID> sources = new ArrayList<PageID>(reversePageLinks.size());
        Iterator<Entry<PageID,PageID>> links = reversePageLinks.entrySet().iterator();
        while(links.hasNext()) {
            Entry<PageID,PageID> entry = links.next();
            if(entry.getValue() == earlierPage) {
                sources.add(entry.getValue());
            }
        }

        return sources;
    }

    /**
     * Gets whether <code>earlierPage</code> forward links to <code>laterPage</code>
     * @param earlierPage the source page
     * @param laterPage the destination page
     * @return <code>true</code> if <code>earlierPage</code> forward links to <code>laterPage</code>, <code>false</code> otherwise
     */
    public boolean forwardLinkExists(PageID earlierPage, PageID laterPage) {
        return (forwardPageLinks.get(earlierPage) == laterPage) ? true : false;
    }

    /**
     * Gets whether <code>laterPage</code> reverse links to <code>earlierPage</code>
     * @param earlierPage the destination page
     * @param laterPage the source page
     * @return <code>true</code> if <code>laterPage</code> reverse links to <code>earlierPage</code>, <code>false</code> otherwise
     */
    public boolean reverseLinkExists(PageID earlierPage, PageID laterPage) {
        return (reversePageLinks.get(laterPage) == earlierPage) ? true : false;
    }

}
