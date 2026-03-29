
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
        setSize(1000, 600);
        setLayout(new BorderLayout());

        // ===== Panels =====
        internalPanel = createPanel("Internal Queue");
        externalPanel = createPanel("External Queue");
        criticalPanel = createPanel("Critical Queue");

        JPanel centerPanel = new JPanel(new GridLayout(1, 3));
        centerPanel.add(internalPanel);
        centerPanel.add(externalPanel);
        centerPanel.add(criticalPanel);

        add(centerPanel, BorderLayout.CENTER);

        // ===== Buttons =====
        JButton loadBtn = new JButton("Load File");
        JButton showBtn = new JButton("Show Queue");
        JButton nextBtn = new JButton("Next Queue");

        JPanel btnPanel = new JPanel();
        btnPanel.add(loadBtn);
        btnPanel.add(showBtn);
        btnPanel.add(nextBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // ===== Actions =====

        loadBtn.addActionListener(e -> loadData());

        showBtn.addActionListener(e -> {
            displayQueue(routing.getInternalQueue(), internalPanel);
            displayQueue(routing.getExternalQueue(), externalPanel);
            displayQueue(routing.getCriticalQueue(), criticalPanel);
        });

        nextBtn.addActionListener(e -> processNextBatch());

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

    /*
    ===== STACK (COMMENTED AS REQUIRED) =====

    private void showResolved() {

        System.out.println("\n=== COMPLETED ANALYSTS (STACK) ===");

        while (!resolvedStack.isEmpty()) {

            AnalystInfo analyst = resolvedStack.pop();

            System.out.println("Name: " + analyst.getAnalystName());
            System.out.println("Expertise: " + analyst.getExpertiseArea());

            double total = 0;

            for (IncidentInfo i : analyst.getIncidents()) {
                System.out.println(" - " + i.getIncidentId());
                total += i.getImpactCost();
            }

            System.out.println("Total Cost: RM " + total);
            System.out.println("--------------------------");
        }
    }
    */
}
