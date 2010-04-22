package net.binarymuse.gwt.client.ui.wizard.view.widget;


import net.binarymuse.gwt.client.ui.wizard.Wizard.Display;
import net.binarymuse.gwt.client.ui.wizard.view.HasWizardButtonMethods;
import net.binarymuse.gwt.client.ui.wizard.view.HasWizardButtons;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * The default implementation of {@link Display#getButtonBar()}.
 * @author Brandon Tilley
 *
 */
public class WizardNavigationPanel extends Composite implements
        HasWizardButtons {

    private HorizontalPanel container;

    private WizardButton prev;
    private WizardButton next;
    private WizardButton cancel;
    private WizardButton finish;

    public WizardNavigationPanel() {
        this("< Prev", "Next >");
    }

    public WizardNavigationPanel(String prevText, String nextText) {
        this(prevText, nextText, "Cancel", "Finish");
    }

    public WizardNavigationPanel(String prevText, String nextText, String cancelText, String finishText) {
        this.container = new HorizontalPanel();
        initWidget(this.container);

        this.container.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.container.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        this.container.setSpacing(0);
        this.container.setWidth("100%");

        HTML leftSpacer = new HTML("&nbsp;");
        leftSpacer.setWidth("100%");
        this.cancel = new WizardButton(cancelText);
        this.finish = new WizardButton(finishText);
        HTML spacer = new HTML("&nbsp;");
        this.prev = new WizardButton(prevText);
        this.next = new WizardButton(nextText);

        this.container.add(leftSpacer);
        this.container.add(this.cancel);
        this.container.add(this.finish);
        this.container.add(spacer);
        this.container.add(this.prev);
        this.container.add(this.next);

        this.container.setCellWidth(leftSpacer, "100%");
        this.cancel.setWidth("75px");
        this.cancel.setHeight("25px");
        this.finish.setWidth("75px");
        this.finish.setHeight("25px");
        this.container.setCellWidth(spacer, "20px");
        this.prev.setWidth("75px");
        this.prev.setHeight("25px");
        this.next.setWidth("75px");
        this.next.setHeight("25px");
    }

    @Override
    public HasWizardButtonMethods getCancelButton() {
        return cancel;
    }

    @Override
    public HasWizardButtonMethods getFinishButton() {
        return finish;
    }

    @Override
    public HasWizardButtonMethods getNextButton() {
        return next;
    }

    @Override
    public HasWizardButtonMethods getPreviousButton() {
        return prev;
    }

}
