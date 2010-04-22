package net.binarymuse.gwt.client.ui.wizard.view.widget;

import java.util.ArrayList;
import java.util.List;

import net.binarymuse.gwt.client.ui.wizard.Wizard.Display;
import net.binarymuse.gwt.client.ui.wizard.view.HasWizardTitles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;

/**
 * The default implementation of {@link Display#getPageList()}.
 * @author Brandon Tilley
 *
 */
public class WizardPageList extends Composite implements HasWizardTitles {

    public interface WizardPageListImages extends ClientBundle {
        @Source("arrow_right.png")
        ImageResource indicator();
    }

    private WizardPageListImages images = GWT.create(WizardPageListImages.class);

    private final FlexTable table;
    private List<String> pageNames;
    private final Image arrow = new Image(images.indicator());

    public WizardPageList() {
        table = new FlexTable();
        initWidget(table);
        pageNames = new ArrayList<String>();
    }

    public void addPage(String name) {
        if(!pageNames.contains(name))
            pageNames.add(name);

        int index = pageNames.indexOf(name);
        table.setHTML(index, 1, name);
        table.getCellFormatter().setVerticalAlignment(index, 0, HasVerticalAlignment.ALIGN_MIDDLE);
        table.getCellFormatter().setVerticalAlignment(index, 1, HasVerticalAlignment.ALIGN_TOP);
        table.getCellFormatter().addStyleName(index, 1, "inactive");
    }

    public void setCurrentPage(String name) {
        // some pages may not have titles
        if(pageNames.indexOf(name) == -1) {
            return;
        }

        int index = pageNames.indexOf(name);
        for(int i = 0; i < pageNames.size(); i++) {
            if(i <= index)
                table.getCellFormatter().removeStyleName(i, 1, "inactive");
            else
                table.getCellFormatter().addStyleName(i, 1, "inactive");

            if(i == index) {
                table.remove(arrow);
                table.setWidget(i, 0, arrow);
            }
        }
    }

}
