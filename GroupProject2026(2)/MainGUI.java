
/**
 * Write a description of class Main here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MainGUI extends JFrame {

    private IncidentRouting routing;

    private JPanel internalPanel, externalPanel, criticalPanel;

    private Stack<AnalystInfo> resolvedStack = new Stack<>();

    public MainGUI() {

        setTitle("SOC Incident Management");
        setSize(1000, 700);
        setLayout(new BorderLayout());

        // ===== Panels =====
        internalPanel = createPanel("Internal Queue");
        externalPanel = createPanel("External Queue");
        criticalPanel = createPanel("Critical Queue");

        JPanel centerPanel = new JPanel(new GridLayout(1, 3));
        centerPanel.add(new JScrollPane(internalPanel));
        centerPanel.add(new JScrollPane(externalPanel));
        centerPanel.add(new JScrollPane(criticalPanel));

        add(centerPanel, BorderLayout.CENTER);

        // ===== Buttons =====
        JButton loadBtn = new JButton("Load File");
        JButton showBtn = new JButton("Show Queue");
        JButton nextBtn = new JButton("Next Queue");
        JButton stackBtn = new JButton("Show Completed");

        JPanel btnPanel = new JPanel();
        btnPanel.add(loadBtn);
        btnPanel.add(showBtn);
        btnPanel.add(nextBtn);
        btnPanel.add(stackBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // ===== Actions =====

        loadBtn.addActionListener(e -> loadData());

        showBtn.addActionListener(e -> {
                    displayQueue(routing.getInternalQueue(), internalPanel);
                    displayQueue(routing.getExternalQueue(), externalPanel);
                    displayQueue(routing.getCriticalQueue(), criticalPanel);
            });

        nextBtn.addActionListener(e -> processNextBatch());

        stackBtn.addActionListener(e -> showResolvedStack());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ===== Load Data =====
    private void loadData() {

        LinkedList<AnalystInfo> analystList =
            CaseReader.readFromFile("cyber_incidents.txt");

        routing = new IncidentRouting(analystList);

        routing.routeAnalysts();

        JOptionPane.showMessageDialog(this, "File Loaded & Queues Created!");
    }

    // ===== Display Queue (max 5 only) =====
    private void displayQueue(Queue<AnalystInfo> queue, JPanel panel) {

        panel.removeAll();

        java.util.List<String> details =routing.getQueueDetails(queue);

        int count = 0;

        for (String text : details) {

            if (count == 5) break;

            JTextArea area = new JTextArea(text);
            area.setEditable(false);

            panel.add(area);
            count++;
        }

        panel.revalidate();
        panel.repaint();
    }

    // ===== Process Next Batch =====
    private void processNextBatch() {

        processQueue(routing.getInternalQueue());
        processQueue(routing.getExternalQueue());
        processQueue(routing.getCriticalQueue());

        displayQueue(routing.getInternalQueue(), internalPanel);
        displayQueue(routing.getExternalQueue(), externalPanel);
        displayQueue(routing.getCriticalQueue(), criticalPanel);
    }

    // ===== Process 5 analysts =====
    private void processQueue(Queue<AnalystInfo> queue) {

        int count = 0;

        while (!queue.isEmpty() && count < 5) {

            AnalystInfo analyst = queue.poll();

            System.out.println("Processing: " + analyst.getAnalystName());
            // push into stack (completed)
            resolvedStack.push(analyst);

            count++;
        }
    }
    //display stack
    private void showResolvedStack() {

        if(resolvedStack.isEmpty()){
            JOptionPane.showMessageDialog(this," No completed analysts yet.");
            return;
        }

        StringBuilder text = new StringBuilder();

        Stack<AnalystInfo> temp = new Stack<>();

        text.append("\n=== COMPLETED ANALYSTS (STACK) ===\n");

        while (!resolvedStack.isEmpty()) {

            AnalystInfo analyst = resolvedStack.pop();
            temp.push(analyst);

            text.append(" Name: ").append(analyst.getAnalystName()).append("\n");
            text.append(" Expertise: ").append(analyst.getExpertiseArea()).append("\n");

            double total = 0;

            for (IncidentInfo i : analyst.getIncidents()) {
                text.append(" - ").append(i.getIncidentId()).append(" (").append(i.getIncidentType()).append(")")
                .append(" Severity Lv.(").append(i.getSeverityLevel()).append(")\n")
                .append(" Report Date : ").append(i.getReportDate()).append("\n")
                .append(" Est. Resolution Time(Hour): ").append(i.getERT()).append("\n");
                total += i.getImpactCost();
            }

            text.append(" Total Cost: RM ").append(String.format("%.2f",total)).append("\n\n");
            text.append("--------------------------\n");
        }
        //restore stack
        while(!temp.isEmpty()){
            resolvedStack.push(temp.pop());
        }
        JTextArea area = new JTextArea(text.toString());
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this,scroll,"Resolved Stack",JOptionPane.INFORMATION_MESSAGE);
    }
    // ===== Panel helper =====
    private JPanel createPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    public static void main(String[] args) {
        new MainGUI();
    }
}
