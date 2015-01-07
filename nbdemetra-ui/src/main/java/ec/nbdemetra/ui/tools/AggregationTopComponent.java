/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.nbdemetra.ui.tools;

import ec.nbdemetra.ui.NbComponents;
import ec.tss.Ts;
import ec.tss.TsFactory;
import ec.tss.TsInformationType;
import ec.tss.TsStatus;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.ui.chart.JTsChart;
import ec.ui.interfaces.ITsCollectionView;
import ec.ui.list.JTsList;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JSplitPane;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//ec.nbdemetra.ui.tools//Aggregation//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "AggregationTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "ec.nbdemetra.ui.tools.AggregationTopComponent")
@ActionReference(path = "Menu/Tools" , position = 332)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_AggregationAction")
@Messages({
    "CTL_AggregationAction=Aggregation",
    "CTL_AggregationTopComponent=Aggregation Window",
    "HINT_AggregationTopComponent=This is an Aggregation window"
})
public final class AggregationTopComponent extends TopComponent {

    private final JSplitPane mainPane;
    private final JTsList inputList;
    private final JTsChart aggChart;
    
    public AggregationTopComponent() {
        initComponents();
        setName(Bundle.CTL_AggregationTopComponent());
        setToolTipText(Bundle.HINT_AggregationTopComponent());
        inputList = new JTsList();
        initList();
        aggChart = new JTsChart();
        aggChart.setTsUpdateMode(ITsCollectionView.TsUpdateMode.None);
        mainPane = NbComponents.newJSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputList, aggChart);

        setLayout(new BorderLayout());
        add(mainPane, BorderLayout.CENTER);
        mainPane.setDividerLocation(.5);
        mainPane.setResizeWeight(.5);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
        aggChart.connect();
        inputList.connect();
    }

    @Override
    public void componentClosed() {
        aggChart.dispose();
        inputList.dispose();
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private void initList() {
        inputList.addPropertyChangeListener(JTsList.COLLECTION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                TsData sum = null;
                for (Ts s : inputList.getTsCollection()) {
                    if (s.hasData() == TsStatus.Undefined) {
                        s.load(TsInformationType.Data);
                    }
                    sum = TsData.add(sum, s.getTsData());
                }
                Ts t = TsFactory.instance.createTs("Total", null, sum);
                aggChart.getTsCollection().replace(t);
            }
        });
    }
 }