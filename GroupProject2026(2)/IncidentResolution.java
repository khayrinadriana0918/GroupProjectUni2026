
/**
 * Write a description of class IncidentResolution here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;
public class IncidentResolution
{
    private Stack<AnalystInfo> resolvedStack;
    
    public IncidentResolution()
    {
        resolvedStack = new Stack<>();
    }
    
    public void processIncidents(Queue<AnalystInfo> internalQueue,
                                 Queue<AnalystInfo> externalQueue,
                                 Queue<AnalystInfo> criticalQueue)
    {
       int batchNum = 1;
        
        // Repeat until all queues are empty
        while (!internalQueue.isEmpty() || !externalQueue.isEmpty() || !criticalQueue.isEmpty()) {
            
            System.out.println("\n========== BATCH " + batchNum + " ==========");
            
            serveFromQueue(internalQueue, "Internal Queue", 5);
            serveFromQueue(externalQueue, "External Queue", 5);
            serveFromQueue(criticalQueue, "Critical Queue", 5);
            
            batchNum++;
        }
        
        System.out.println("\n================================================================");
        System.out.println("ALL QUEUES ARE EMPTY");
        System.out.println("================================================================");
        
        displayResolvedStack();
    }
    
    /**
     * Serve analysts from a queue (FIFO)
     */
    private void serveFromQueue(Queue<AnalystInfo> queue, String queueName, int batchSize)
    {
        System.out.println("\n--- " + queueName + " ---");
        System.out.println("Analysts waiting: " + queue.size());
        
        if (queue.isEmpty()) {
            System.out.println("No analysts in this queue.");
            return;
        }
        
        int served = 0;
        
        while (!queue.isEmpty() && served < batchSize) {
            
            AnalystInfo analyst = queue.poll(); // FIFO
            
            System.out.println("\nServing: " + analyst.getAnalystName() + " (" + analyst.getAnalystId() + ")");
            System.out.println("  Expertise: " + analyst.getExpertiseArea());
            System.out.println("  Number of incidents: " + analyst.getIncidents().size());
            
            double totalImpact = completeIncidents(analyst);
            
            resolvedStack.push(analyst); // LIFO
            System.out.println("  >> PUSHED to resolvedStack (Stack size: " + resolvedStack.size() + ")");
            
            served++;
        }
        
        System.out.println("\nServed " + served + " analyst(s) from " + queueName);
        
        if (queue.isEmpty()) {
            System.out.println(queueName + " is now EMPTY.");
        }
    }
    
    /**
     * Complete all incidents for an analyst
     */
    private double completeIncidents(AnalystInfo analyst)
    {
        List<IncidentInfo> incidents = analyst.getIncidents();
        double totalImpact = 0;
        
        System.out.println("  Incidents handled:");
        
        for (int i = 0; i < incidents.size(); i++) {
            IncidentInfo inc = incidents.get(i);
            totalImpact += inc.getImpactCost();
            
            System.out.printf("    %d. %s - %s (RM %,.2f)%n",
                (i + 1),
                inc.getIncidentId(),
                inc.getIncidentType(),
                inc.getImpactCost());
        }
        
        System.out.printf("  Total impact cost resolved: RM %,.2f%n", totalImpact);
        
        return totalImpact;
    }
    
    /**
     * Display resolved stack (LIFO)
     */
    private void displayResolvedStack()
    {
        System.out.println("\n================================================================");
        System.out.println("RESOLVED STACK (LIFO Order - Most Recent First)");
        System.out.println("================================================================");
        
        if (resolvedStack.isEmpty()) {
            System.out.println("No analysts in resolvedStack.");
            return;
        }
        
        Stack<AnalystInfo> tempStack = new Stack<>();
        int position = 1;
        double grandTotal = 0;
        
        System.out.println();
        
        while (!resolvedStack.isEmpty()) {
            AnalystInfo analyst = resolvedStack.pop();
            tempStack.push(analyst);
            
            System.out.println("[" + position + "] " + analyst.getAnalystName() + " (" + analyst.getAnalystId() + ")");
            System.out.println("    Expertise Area: " + analyst.getExpertiseArea());
            System.out.println("    Incidents Handled: " + analyst.getIncidents().size());
            
            // show incident details
            System.out.println("    Incident Details:");
            for (IncidentInfo inc : analyst.getIncidents()) {
                System.out.printf("      - %s (%s) RM %,.2f%n",
                    inc.getIncidentId(),
                    inc.getIncidentType(),
                    inc.getImpactCost());
            }
            double totalCost= analyst.getTotalImpactCost();
            
            System.out.printf("    Total Impact Cost Resolved: RM %,.2f%n",totalCost);
            System.out.println();
            
            grandTotal += totalCost;
            position++;
        }
        
        // Restore stack
        while (!tempStack.isEmpty()) {
            resolvedStack.push(tempStack.pop());
        }
        
        System.out.println("================================================================");
        System.out.printf("GRAND TOTAL IMPACT COST: RM %,.2f%n", grandTotal);
        System.out.println("================================================================");
    }
    
    public Stack<AnalystInfo> getResolvedStack()
    {
        return resolvedStack;
    }
}
