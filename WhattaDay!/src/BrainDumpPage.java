import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class BrainDumpPage extends WhattaDay implements Executable{
    private final HashMap<Integer, BrainDump> dump = new HashMap<>();

    private final String username;

    public BrainDumpPage(String username){
        this.username = username;
    }

    @Override
    public int execute() {
        this.readFile();

        while(true) {
            this.printLine();
            this.printDate(getCurrentDate(), true);
            this.printLine();
            this.printHeader();
            this.printLine();
            this.printDump();

            this.printLine();
            System.out.println("        (<) (add dump) (delete dump) (check dump)\n");
            System.out.print("USER: ");

            String choice = scanner.nextLine();

            switch (choice.toLowerCase(Locale.ROOT)) {
                case "add dump" -> this.addDump(getCurrentDate());
                case "delete dump" -> this.deleteDump();
                case "check dump" -> this.completeTask();
                case "<" -> {
                    return 1;
                }
            }
        }
    }

    public void addDump(String date){
        boolean validity=false;
        boolean innerLoop=false;

        System.out.println("PROGRAM: Dumping a task (or a thought?) ...");
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
                    System.out.println("PROGRAM: Congratulations! Dumping has been successful!");
                    this.insertDump(taskDesc, date);
                    innerLoop = true;
                    validity=true;
                } else {
                    System.out.println("    PROGRAM: Please enter either (<) or (>)");
                }
            }
            this.writeToFile();
        }

    }

    public void insertDump(String desc, String date){
        boolean validity = false;
        int availableID = 1;

        while(!validity){
            if(dump.containsKey(availableID)){
                availableID++;
            } else {
                validity=true;
            }
        }

        dump.put(availableID, new BrainDump(availableID, date, desc.split(" "), false));
    }

    public void deleteDump(){
        boolean validity = false;
        boolean innerLoop = false;
        System.out.println("PROGRAM: Removing a dumped task (or thought? hmm) ...");

        while(!validity){
            System.out.println("    PROGRAM: Please enter the INDEX of the task to be deleted");
            System.out.print("        TASK INDEX: ");
            String idTask = scanner.nextLine();

            if(this.isNumber(idTask)) {
                Integer validID = Integer.parseInt(idTask);

                if (dump.containsKey(validID)) {
                    String chosenID;
                    while (!innerLoop) {
                        if(validID>0 && validID<10){
                            chosenID = "000"+validID;
                        } else if(validID>9 && validID<100){
                            chosenID = "00"+validID;
                        } else if(validID>100 && validID<999){
                            chosenID = "0"+validID;
                        } else {
                            chosenID = String.valueOf(validID);
                        }

                        System.out.println("        ENTERED TASK INDEX: "+chosenID);
                        System.out.println("    PROGRAM: (cancel) (continue)");
                        System.out.print("    USER: ");
                        String userChoice = scanner.nextLine();

                        if (userChoice.equals("<")) {
                            System.out.println("    PROGRAM: Cancelled removing process successfully");
                            innerLoop = true;
                            validity = true;
                        } else if (userChoice.equals(">")) {
                            dump.remove(validID);
                            System.out.println("    PROGRAM: Successfully deleted dumped task, feel better? " + idTask);
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
            System.out.println("    PROGRAM: Please enter the index of the dump task");
            System.out.print("        TASK INDEX: ");
            String idTask = scanner.nextLine();

            if(this.isNumber(idTask)) {
                int validID = Integer.parseInt(idTask);
                String chosenID = "";

                if (dump.containsKey(validID)) {
                    while (!innerLoop) {
                        if(validID>0 && validID<10){
                            chosenID = "000"+validID;
                        } else if(validID>9 && validID<100){
                            chosenID = "00"+validID;
                        } else if(validID>100 && validID<999){
                            chosenID = "0"+validID;
                        } else {
                            chosenID = String.valueOf(validID);
                        }
                        System.out.println("        ENTERED DUMP INDEX: "+chosenID);
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
                System.out.println("    PROGRAM: Please enter a valid task index");
            }
        }
        this.writeToFile();
    }

    public void editStatus(int taskID){
        boolean validity = false;

        while(!validity){
            System.out.println("    DUMP DESCRIPTION: "+dump.get(taskID).getDescriptionText());
            System.out.println("    PROGRAM: (incomplete) (completed)");
            System.out.print("    USER: ");
            String completion = scanner.nextLine();

            if(completion.equals("<")){
                dump.get(taskID).setCompletionStat(false);
                System.out.println("    PROGRAM: Hope you finish it soon!");
                validity=true;
            } else if(completion.equals(">")){
                dump.get(taskID).setCompletionStat(true);
                System.out.println("    PROGRAM: Congratulations for completing this task!");
                validity=true;
            } else {
                System.out.println("    PROGRAM: Please enter either (<) or (>)");
            }

        }
    }

    public void printHeader(){
        System.out.println("| INDEX  |    DATE    |              DUMP               |   STATUS   |");
    }

    public void printDump(){
        ArrayList<Integer> keyList = new ArrayList<>(dump.keySet());
        Collections.sort(keyList);

        for(Integer id : keyList){
            BrainDump task = dump.get(id);
            this.printDumpTask(task.getDumpID(), task.getDumpDate(), task.getDumpDesc(), task.isTaskCompletion());
        }
    }

    public void printDumpTask(int id, String date, String[] desc, boolean status){
        StringBuilder line = new StringBuilder();
        int charCount=0;
        boolean firstLine=true;

        String number = String.format("%04d", id);

        System.out.print("|  "+number+"  | "+date+" | ");
        for (String s : desc) {
            charCount += s.length() + 1;
            if (charCount > 31) {
                for (int j = 0; j < 31 - line.length(); j++) {
                    System.out.print(" ");
                }
                if (firstLine) {
                    if (status) {
                        System.out.println("| completed  |");
                    } else {
                        System.out.println("| incomplete |");
                    }
                    firstLine = false;
                } else {
                    System.out.println("|            |");
                }
                System.out.print("|        |            | ");
                line = new StringBuilder();
                charCount = 0;
            }
            line.append(s).append(" ");
            System.out.print(s + " ");
        }

        for(int i=0; i<31-line.length(); i++){
            System.out.print(" ");
        }

        if (firstLine) {
            if (status) {
                System.out.println("| completed  |");
            } else {
                System.out.println("| incomplete |");
            }
        } else {
            System.out.println("|            |");
        }
    }

    public void readFile(){
        String path = "/Users/"+MAC_USER_ID+"/Desktop/User Database/"+username+"/BrainDump.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split("#");
                int id = Integer.parseInt(info[0]);
                String date = info[1];
                String[] desc = info[2].split(" ");
                boolean completionStatus = Boolean.parseBoolean(info[3]);

                dump.put(id, new BrainDump(id, date, desc, completionStatus));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(){
        String path = "/Users/" + MAC_USER_ID + "/Desktop/User Database/" + username + "/BrainDump.txt";

        try (PrintWriter pw = new PrintWriter((new FileWriter(path, false)))) {
            for (int id : dump.keySet()) {
                BrainDump task = dump.get(id);
                pw.println(id+"#"+task.getDumpDate()+"#"+task.getDescriptionText()+"#"+task.isTaskCompletion());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
