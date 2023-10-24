public class Employee extends Utilisateurs {
    Service service;
    public Employee (String username , String password, String role){
        super(username, password,role="Employee");
    }
    public void createAccount(String username,String password){
        Employee employee = new Employee(username,password,"Employee");
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
