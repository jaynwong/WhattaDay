import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class ActivityPage extends WhattaDay implements Executable{
    private static final int DESCRIPTION_LENGTH = 40;

    private final HashMap<String, Event> activities = new HashMap<>();

    private final Calendar currentTime = Calendar.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    private final String username;
    private final String yesterday;
    private final String today;
    private final String tmr;
    private int hour;
    private int min;
    private double timeDouble;

    public ActivityPage(String username, String yesterday, String today, String tmr){
        this.username = username;
        this.yesterday = yesterday;
        this.today = today;
        this.tmr = tmr;
    }

    @Override
    public int execute() throws URISyntaxException {
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

        validity=false;
        while(!validity) {
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
        return 0;
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

    public String setEventTime(String date){
        String time = "";
        String hourInput="";
        boolean cancelChoice = false;
        boolean checkTime=false;
        boolean validity = false;
        boolean cancellation = false;
        System.out.println("PROGRAM: Adding new event ...");

        while(!checkTime) {
            while (!validity) {
                System.out.println("    PROGRAM: Please enter the time of the event");
                System.out.print("        Hour: ");
                hourInput = scanner.nextLine();
                int intHour = Integer.parseInt(hourInput);

                if (intHour < 24 && intHour >= 0) {
                    validity = true;
                    this.hour = intHour;
                }
            }

            validity = false;
            while (!validity) {
                System.out.print("        Minute: ");
                String minute = scanner.nextLine();
                int intMin = Integer.parseInt(minute);

                if (intMin >= 0 && intMin < 60) {
                    validity = true;
                    this.min = intMin;
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

            if(!checkTimeAvailability(this.timeDouble, date)){
                while(!cancelChoice) {
                    System.out.println("    PROGRAM: Time slot unavailable, would you like to re-enter the time or " +
                            "cancel adding the event? ");
                    System.out.println("PROGRAM: (re-enter) (cancel)");
                    System.out.print("USER: ");
                    String userChoice = scanner.nextLine();

                    if(userChoice.equals("<")){
                        cancelChoice = true;
                    } else if (userChoice.equals(">")){
                        cancelChoice = true;
                        checkTime=true;
                        cancellation=true;
                    }
                }
            } else {
                checkTime=true;
            }
        }

        System.out.println("    Setting event time as: "+this.hour+":"+this.min);

        validity=false;
        while(!validity && !cancellation) {
            System.out.println("    PROGRAM: (re-enter) (continue)");
            System.out.print("    USER: ");
            String userInput = scanner.nextLine();

            if(userInput.equals("<")){
                this.setEventTime(date);
                validity=true;
            } else if(userInput.equals(">")){
                validity=true;
            }
        }

        return time;
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
            System.out.println("    EVENT DESCRIPTION ENTERED: " +desc);

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
        String eventTime = this.setEventTime(date);
        String eventDescription = this.setEventDesc();

        activities.put(date+":"+eventTime,new Event(date+":"+eventTime,date,this.timeDouble,
                eventDescription.split(" ")));

        this.writeToFile();
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

        System.out.println("PROGRAM: Deleting an event ...");
        while(!validity){
            System.out.println("    PROGRAM: Which event are you planning to delete? ");
            System.out.print("        EVENT ID: ");
            String eventId = scanner.nextLine();

            if(activities.containsKey(eventId)){
                activities.remove(eventId);
                validity=true;
            }
        }

        this.writeToFile();
    }

    public void printDate(String date, boolean printTime){
        System.out.println("                            " + date);
        if(printTime) {
            System.out.println("                              " + currentTime.get(Calendar.HOUR_OF_DAY) + ":" +
                    currentTime.get(Calendar.MINUTE));
        }
    }

    public void printEvents(String date){
        ArrayList<Event> eventArrayList = new ArrayList<>();

        for(String eventID: activities.keySet()){
            if(date.equals(activities.get(eventID).getDate())) {
                Event event = activities.get(eventID);
                eventArrayList.add(event);
            }
        }

        Collections.sort(eventArrayList, new timeComparator());

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

    public void readFile() throws URISyntaxException {
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
