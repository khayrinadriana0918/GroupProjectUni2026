
/**
 * Write a description of class AnalystInfo here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;
public class AnalystInfo{
    private String analystId;
    private String analystName;
    private String expertiseArea;
    private List<IncidentInfo> incidents;

    public AnalystInfo(String analystId,String analystName,String expertiseArea){
        this.analystId = analystId;
        this.analystName = analystName;
        this.expertiseArea = expertiseArea;
        this.incidents = new LinkedList<>();
    }

    public void addIncident(IncidentInfo incident) { 
        incidents.add(incident); 
    }

    public String getAnalystId(){
        return analystId;
    }

    public String getAnalystName(){ //for queue display
        return analystName;
    }

    public String getExpertiseArea(){ //for queue display
        return expertiseArea;
    }

    public List<IncidentInfo> getIncidents(){
        return incidents;
    }
    //requirement for queue display
    public double getTotalImpactCost(){
        double total = 0;
        for(IncidentInfo i: incidents){
            total += i.getImpactCost();
        }
        return total;
    }
}
