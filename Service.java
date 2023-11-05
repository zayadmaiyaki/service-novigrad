import java.sql.Time;
import java.text.DateFormat;

public class Service {
    
    private String serviceType ; 
    private Time heureDeTravail;
    private List<String> informationsClientRequises; 
    private List<String> documentsRequis;
    
    public Service(String serviceType){
        this.serviceType=serviceType;
    }

     public Service(String serviceType, Time heureDeTravail, List<String> informationsClientRequises, List<String> documentsRequis) {
        this.serviceType = serviceType;
        this.heureDeTravail = heureDeTravail;
        this.informationsClientRequises = informationsClientRequises;
        this.documentsRequis = documentsRequis;
    
    }


}
