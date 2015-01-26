/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.nbdemetra.kix.actions;

import ec.nbdemetra.kix.KIXDocument;
import ec.nbdemetra.kix.KIXDocumentManager;
import ec.nbdemetra.ui.nodes.SingleNodeAction;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.nodes.ItemWsNode;
import ec.tstoolkit.timeseries.regression.ITsVariable;
import ec.tstoolkit.utilities.IDynamicObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "KIX",
        id = "ec.nbdemetra.kix.actions.RefreshAction")
@ActionRegistration(displayName = "#CTL_RefreshAction", lazy = false)
@ActionReferences({
    @ActionReference(path = KIXDocumentManager.ITEMPATH, position = 1700, separatorBefore = 1699)
})
@Messages("CTL_RefreshAction=Refresh")
public final class RefreshAction extends SingleNodeAction<ItemWsNode> {

    public RefreshAction() {
        super(ItemWsNode.class);
    }

    @Override
    protected void performAction(ItemWsNode context) {
        WorkspaceItem<KIXDocument> cur = (WorkspaceItem<KIXDocument>) context.getItem();
        if (cur != null && !cur.isReadOnly()) {
            for (ITsVariable var : cur.getElement().getIndices().variables()) {
                if (var instanceof IDynamicObject) {
                    IDynamicObject dvar = (IDynamicObject) var;
                    dvar.refresh();
                }
            }
            for (ITsVariable var : cur.getElement().getWeights().variables()) {
                if (var instanceof IDynamicObject) {
                    IDynamicObject dvar = (IDynamicObject) var;
                    dvar.refresh();
                }
            }
        }
    }

    @Override
    protected boolean enable(ItemWsNode context) {
        WorkspaceItem<?> cur = context.getItem();
        return cur != null && !cur.isReadOnly();
    }

    @Override
    public String getName() {
        return Bundle.CTL_RefreshAction();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}
