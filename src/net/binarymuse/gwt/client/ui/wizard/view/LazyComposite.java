package net.binarymuse.gwt.client.ui.wizard.view;

import net.binarymuse.gwt.client.ui.wizard.Wizard;
import net.binarymuse.gwt.client.ui.wizard.WizardPage;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A LazyComposite is a Composite you can use to create
 * the views for your {@link WizardPage}'s that supports lazy
 * loading and DOM attachment. For use with
 * {@link Wizard#setUseLazyPageLoading(boolean)}.
 *
 * If you use this class, you should not call <code>initWidget</code>
 * in {@link #createWidget()}--instead, return the Widget
 * you want wrapped from the method instead.
 * @author Brandon Tilley
 *
 */
public abstract class LazyComposite extends Composite implements WidgetDisplay {

    private boolean widgetCreated = false;

    /**
     * Creates the Composite's widget with {@link #createWidget()}
     * if it hasn't been created and wraps it with <code>initWidget()</code>.
     * @return the Composite (<code>this</code>).
     */
    @Override
    public final Widget asWidget() {
        if(!widgetCreated) {
            initWidget(createWidget());
            widgetCreated = true;
        }

        return this;
    }

    /**
     * Lazily create the widget and return it to be wrapped
     * by {@link #asWidget()}.
     * @return the Widget to be wrapped by the composite.
     */
    public abstract Widget createWidget();

}
