import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class HomePage extends CreateFile implements Executable{
    private static final int USERNAME = 1;
    private static final int PASSWORD = 2;
    private static final String ACCOUNTS_FILE = "database/accounts.csv";

    private final Scanner scanner = new Scanner(System.in);
    private final HashMap<String, Account> accounts = new HashMap<>();

    private boolean newUser = false;
    private String username = "";

    /**
     * This method is the backbone of the home page of the application, which asks the users to either
     * log in or sign up.
     */
    public void execute(){
        boolean validity = false;

        this.loadAccounts();

        System.out.println("PROGRAM: (Login) (Sign up)");

        while(!validity){
            System.out.print("    USER: ");
            String login = scanner.nextLine();

            if(login.toLowerCase(Locale.ROOT).equals("l")){
                validity = true;
                this.login();
            } else if (login.toLowerCase(Locale.ROOT).equals("r")){
                validity = true;
                this.signUp();
            }
        }


    }

    public void login(){
        boolean validity = false;

        while(!validity){
            System.out.println("PROGRAM: (username) (password)");
            System.out.print("    USER: ");
            String userChoice = scanner.nextLine();

            if(userChoice.toLowerCase(Locale.ROOT).equals("l")){
                if(this.accountLogin(USERNAME)){
                    validity = true;
                }

            } else if(userChoice.toLowerCase(Locale.ROOT).equals("r")){
                if(this.accountLogin(PASSWORD)){
                    validity = true;
                }
            }
        }
    }

    public boolean accountLogin(int x){
        boolean validity = false;
        String username;
        String password;

        if(x==1){
            System.out.print("    username: ");
            username = scanner.nextLine();

            System.out.print("    password: ");
             password = scanner.nextLine();
        } else {
            System.out.print("    password: ");
            password = scanner.nextLine();

            System.out.print("    username: ");
            username = scanner.nextLine();
        }

        if(this.checkUser(username, password)){
            System.out.println("PROGRAM: Log in success!");
            return true;
        } else {
            System.out.println("PROGRAM: Log in failed");

            while(!validity) {
                System.out.println("PROGRAM: (Try again) (Sign up)");
                System.out.print("    USER: ");
                String userChoice = scanner.nextLine();

                if (userChoice.toLowerCase(Locale.ROOT).equals("r")) {
                    this.signUp();
                    validity = true;
                } else if (userChoice.toLowerCase(Locale.ROOT).equals("l")) {
                    return false;
                } else {
                    System.out.println("PROGRAM: Error");
                }
            }
            return false;
        }
    }

    public void signUp(){
        boolean validity = false;
        boolean validEmail=false;
        String userName="";
        String userEmail = "";
        String userPw = "";

        while(!validity) {
            System.out.println("PROGRAM: Please enter your name");
            System.out.print("    USER: ");
            String scanUserName = scanner.nextLine();
            if(scanUserName.length()>0){
                validity=true;
                userName = scanUserName;
            }
        }

        validity = false;
        while(!validity){
            System.out.println("PROGRAM: Please enter your email (this will become your username)");
            System.out.print("    USER: ");
            userEmail = scanner.nextLine();

            if(accounts.containsKey(userEmail)){
                System.out.println("PROGRAM: An account with that email already exists, " +
                        "do you want to log in instead? (yes) (no)");
                System.out.print("    USER: ");
                String userChoice = scanner.nextLine();

                if(userChoice.toLowerCase(Locale.ROOT).equals("l")){
                    this.login();
                    validity=true;
                } else if (userChoice.toLowerCase(Locale.ROOT).equals("r")){
                    System.out.println("PROGRAM: Please enter another email");
                } else {
                    System.out.println("PROGRAM: Error");
                }
            } else {
                if (userEmail.length() > 0) {
                    validity=true;
                    validEmail=true;
                }
            }
        }

        // only process this if the user decides to sign up and not try to log in
        if(validEmail) {
            validity = false;
            while(!validity) {
                System.out.println("PROGRAM: Please set your password");
                System.out.print("    USER: ");
                userPw = scanner.nextLine();

                if(userPw.length()>0) {

                    System.out.println("PROGRAM: Please re-enter your password");
                    System.out.print("    USER: ");
                    String userPw2 = scanner.nextLine();

                    if (userPw2.equals(userPw)) {
                        validity = true;
                    } else {
                        System.out.println("PROGRAM: Oops! Password does not match");
                    }
                }
            }
            // create the user account
            this.createAccount(userName, userEmail, userPw);
            System.out.println("PROGRAM: Glad to have you with us, "+ userName+"!"+" We hope we could make your day" +
                    " more organized and planned well ahead!");

            this.newUser = true;
            this.username = userEmail;
        }
    }

    public void createAccount(String name, String email, String password){
        accounts.put(email, new Account(name, email, password));

        try(PrintWriter pw = new PrintWriter((new FileWriter(ACCOUNTS_FILE, false)))){
            for(String emailKey : accounts.keySet()){
                pw.println(accounts.get(emailKey).getName()+","+accounts.get(emailKey).getEmail()+","+
                        accounts.get(emailKey).getPassword());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUser(String username, String password){
        if(accounts.containsKey(username)){
            return accounts.get(username).getPassword().equals(password);
        } else {
            return false;
        }
    }

    public void loadAccounts(){
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split(",");
                String name = info[0];
                String email = info[1];
                String password = info[2];

                accounts.put(email, new Account(name, email, password));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getNewUserStatus(){
        return newUser;
    }

    public String getUsername(){
        return username;
    }
}
