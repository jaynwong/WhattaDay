import java.io.*;
import java.util.*;

public class ActivityPage extends WhattaDay implements Executable{

    private final HashMap<String, Event> activities = new HashMap<>();

    private final Scanner scanner = new Scanner(System.in);

    private final String username;
    private final String yesterday;
    private final String today;
    private final String tmr;
    private int hour;
    private int min;
    private double timeDouble;
    private boolean replacement=false;
    private boolean setEventTime = false;

    public ActivityPage(String username, String yesterday, String today, String tmr){
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
                System.out.println("PROGRAM: You don't have any activities today. Do you want to add an event? " +
                        "(yes) (no)");
                System.out.print("USER: ");
                String userAddEvent = scanner.nextLine();

                if(userAddEvent.equals("<")){
                    this.addEvent(today);
                    validity=true;
                } else if(userAddEvent.equals(">")){
                    validity = true;
                }
            }
        }

        while(true) {
            if(getCurrentDate().equals(tmr)){
                this.printLine();
                this.printDate(getCurrentDate(), false);
                this.printLine();
                this.printIndex();
                this.printEvents(getCurrentDate());
                this.printLine();
                System.out.println("               (add event) (delete event) (plan today) (>)\n");
                System.out.print("USER: ");

                String userChoice = scanner.nextLine();

                switch (userChoice.toLowerCase(Locale.ROOT)) {
                    case "add event" -> this.addEvent(getCurrentDate());
                    case "delete event" -> this.deleteEvent();
                    case "plan today" -> setCurrentDate(today);
                    case ">" -> {
                        return 1;
                    }
                }
            } else {
                this.printLine();
                this.printDate(getCurrentDate(), true);
                this.printLine();
                this.printIndex();
                this.printEvents(getCurrentDate());
                this.printLine();
                System.out.println("            (add event) (delete event) (plan tomorrow) (>)\n");
                System.out.print("USER: ");

                String choice = scanner.nextLine();

                switch (choice.toLowerCase(Locale.ROOT)) {
                    case "add event" -> this.addEvent(getCurrentDate());
                    case "delete event" -> this.deleteEvent();
                    case "plan tomorrow" -> setCurrentDate(tmr);
                    case ">" -> {
                        return 1;
                    }
                }
            }
        }
    }

    public int countEvents(String date){
        int count=0;
        for(String key: activities.keySet()){
            Event event = activities.get(key);

            if(event.getDate().equals(date)){
                count+=1;
            }
        }
        return count;
    }

    public String settingEventTime(String date){
        String time = "";
        String hourInput="";
        boolean validity= false;
        boolean innerLoop = false;
        boolean timeSlotProblem = false;

        System.out.println("PROGRAM: Adding new event ...");

        while(!validity){
            System.out.println("    PROGRAM: please enter the time of the event");
            while(!innerLoop) {
                System.out.print("        HOUR: ");
                hourInput = scanner.nextLine();
                System.out.print("        MINUTE: ");
                String inputMin = scanner.nextLine();

                if (this.isNumber(hourInput) && this.isNumber(inputMin)) {

                    int hourNum = Integer.parseInt(hourInput);
                    int minNum = Integer.parseInt(inputMin);

                    if (hourNum>=0 && hourNum<24 && minNum<60 && minNum>=0) {
                        innerLoop=true;
                        this.hour = hourNum;
                        this.min = minNum;
                    } else {
                        System.out.println("    PROGRAM: Please enter a valid time");
                    }
                }
            }

            double decimalMin = (double) this.min / 60.0;
            double minDoubleFormat = Double.parseDouble(String.format("%,.2f", decimalMin));
            this.timeDouble = this.hour+minDoubleFormat;
            double rounded = Math.round((this.timeDouble-Math.floor(this.timeDouble))*100.0)/100.0;
            int whole = (int) Math.ceil(rounded*100);

            if(whole == 0){
                time = hourInput+".00";
            } else {
                time = hourInput + "." + whole;
            }

            innerLoop=false;
            while(!innerLoop){
                System.out.println("    PROGRAM: Adding new event time at:  "+hourInput+":"+
                        String.format("%02d",this.min));
                System.out.println("    PROGRAM: (re-enter) (continue)");
                System.out.print("    USER: ");
                String userChoose = scanner.nextLine();

                if(userChoose.equals("<")){
                    innerLoop=true;
                } else if (userChoose.equals(">")){
                    innerLoop=true;
                    validity=true;
                } else {
                    System.out.println("    PROGRAM: Please enter either (<) or (>)");
                }
            }

            innerLoop=false;
            while(!innerLoop){
                if(!checkTimeAvailability(this.timeDouble, date)){
                    System.out.println("    PROGRAM: Time slot unavailable");
                    System.out.println("    PROGRAM: (cancel) (continue)");

                    System.out.print("    USER: ");
                    String userDecision = scanner.nextLine();

                    if(userDecision.equals("<")){
                        System.out.println("PROGRAM: Adding event cancelled successfully ...");
                        innerLoop = true;
                    } else if(userDecision.equals(">")){
                        timeSlotProblem = true;
                        innerLoop = true;
                    } else {
                        System.out.println("    PROGRAM: Please enter either (<) or (>)");
                    }
                } else {
                    System.out.println("    PROGRAM: Time slot available");
                    innerLoop=true;
                    validity = true;
                    this.setEventTime=true;
                }
            }

            innerLoop = false;
            while(!innerLoop && timeSlotProblem){
                System.out.println("    PROGRAM: Would you like to replace existing event at time slot " + hourInput+":"+
                        String.format("%02d",this.min) + ", or re-enter the time of the new event?");
                System.out.println("    PROGRAM: (re-enter) (replace)");
                System.out.print("    USER: ");
                String userIn = scanner.nextLine();

                if(userIn.equals("<")){
                    innerLoop = true;
                    timeSlotProblem = false;
                } else if(userIn.equals(">")){
                    if(this.replaceEvent(date, time)) {
                        this.replacement = true;
                    }
                    innerLoop=true;
                    validity=true;
                }
            }
        }
        return time;
    }

    public boolean replaceEvent(String date, String time){

        System.out.println("PROGRAM: Replacing existing time ...");
        while(true){
            System.out.println("    PROGRAM: Event being replaced is shown below.");
            double doubleNumber = activities.get(date+":"+time).getTime();
            int hour = (int) doubleNumber;
            long minutes = Math.round((doubleNumber - hour)*60);
            this.printEvent(date+":"+time, hour, minutes, activities.get(date+":"+time).getDescription());

            System.out.println("    PROGRAM: (cancel) (continue)");
            System.out.print("    USER: ");
            String userChoice = scanner.nextLine();

            if(userChoice.equals("<")){
                System.out.println("    PROGRAM: Replacing event was cancelled successfully!");
                return false;
            } else if (userChoice.equals(">")){
                System.out.println("    PROGRAM: Removing the existing event ...");
                activities.remove(date+":"+time);
                return true;
            } else {
                System.out.println("    PROGRAM: Please enter either (<) or (>)");
            }
        }
    }

    public String setEventDesc(){
        String desc ="";
        boolean validity = false;
        boolean innerValid = false;

        while(!validity){
            System.out.println("    PROGRAM: Please enter the event's description");
            System.out.print("        EVENT DESCRIPTION: ");

            desc = scanner.nextLine();

            System.out.println("    PROGRAM: You have entered the following as the event's description");
            System.out.println("        EVENT DESCRIPTION ENTERED: " +desc);

            while(!innerValid) {
                System.out.println("    PROGRAM: (re-enter) (continue)");
                System.out.print("    USER: ");
                String userInput = scanner.nextLine();

                if (userInput.toLowerCase(Locale.ROOT).equals("<")) {
                    innerValid=true;
                } else if (userInput.toLowerCase(Locale.ROOT).equals(">")) {
                    innerValid=true;
                    validity=true;
                }
            }
        }

        return desc;
    }

    public void addEvent(String date){
        String eventTime = this.settingEventTime(date);

        if(this.replacement || this.setEventTime) {
            String eventDescription = this.setEventDesc();

            activities.put(date + ":" + eventTime, new Event(date + ":" + eventTime, date, this.timeDouble,
                    eventDescription.split(" ")));

            this.writeToFile();
            this.replacement=false;
            this.setEventTime=false;
        }
    }

    public boolean checkTimeAvailability(double time, String date){
        for(String id:activities.keySet()){
            Event event = activities.get(id);
            if(event.getDate().equals(date) && event.getTime() == time){
                return false;
            }
        }
        return true;
    }

    public void deleteEvent(){
        boolean validity = false;
        boolean innerLoop = false;

        System.out.println("PROGRAM: Deleting an event ...");
        while(!validity){
            System.out.println("    PROGRAM: Which event are you planning to delete? ");
            System.out.print("        EVENT ID: ");
            String eventId = scanner.nextLine();

            if (activities.containsKey(eventId)) {
                while (!innerLoop) {
                    System.out.println("        ENTERED EVENT ID: "+eventId);
                    System.out.println("    PROGRAM: Are you sure you want to delete this event?");
                    System.out.println("    PROGRAM: (cancel) (delete)");
                    System.out.print("    USER: ");
                    String userChoice = scanner.nextLine();

                    if(userChoice.equals("<")) {
                        innerLoop=true;
                        validity = true;
                    } else if (userChoice.equals(">")){
                        activities.remove(eventId);
                        System.out.println("PROGRAM: Deleting an event cancelled successfully ...");
                        innerLoop = true;
                        validity = true;
                    } else {
                        System.out.println("    PROGRAM: *error*");
                    }
                }
            } else {
                System.out.println("    PROGRAM: Please enter a valid event ID");
            }
        }

        this.writeToFile();
    }

    public void printEvents(String date){
        ArrayList<Event> eventArrayList = new ArrayList<>();

        for(String eventID: activities.keySet()){
            if(date.equals(activities.get(eventID).getDate())) {
                Event event = activities.get(eventID);
                eventArrayList.add(event);
            }
        }

        eventArrayList.sort(new timeComparator());

        for(Event event:eventArrayList) {
            double doubleNumber = event.getTime();
            int hour = (int) doubleNumber;
            long minutes = Math.round((doubleNumber - hour)*60);

            this.printEvent(event.getId(), hour, minutes, event.getDescription());

        }
    }

    public void printEvent(String id, int hour, long minutes, String[] desc){
        int charCount = 0;
        StringBuilder line = new StringBuilder();

        if(minutes==0){
            System.out.print("| "+id+" | "+hour+":00"+" | ");
        } else {
            System.out.print("| " + id + " | " + hour + ":" + minutes + " | ");
        }

        for (String s : desc) {
            charCount += s.length()+1;
            if (charCount > DESCRIPTION_LENGTH) {
                for(int i=0; i<DESCRIPTION_LENGTH-line.length(); i++){
                    System.out.print(" ");
                }
                System.out.println("|");
                System.out.print("|                  |       | ");
                line = new StringBuilder();
                charCount = 0;
            }
            line.append(s).append(" ");
            System.out.print(s+" ");
        }

        for(int i=0; i<DESCRIPTION_LENGTH-line.length(); i++){
            System.out.print(" ");
        }
        System.out.println("|");
    }

    public void readFile() {
        String path = "/Users/"+MAC_USER_ID+"/Desktop/User Database/"+username+"/Activity.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split("#");
                String id = info[0];
                String readDate = info[1];
                double time = Double.parseDouble(info[2]);
                String[] desc = info[3].split(" ");

                activities.put(id, new Event(id, readDate, time, desc));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile() {
        String path = "/Users/" + MAC_USER_ID + "/Desktop/User Database/" + username + "/Activity.txt";

        try (PrintWriter pw = new PrintWriter((new FileWriter(path, false)))) {
            for (String id : activities.keySet()) {
                Event event = activities.get(id);

                if(!event.getDate().equals(yesterday)){
                    String timeToSave = String.format("%,.2f", event.getTime());
                    pw.println(id+"#"+event.getDate()+"#"+timeToSave+"#"+event.getDescriptionText());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printIndex(){
        System.out.println("|     EVENT ID     | TIME  |               DESCRIPTION               |");
        this.printLine();
    }
}
