import java.util.Scanner;

  public class Client extends Utilisateurs {

        public Client (String username, String password){
            super (username, password, "client");
            
        }

        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

        //Get username from user
        System.out.println("Veuillez entrer votre nom d'utilisateur");
        String username = scanner.nextLine();

        //Get password from user
        System.out.println("Veuillez entrer votre nom mot de passe");
        String password = scanner.nextLine();

        // Verify if the client is already registered in the database and log him in
        // Else register him
        Client myClient = new Client(username, password);
        boolean isConnected = myClient.seConnecter(username, password);

        if (isConnected) {
            System.out.println("Bienvenue" + username + " Vous êtes connecté en tant que client");
        } else {
            System.out.println("Nom d'utilisateur ou mot de passe incorrect.");
        }
        scanner.close();
    
}
