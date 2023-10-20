public class Employee extends Utilisateur {
    Service service;
    public Employee (){
        super();
    }
    public void createAccount(username,password){
        Employee employee = new Employee(username,password);
    }
    public void selectService(String service){
        this.service.serviceType = service ;
    }
    public void setHeureTravail(Time heureDeTravail){
        this.service.heureDeTravail=heureDeTravail;
    }
    public void visualiserDemande(){
    }
    public void approuverDemande(){
    }
    public void rejectDemande(){
    }


}