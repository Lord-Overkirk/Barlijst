import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class CheckListener implements TableModelListener {

    @Override
    public void tableChanged(TableModelEvent e) {
        TableModel tableModel = (TableModel) e.getSource();
        int changed = e.getLastRow();
        if (changed == -1) {
            return;
        } 
        if ((boolean)tableModel.getValueAt(changed, 4)) {
            Customer selected = BarLijst.getCustomer((int) tableModel.getValueAt(changed, 0));
            selected.select(true);
            BarLijst.selectedCustomers.add(selected);
        } else if (!(boolean)tableModel.getValueAt(changed, 4)) {
            Customer unselected = BarLijst.getCustomer((int) tableModel.getValueAt(changed, 0));
            unselected.select(false);
            BarLijst.selectedCustomers.removeIf(c -> (c.getUid() == unselected.getUid()));
        }
        // System.out.println("Selected customers:");
        // System.out.println(BarLijst.customers.get(0).getBalance());
        // System.out.println(BarLijst.selectedCustomers.get(0).getBalance());
    }       
}