import java.util.*;

public class IncidentRouting {

    // Using Java's built-in Queue interface with LinkedList
    private Queue<AnalystInfo> internalQueue = new LinkedList<>();  // Queue 1
    private Queue<AnalystInfo> externalQueue = new LinkedList<>();  // Queue 2
    private Queue<AnalystInfo> criticalQueue = new LinkedList<>();  // Queue 3

    private List<AnalystInfo> analystList;

    public IncidentRouting(List<AnalystInfo> analystList) {
        this.analystList = analystList;
    }

    // Route analysts based on incident count
    public void routeAnalysts() {
        boolean assignToInternal = true;

        for (AnalystInfo analyst : analystList) {
            if (analyst.getIncidents().size() > 3) {
                criticalQueue.add(analyst);
                System.out.println(analyst.getAnalystName() + " -> Critical Queue");
            } else {
                if (assignToInternal) {
                    internalQueue.add(analyst);
                    System.out.println(analyst.getAnalystName() + " -> Internal Queue");
                } else {
                    externalQueue.add(analyst);
                    System.out.println(analyst.getAnalystName() + " -> External Queue");
                }
                assignToInternal = !assignToInternal;
            }
        }
    }

    // Helper method to display queue details
    public  List<String> getQueueDetails(Queue<AnalystInfo> queue) {
        List<String> details = new ArrayList<>();

        if (queue.isEmpty()) {
            details.add("Queue is empty");
            return details;
        }

        for (AnalystInfo analyst : queue) {

            StringBuilder text = new StringBuilder();
            text.append("\nAnalyst ID " ).append(analyst.getAnalystId()).append("\n");
            text.append("  Name: ").append(analyst.getAnalystName()).append("\n");
            text.append("  Expertise: ").append(analyst.getExpertiseArea()).append("\n");
            text.append("  Assigned Incidents:\n ");

            for(IncidentInfo incident : analyst.getIncidents()){
                text.append("-").append(incident.getIncidentId())
                .append(" (").append(incident.getIncidentType()).append(")\n");
           text.append("-Impact Cost: ").append(incident.getImpactCost()).append("\n");
            }

            double totalCost = analyst.getTotalImpactCost();
            
            text.append("TOTAL IMPACT COST: RM ").append(String.format("%.2f",totalCost)).append("\n");

            text.append("-----------------------------");
            details.add(text.toString());
        }

        return details;
    }

    // Getters for Phase 3
    public Queue<AnalystInfo> getInternalQueue() { 
        return internalQueue; 
    }

    public Queue<AnalystInfo> getExternalQueue() { 
        return externalQueue; 
    }

    public Queue<AnalystInfo> getCriticalQueue() { 
        return criticalQueue;
    }
}