package com.subtrack.client.ui.subscription;

import com.subtrack.client.config.RmiClientConfig;
import com.subtrack.client.service.SubscriptionClientService;
import com.subtrack.client.service.impl.SubscriptionClientServiceImpl;
import com.subtrack.client.ui.dashboard.DashboardFrame;
import com.subtrack.server.rmi.dto.SubscriptionDto;

// OpenPDF imports
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SubscriptionFrame extends JFrame {

    private final JTextField txtName = new JTextField(15);
    private final JTextField txtAmount = new JTextField(10);
    private final JTextField txtCycle = new JTextField(10);
    private final JTextField txtNextDate = new JTextField(10);

    private final JButton btnAdd = new JButton("Add");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnRefresh = new JButton("Refresh");
    private final JButton btnExportPdf = new JButton("Export PDF");
    private final JButton btnExportCsv = new JButton("Export CSV");
    private final JButton btnBack = new JButton("Back");

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Name", "Amount", "Cycle", "Next Payment"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);

    private final Integer currentUserId;
    private final String currentUsername;

    private SubscriptionClientService subscriptionClientService;

    public SubscriptionFrame(Integer currentUserId, String currentUsername) {
        this.currentUserId = currentUserId;
        this.currentUsername = currentUsername;

        setTitle("Manage Subscriptions - " + currentUsername + " (ID: " + currentUserId + ")");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initService();
        initUi();
        initActions();

        // Initially disable until a row is selected
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        loadTable(false); // initial load without popup
    }

    private void initService() {
        try {
            subscriptionClientService = new SubscriptionClientServiceImpl(
                    RmiClientConfig.getSubscriptionRemoteService()
            );
        } catch (Exception e) {
            subscriptionClientService = null;
            JOptionPane.showMessageDialog(
                    this,
                    "Cannot connect to SubscriptionService.\n" + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void initUi() {
        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        form.add(new JLabel("Name"));
        form.add(new JLabel("Amount"));
        form.add(new JLabel("Billing Cycle"));
        form.add(new JLabel("Next Payment (yyyy-MM-dd)"));
        form.add(txtName);
        form.add(txtAmount);
        form.add(txtCycle);
        form.add(txtNextDate);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnRefresh);
        buttons.add(btnExportCsv);   // CSV button added
        buttons.add(btnExportPdf);
        buttons.add(btnBack);

        JScrollPane sp = new JScrollPane(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLayout(new BorderLayout(10, 10));
        add(form, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void initActions() {
        btnAdd.addActionListener(e -> doAdd());
        btnUpdate.addActionListener(e -> doUpdate());
        btnDelete.addActionListener(e -> doDelete());
        btnRefresh.addActionListener(e -> loadTable(true));
        btnExportPdf.addActionListener(e -> doExportPdf());
        btnExportCsv.addActionListener(e -> doExportCsv()); // CSV action wired

        btnBack.addActionListener(e -> {
            new DashboardFrame(currentUserId, currentUsername).setVisible(true);
            dispose();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = table.getSelectedRow();

            boolean hasSelection = row >= 0;
            btnUpdate.setEnabled(hasSelection);
            btnDelete.setEnabled(hasSelection);

            if (hasSelection) {
                txtName.setText(String.valueOf(model.getValueAt(row, 1)));
                txtAmount.setText(String.valueOf(model.getValueAt(row, 2)));
                txtCycle.setText(String.valueOf(model.getValueAt(row, 3)));
                txtNextDate.setText(String.valueOf(model.getValueAt(row, 4)));
            }
        });
    }

    private void doAdd() {
        if (subscriptionClientService == null) {
            JOptionPane.showMessageDialog(this, "Subscription service not connected.");
            return;
        }
        if (!validateInput()) return;

        SubscriptionDto dto = new SubscriptionDto(
                null,
                currentUserId,
                txtName.getText().trim(),
                Double.parseDouble(txtAmount.getText().trim()),
                txtCycle.getText().trim().toUpperCase(),
                txtNextDate.getText().trim()
        );

        String response = subscriptionClientService.create(dto);
        if (response != null && response.startsWith("SUB_CREATE_OK")) {
            JOptionPane.showMessageDialog(this, "Subscription created successfully.");
            clearForm();
            loadTable(false);
        } else {
            JOptionPane.showMessageDialog(this, "Create failed: " + response);
        }
    }

    private void doUpdate() {
        if (subscriptionClientService == null) {
            JOptionPane.showMessageDialog(this, "Subscription service not connected.");
            return;
        }

        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }
        if (!validateInput()) return;

        Integer id = Integer.parseInt(String.valueOf(model.getValueAt(row, 0)));

        SubscriptionDto dto = new SubscriptionDto(
                id,
                currentUserId,
                txtName.getText().trim(),
                Double.parseDouble(txtAmount.getText().trim()),
                txtCycle.getText().trim().toUpperCase(),
                txtNextDate.getText().trim()
        );

        String response = subscriptionClientService.update(dto);
        if (response != null && response.startsWith("SUB_UPDATE_OK")) {
            JOptionPane.showMessageDialog(this, "Subscription updated successfully.");
            clearForm();
            loadTable(false);
        } else {
            JOptionPane.showMessageDialog(this, "Update failed: " + response);
        }
    }

    private void doDelete() {
        if (subscriptionClientService == null) {
            JOptionPane.showMessageDialog(this, "Subscription service not connected.");
            return;
        }

        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected subscription?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        Integer id = Integer.parseInt(String.valueOf(model.getValueAt(row, 0)));
        String response = subscriptionClientService.delete(id);

        if (response != null && response.startsWith("SUB_DELETE_OK")) {
            JOptionPane.showMessageDialog(this, "Subscription deleted successfully.");
            clearForm();
            loadTable(false);
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed: " + response);
        }
    }

    private void doExportPdf() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data to export.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("subscriptions_report_" + currentUsername + ".pdf"));

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            file = new File(file.getAbsolutePath() + ".pdf");
        }

        Document document = null;
        try {
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font metaFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font rowFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            document.add(new Paragraph("SubTrack - Subscription Report", titleFont));
            document.add(new Paragraph("User: " + currentUsername + " (ID: " + currentUserId + ")", metaFont));
            document.add(new Paragraph("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), metaFont));
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(5);
            pdfTable.setWidthPercentage(100f);
            pdfTable.setWidths(new float[]{1.2f, 3.0f, 2.0f, 2.0f, 2.4f});

            addHeaderCell(pdfTable, "ID", headerFont);
            addHeaderCell(pdfTable, "Name", headerFont);
            addHeaderCell(pdfTable, "Amount", headerFont);
            addHeaderCell(pdfTable, "Cycle", headerFont);
            addHeaderCell(pdfTable, "Next Payment", headerFont);

            for (int i = 0; i < model.getRowCount(); i++) {
                pdfTable.addCell(new Phrase(String.valueOf(model.getValueAt(i, 0)), rowFont));
                pdfTable.addCell(new Phrase(String.valueOf(model.getValueAt(i, 1)), rowFont));
                pdfTable.addCell(new Phrase(String.valueOf(model.getValueAt(i, 2)), rowFont));
                pdfTable.addCell(new Phrase(String.valueOf(model.getValueAt(i, 3)), rowFont));
                pdfTable.addCell(new Phrase(String.valueOf(model.getValueAt(i, 4)), rowFont));
            }

            document.add(pdfTable);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total records: " + model.getRowCount(), metaFont));

            JOptionPane.showMessageDialog(this, "PDF exported successfully:\n" + file.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "PDF export failed: " + ex.getMessage());
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }

    private void doExportCsv() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data to export.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("subscriptions_report_" + currentUsername + ".csv"));

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("ID,Name,Amount,Cycle,Next Payment");

            for (int i = 0; i < model.getRowCount(); i++) {
                String id = csvSafe(String.valueOf(model.getValueAt(i, 0)));
                String name = csvSafe(String.valueOf(model.getValueAt(i, 1)));
                String amount = csvSafe(String.valueOf(model.getValueAt(i, 2)));
                String cycle = csvSafe(String.valueOf(model.getValueAt(i, 3)));
                String nextPayment = csvSafe(String.valueOf(model.getValueAt(i, 4)));

                pw.println(id + "," + name + "," + amount + "," + cycle + "," + nextPayment);
            }

            JOptionPane.showMessageDialog(this, "CSV exported successfully:\n" + file.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "CSV export failed: " + ex.getMessage());
        }
    }

    private String csvSafe(String value) {
        if (value == null) return "\"\"";
        String v = value.replace("\"", "\"\"");
        return "\"" + v + "\"";
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        table.addCell(cell);
    }

    private void loadTable(boolean showPopup) {
        if (subscriptionClientService == null) return;

        List<SubscriptionDto> list;
        try {
            list = subscriptionClientService.findByUser(currentUserId);
            if (list == null) list = Collections.emptyList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Refresh failed: " + e.getMessage());
            return;
        }

        model.setRowCount(0);
        for (SubscriptionDto s : list) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getAmount(),
                    s.getBillingCycle(),
                    s.getNextPaymentDate()
            });
        }

        System.out.println("DEBUG Refresh userId=" + currentUserId + ", rows=" + list.size());
        if (showPopup) {
            JOptionPane.showMessageDialog(this, "Data refreshed. Rows: " + list.size());
        }
    }

    private boolean validateInput() {
        String name = txtName.getText().trim();
        String amountStr = txtAmount.getText().trim();
        String cycle = txtCycle.getText().trim();
        String nextDate = txtNextDate.getText().trim();

        if (name.isEmpty() || amountStr.isEmpty() || cycle.isEmpty() || nextDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return false;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid number.");
            return false;
        }

        if (!nextDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Date must be in yyyy-MM-dd format.");
            return false;
        }

        if (!cycle.equalsIgnoreCase("MONTHLY")
                && !cycle.equalsIgnoreCase("YEARLY")
                && !cycle.equalsIgnoreCase("WEEKLY")) {
            JOptionPane.showMessageDialog(this, "Billing Cycle must be MONTHLY, YEARLY, or WEEKLY.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        txtName.setText("");
        txtAmount.setText("");
        txtCycle.setText("");
        txtNextDate.setText("");
        table.clearSelection();
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }
}
