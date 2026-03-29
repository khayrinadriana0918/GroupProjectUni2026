
/**
 * Write a description of class IncidentInfo here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class IncidentInfo
{
    private String incidentId;
    private String incidentType;
    private int severityLevel; 
    private String reportDate;
    private String estimatedResolutionTime;
    private double impactCost;
    
    public IncidentInfo(String incidentId,String incidentType,int severityLevel,String reportDate,String estimatedResolutionTime,double impactCost){
        this.incidentId = incidentId;
        this.incidentType = incidentType;
        this.severityLevel = severityLevel;
        this.reportDate = reportDate;
        this.estimatedResolutionTime = estimatedResolutionTime;
        this.impactCost = impactCost;
    }
    //for queue display
    public String getIncidentId() { 
        return incidentId; 
    }
    public String getIncidentType() {
        return incidentType; 
    }
    public int getSeverityLevel() {
        return severityLevel; 
    }
    public String getReportDate() {
        return reportDate; 
    }
    public String getERT() { //ERT = estimatedResolutionTime
        return estimatedResolutionTime; 
    }
    public double getImpactCost() {
        return impactCost; 
    }
}
