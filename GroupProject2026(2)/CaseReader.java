
/**
 * Write a description of class CaseReader here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.io.*;
import java.util.*;

public class CaseReader
{
    public static LinkedList<AnalystInfo> readFromFile(String filename){
        //store all analysts in LinkedList
        LinkedList<AnalystInfo>analystList = new LinkedList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;

            //read line by line
            while((line=reader.readLine()) != null){
                if (line.trim().isEmpty()) {
                    continue;
                }
                //delimiter is '|'
                String[]parts = line.split("\\|");

                if (parts.length != 9){//information must have 9 parts
                    System.out.println("Invalid Data"+ line);
                    continue;
                }
                //analyst Information
                String analystId = parts[0];
                String analystName = parts[1];
                String expertiseArea = parts[2];

                //incidents Information
                String incidentId = parts[3];
                String incidentType = parts[4];
                int severityLevel = Integer.parseInt(parts[5]);
                String reportDate = parts[6];
                String estimatedResolutionTime = parts[7];
                double impactCost = Double.parseDouble(parts[8]);

                //Incident object
                IncidentInfo incident = new IncidentInfo(
                        incidentId,incidentType,severityLevel,reportDate,estimatedResolutionTime,impactCost
                    );

                //check if analyst already exists
                AnalystInfo analyst = null;

                for(AnalystInfo a: analystList){
                    if(a.getAnalystId().equals(analystId)){
                        analyst=a;
                        break;
                    }
                }
                //if analyst doesn't exist yet
                if(analyst == null){
                    //create new
                    analyst = new AnalystInfo(analystId, analystName, expertiseArea);
                    analystList.add(analyst);
                }
                //add incident to that analyst
                analyst.addIncident(incident);

            }
            /*debug print
            System.out.println("All analysts in the list:");
            for (AnalystInfo b : analystList) {
                System.out.println(b.getAnalystId() + " - " + b.getAnalystName());
                System.out.println("Expertise Area: "+ b.getExpertiseArea()+"\n");
            }
            System.out.println("Total analysts: " + analystList.size());
            */
        }catch(FileNotFoundException e){
            System.out.println("could not locate file");
        }
        catch(IOException e){
            System.out.println("Something went wrong ");
            e.printStackTrace();
        }
        return analystList;
    }
}
