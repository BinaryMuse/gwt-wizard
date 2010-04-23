package net.binarymuse.gwt.client.ui.wizard.view.impl;

import net.binarymuse.gwt.client.ui.wizard.Wizard;
import net.binarymuse.gwt.client.ui.wizard.Wizard.Display;
import net.binarymuse.gwt.client.ui.wizard.event.handler.HandlerFactory;
import net.binarymuse.gwt.client.ui.wizard.view.HasIndexedWidgets;
import net.binarymuse.gwt.client.ui.wizard.view.HasWizardButtons;
import net.binarymuse.gwt.client.ui.wizard.view.HasWizardTitles;
import net.binarymuse.gwt.client.ui.wizard.view.widget.WizardDeckPanel;
import net.binarymuse.gwt.client.ui.wizard.view.widget.WizardNavigationPanel;
import net.binarymuse.gwt.client.ui.wizard.view.widget.WizardPageList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The default implementation of {@link Display}.
 * <p>
 * WizardView provides some CSS selectors to assist in
 * styling it:
 * <ul>
 * <li><code>.hasBorder</code> is applied to every element that has a border</li>
 * <li><code>.wizardTitle</code> is applied to the panel that contains the caption</li>
 * <li><code>.wizardPages</code> is applied to the panel that contains the list of page titles</li>
 * <li><code>.wizardNagivation</code> is applied to the panel that contains the naviation buttons</li>
 * <li><code>.wizardContent</code> is applied to the panel that contains the main wizard content</li>
 * </ul>
 * @author Brandon Tilley
 *
 */
public class WizardView extends Composite implements Display {

    private final DockLayoutPanel outerPanel;

    private final HorizontalPanel titlePanel;
    private final HTML titleHtml;
    private final Image workingIndicatorImage;
    private final ScrollPanel pageNamePanelContainer;
    private final WizardPageList pageNamePanel;
    private final WizardDeckPanel pagePanel;
    private final WizardNavigationPanel navigationPanel;

    private final HandlerFactory<Display> handlers;

    private static final WizardViewImages images = GWT.create(WizardViewImages.class);

    public WizardView()
    {
        // Handlers
        handlers = new HandlerFactory(this);

        // outer panel
        this.outerPanel = new DockLayoutPanel(Unit.PX);
        initWidget(this.outerPanel);

        // title panel and contained html
        this.titlePanel = new HorizontalPanel();
        this.titlePanel.addStyleName("wizardTitle");
        this.titlePanel.addStyleName("hasBorder");

        this.titleHtml = new HTML("&nbsp;");
        this.titleHtml.setWidth("100%");
        this.titlePanel.add(this.titleHtml);
        this.workingIndicatorImage = new Image(images.workingIndicator());
        workingIndicatorImage.setVisible(false);
        this.titlePanel.add(workingIndicatorImage);
        titlePanel.setWidth("100%");

        // list of page names on the left
        this.pageNamePanelContainer = new ScrollPanel();
        this.pageNamePanelContainer.addStyleName("wizardPages");
        this.pageNamePanel = new WizardPageList();
        this.pageNamePanelContainer.add(this.pageNamePanel);

        // the navigation panel at the bottom
        this.navigationPanel = new WizardNavigationPanel();
        this.navigationPanel.addStyleName("wizardNavigation");

        // the main content panel
        this.pagePanel = new WizardDeckPanel();
        this.pagePanel.addStyleName("wizardContent");
        this.pagePanel.setAnimationEnabled(true);

        // add all the panels
        this.outerPanel.addNorth(this.titlePanel, 33);
        this.outerPanel.addWest(this.pageNamePanelContainer, 165);
        this.outerPanel.addSouth(this.navigationPanel, 25);
        this.outerPanel.add(this.pagePanel);
        this.outerPanel.getWidgetContainerElement(this.navigationPanel);
        this.outerPanel.getWidgetContainerElement(this.titlePanel).addClassName("valignMiddle");

        // add borders directly to the TD's
        // each element also gets "hasBorder" so injected styles
        // can override the color, width, etc. of the borders in one selector
        this.titlePanel.getElement().getParentElement().addClassName("bottomBorder");
        this.titlePanel.getElement().getParentElement().addClassName("hasBorder");
        this.pageNamePanelContainer.getElement().getParentElement().addClassName("rightBorder");
        this.pageNamePanelContainer.getElement().getParentElement().addClassName("hasBorder");
        this.navigationPanel.getElement().getParentElement().addClassName("topBorder");
        this.navigationPanel.getElement().getParentElement().addClassName("hasBorder");
    }

    @Override
    public HasWizardButtons getButtonBar() {
        return this.navigationPanel;
    }

    @Override
    public HasWizardTitles getPageList() {
        return this.pageNamePanel;
    }

    @Override
    public HasHTML getCaption() {
        return this.titleHtml;
    }

    @Override
    public HasIndexedWidgets getContent() {
        return this.pagePanel;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void startProcessing() {
        workingIndicatorImage.setVisible(true);
    }

    @Override
    public void stopProcessing() {
        workingIndicatorImage.setVisible(false);
    }

    @Override
    public HandlerFactory<Display> getHandlerFactory() {
        return handlers;
    }

}
