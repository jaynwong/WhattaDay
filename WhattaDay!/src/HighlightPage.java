import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class HighlightPage extends WhattaDay implements Executable{
    private HashMap<String, Highlight> tasks = new HashMap<>();
    private String highlight = "";

    private final String username;
    private String yesterday;
    private String today;
    private String tmr;

    public HighlightPage(String username, String yesterday, String today, String tmr){
        this.username = username;
        this.yesterday = yesterday;
        this.today = today;
        this.tmr = tmr;
    }

    @Override
    public int execute() {
        boolean validity = false;

        if(countEvents(today)==0){
            while(!validity) {
                System.out.println("PROGRAM: There is no highlight set for today. Do you want to set a highlight? " +
                        "(yes) (no)");
                System.out.print("USER: ");
                String userAddTask = scanner.nextLine();

                if(userAddTask.equals("<")){
                    this.addTask(today);
                    validity=true;
                } else if(userAddTask.equals(">")){
                    validity = true;
                }
            }
        }

        validity=false;
        while(!validity){

        }

        return 1;
    }

    public void addTask(String date){

    }

    public int countEvents(String date){
        int count=0;
        for(String key: tasks.keySet()){
            Highlight task = tasks.get(key);

            if(task.getDate().equals(date)){
                count+=1;
            }
        }
        return count;
    }

    public void readFile(){
        String path = "/Users/"+MAC_USER_ID+"/Desktop/User Database/"+username+"/Highlight.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split("#");
                String date = info[0];
                String[] desc = info[1].split(" ");
                boolean completionStatus = Boolean.parseBoolean(info[2]);

                tasks.put(date, new Highlight(date, desc,completionStatus));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(){
        String path = "/Users/" + MAC_USER_ID + "/Desktop/User Database/" + username + "/Highlight.txt";

    }
}
