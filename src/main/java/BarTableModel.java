import javax.swing.table.DefaultTableModel;

public class BarTableModel extends DefaultTableModel {
    BarTableModel() {
        super(new String[] { "ID", "Name", "Present" }, 0);
    }

    BarTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Integer.class;
            case 4:
                return Boolean.class;
            default:
                return String.class;
        }
    }
}
