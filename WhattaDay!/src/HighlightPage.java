import java.io.*;
import java.util.*;

public class HighlightPage extends WhattaDay implements Executable{
    private final HashMap<Integer, Highlight> tasks = new HashMap<>();

    private final String username;
    private final String yesterday;
    private final String today;
    private final String tmr;
    private String highlightText = "";

    public HighlightPage(String username, String yesterday, String today, String tmr){
        this.username = username;
        this.yesterday = yesterday;
        this.today = today;
        this.tmr = tmr;
    }

    @Override
    public int execute() {
        boolean validity = false;
        this.readFile();

        if(countEvents(today)==0){
            while(!validity) {
                System.out.println("PROGRAM: There is no highlight set for today. Do you want to set a highlight? " +
                        "(yes) (no)");
                System.out.print("USER: ");
                String userAddTask = scanner.nextLine();

                if(userAddTask.equals("<")){
                    this.setHighlight();
                    validity=true;
                } else if(userAddTask.equals(">")){
                    validity=true;
                } else {
                    System.out.println("PROGRAM: Please enter either (<) or (>)");
                }
            }
        }

        while(true){
            if(getCurrentDate().equals(tmr)){
                this.readFile();
                this.printLine();
                this.printDate(getCurrentDate(), false);
                this.printLine();
                this.printHighlight();
                this.printLine();
                this.printHeader();
                this.printLine();
                this.printTasks(getCurrentDate());

                this.printLine();
                System.out.println("(<) (add task) (delete task) (set highlight) (check task) (plan today) (>)\n");
                System.out.print("USER: ");

                String userChoice = scanner.nextLine();

                switch (userChoice.toLowerCase(Locale.ROOT)) {
                    case "add task" -> this.addTask(getCurrentDate());
                    case "delete task" -> this.deleteTask();
                    case "set highlight" -> this.setHighlight();
                    case "check task" -> this.completeTask();
                    case "plan today" -> setCurrentDate(today);
                    case ">" -> {
                        return 2;
                    }
                    case "<" -> {
                        return 0;
                    }
                }
            } else {
                this.readFile();
                this.printLine();
                this.printDate(getCurrentDate(), true);
                this.printLine();
                this.printHighlight();
                this.printLine();
                this.printHeader();
                this.printLine();
                this.printTasks(getCurrentDate());

                this.printLine();
                System.out.println("(<) (add task) (delete task) (set highlight) (check task) (plan tomorrow) (>)\n");
                System.out.print("USER: ");

                String choice = scanner.nextLine();

                switch (choice.toLowerCase(Locale.ROOT)) {
                    case "add task" -> this.addTask(getCurrentDate());
                    case "delete task" -> this.deleteTask();
                    case "set highlight" -> this.setHighlight();
                    case "check task" -> this.completeTask();
                    case "plan tomorrow" -> setCurrentDate(tmr);
                    case ">" -> {
                        return 2;
                    }
                    case "<" -> {
                        return 0;
                    }
                }
            }
        }
    }

    public void addTask(String date){
        boolean validity = false;
        boolean innerLoop = false;

        if(!checkHighlight()){
            System.out.println("PROGRAM: *Error* (cannot add task without setting highlight)");
            validity = true;
        } else {
            System.out.println("PROGRAM: Adding new task ...");
        }

        while(!validity){
            System.out.println("    PROGRAM: Please enter the task description");
            System.out.print("        DESCRIPTION: ");
            String taskDesc = scanner.nextLine();

            System.out.println("    PROGRAM: You have entered the following as the task description");
            System.out.println("        ENTERED DESCRIPTION: " + taskDesc);

            while(!innerLoop) {
                System.out.println("    PROGRAM: (re-enter) (add task)");
                System.out.print("    USER: ");
                String userChoice = scanner.nextLine();

                if (userChoice.equals("<")) {
                    innerLoop=true;
                } else if (userChoice.equals(">")) {
                    System.out.println("PROGRAM: Congratulations! Your new task has been added!");
                    this.insertTask(taskDesc, date);
                    innerLoop = true;
                    validity=true;
                } else {
                    System.out.println("    PROGRAM: Please enter either (<) or (>)");
                }
            }
            this.writeToFile();
        }
    }

    public boolean checkHighlight(){
        return highlightText.length() != 0;
    }

    public void insertTask(String desc, String date){
        boolean validity = false;
        int availableID = 1;

        while(!validity){
            if(tasks.containsKey(availableID)){
                availableID++;
            } else {
                validity=true;
            }
        }

        tasks.put(availableID, new Highlight(availableID, date, desc.split(" "), false,
                this.highlightText));
    }

    public void deleteTask(){
        boolean validity = false;
        boolean innerLoop = false;
        System.out.println("PROGRAM: Deleting a task ...");

        while(!validity){
            System.out.println("    PROGRAM: Please enter the ID of the task to be deleted");
            System.out.print("        TASK ID: ");
            String idTask = scanner.nextLine();

            if(this.isNumber(idTask)) {
                Integer validID = Integer.parseInt(idTask);

                if (tasks.containsKey(validID)) {
                    String chosenID;
                    while (!innerLoop) {
                        if(validID>0 && validID<10){
                            chosenID = "00"+validID;
                        } else if(validID>9 && validID<100){
                            chosenID = "0"+validID;
                        } else {
                            chosenID = String.valueOf(validID);
                        }
                        System.out.println("        ENTERED TASK ID: "+chosenID);
                        System.out.println("    PROGRAM: (cancel) (continue)");
                        System.out.print("    USER: ");
                        String userChoice = scanner.nextLine();

                        if (userChoice.equals("<")) {
                            System.out.println("    PROGRAM: Cancelled deletion of task successfully");
                            innerLoop = true;
                            validity = true;
                        } else if (userChoice.equals(">")) {
                            tasks.remove(validID);
                            System.out.println("    PROGRAM: Successfully deleted task " + idTask);
                            innerLoop = true;
                            validity = true;
                            this.writeToFile();
                        } else {
                            System.out.println("    PROGRAM: Please enter either (<) or (>)");
                        }
                    }
                }
            }
        }
    }

    public void completeTask(){
        boolean validity = false;
        boolean innerLoop = false;
        System.out.println("PROGRAM: Editing task completion status ...");

        while(!validity){
            System.out.println("    PROGRAM: Please enter the ID of the task");
            System.out.print("        TASK ID: ");
            String idTask = scanner.nextLine();

            if(this.isNumber(idTask)) {
                int validID = Integer.parseInt(idTask);
                String chosenID = "";

                if (tasks.containsKey(validID)) {
                    while (!innerLoop) {
                        if(validID>0 && validID<10){
                            chosenID = "00"+validID;
                        } else if(validID>9 && validID<100){
                            chosenID = "0"+validID;
                        } else {
                            chosenID = String.valueOf(validID);
                        }
                        System.out.println("        ENTERED TASK ID: "+chosenID);
                        System.out.println("    PROGRAM: (re-enter) (continue)");
                        System.out.print("    USER: ");
                        String userChoice = scanner.nextLine();

                        if (userChoice.equals("<")) {
                            innerLoop = true;
                        } else if (userChoice.equals(">")) {
                            this.editStatus(validID);
                            innerLoop = true;
                            validity = true;
                        } else {
                            System.out.println("    PROGRAM: Please enter either (<) or (>)");
                        }
                    }
                }
            } else {
                System.out.println("    PROGRAM: Please enter a valid task ID");
            }
        }
        this.writeToFile();
    }

    public void editStatus(int taskID){
        boolean validity = false;

        while(!validity){
            System.out.println("    TASK DESCRIPTION: "+tasks.get(taskID).getDescriptionText());
            System.out.println("    PROGRAM: (incomplete) (completed)");
            System.out.print("    USER: ");
            String completion = scanner.nextLine();

            if(completion.equals("<")){
                tasks.get(taskID).setCompletionStat(false);
                System.out.println("    PROGRAM: Do not give up! You are almost there!");
                validity=true;
            } else if(completion.equals(">")){
                tasks.get(taskID).setCompletionStat(true);
                System.out.println("    PROGRAM: Congratulations for completing this task!");
                validity=true;
            } else {
                System.out.println("    PROGRAM: Please enter either (<) or (>)");
            }

        }
    }

    public void printTasks(String date){
        ArrayList<Integer> keyList = new ArrayList<>(tasks.keySet());
        Collections.sort(keyList);

        for(Integer id : keyList){
            Highlight task = tasks.get(id);
            if(task.getDate().equals(date)){
                this.printTask(task.getTaskID(), task.getDescription(), task.isCompletionStat());
            }
        }
    }

    public void printHeader(){
        System.out.println("| INDEX |                     TASK                      |   STATUS   |");
    }

    public void printHighlight(){
        StringBuilder line = new StringBuilder();
        int charCount=0;
        String[] highlightWords = highlightText.split(" ");

        System.out.print("| ");

        for (String highlightWord : highlightWords) {
            charCount += highlightWord.length() + 1;
            if (charCount > 67) {
                for (int j = 0; j < 67 - line.length(); j++) {
                    System.out.print(" ");
                }
                System.out.println("|");
                System.out.print("| ");
                line = new StringBuilder();
                charCount = 0;
            }
            line.append(highlightWord).append(" ");
            System.out.print(highlightWord + " ");
        }

        for(int i=0; i<67-line.length(); i++){
            System.out.print(" ");
        }
        System.out.println("|");
    }

    public void setHighlight(){
        boolean validity = false;
        boolean innerLoop = false;
        System.out.println("PROGRAM: Setting a new highlight ...");

        while(!validity) {
            System.out.println("    PROGRAM: Please enter the new highlight");
            System.out.print("        HIGHLIGHT: ");
            String newHighlight = scanner.nextLine();

            System.out.println("    PROGRAM: You have entered the following as the new highlight for today.");
            System.out.println("        ENTERED HIGHLIGHT: "+newHighlight);

            while (!innerLoop) {
                System.out.println("    PROGRAM: (re-enter) (set as new highlight)");
                System.out.print("    USER: ");
                String userDecision = scanner.nextLine();

                if (userDecision.equals("<")) {
                    innerLoop = true;
                } else if (userDecision.equals(">")) {
                    System.out.println("    PROGRAM: Congratulations! Your new highlight has been set");
                    this.highlightText = newHighlight;
                    validity = true;
                    innerLoop = true;
                    this.writeToFile();
                } else {
                    System.out.println("    PROGRAM: Please enter either (<) or (>)");
                }
            }
        }

    }

    public void readFile(){
        String path = "/Users/"+MAC_USER_ID+"/Desktop/User Database/"+username+"/Highlight.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split("#");
                int id = Integer.parseInt(info[0]);
                String date = info[1];
                String[] desc = info[2].split(" ");
                boolean completionStatus = Boolean.parseBoolean(info[3]);
                String highlight = info[4];

                if(date.equals(getCurrentDate())){
                    highlightText = highlight;
                }

                tasks.put(id, new Highlight(id, date, desc, completionStatus,highlight));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(){
        String path = "/Users/" + MAC_USER_ID + "/Desktop/User Database/" + username + "/Highlight.txt";

        try (PrintWriter pw = new PrintWriter((new FileWriter(path, false)))) {
            for (int id : tasks.keySet()) {
                Highlight task = tasks.get(id);
                if(!task.getDate().equals(yesterday)){
                    pw.println(id+"#"+task.getDate()+"#"+task.getDescriptionText()+"#"+task.isCompletionStat()
                            +"#"+this.highlightText);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int countEvents(String date){
        int count=0;
        for(int id: tasks.keySet()){
            Highlight task = tasks.get(id);

            if(task.getDate().equals(date)){
                count+=1;
            }
        }
        return count;
    }
}
