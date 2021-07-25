import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * The main backbone of the program.
 * Execute run, the green arrow beside the "public static void main(String[] args)" method.
 *
 * Keep in mind, to choose options in this program, you need to either input "<" or ">" if the program asks you
 * to choose either one of the options such as (eat) or (drink). "<" chooses (drink) and ">" chooses (eat).
 * For other user inputs such as entering your username and password, you do need to type in your username and password.
 * As well as choosing options on the screen. In order to quit from the app, just quit by stopping the program once it
 * is in a home screen and not whilst the program is running such as when you are adding a new event or removing an
 * event in order to avoid any errors and bugs.
 *
 * NOTE: PLEASE CHANGE THE PROTECTED STATIC FINAL VARIABLE -> "MAC_USER_ID" BEFORE RUNNING THE PROGRAM TO YOUR MAC USER
 * ID TO ENSURE THE SUCCESSFUL COMPLETE RUN OF THE CODE.
 *
 * SOME NOTES:
 * WELCOME TO WHATTADAY! THIS IS THE PROTOTYPE DESIGN OF HOW THE APPLICATION WOULD RUN.
 * WE ARE PLANNING TO CREATE A REAL MOBILE APPLICATION SIMILAR TO THIS DESIGN IN THE NEAR FUTURE.
 * OUR GOAL IS TO HELP PEOPLE MANAGE THEIR DAY BETTER BY ALLOWING THEM TO PERSONALIZE THEIR
 * DAILY SCHEDULE IN ORDER TO BE MORE PRODUCTIVE AND EFFICIENT IN THEIR DAY TO DAY WORK.
 * PERSONALLY, WE HAVE READ MANY ARTICLES AND DONE RESEARCH WHICH STATED THAT A BRAINDUMP IS AN
 * EFFECTIVE WAY TO KEEP YOUR MIND OFF FROM UNNECESSARY THINGS/THOUGHTS WHILST PERFORMING YOUR DAY
 * TO DAY WORK. FURTHERMORE, PERFORMING WITH A SPECIFIC HIGHLIGHT GOAL FOR THE DAY PROVES TO BE VERY
 * EFFECTIVE IN IMPROVING A PERSON'S DAILY PRODUCTIVITY AND ALLOWS THEM TO GET THINGS DONE AS
 * SCHEDULED. WE HOPE THAT THIS APPLICATION COULD HELP MANAGE YOUR DAY BETTER AND EFFICIENTLY
 * BOOST YOUR PRODUCTIVITY DAILY!"
 *
 * We hope you have enjoyed and find this application useful. Thank you very much.
 *
 * First working prototype created and pushed to git on 2021/07/25.
 */
public class WhattaDay {

    // PLEASE CHANGE THE MAC_USER_ID VARIABLE BELOW TO YOUR MAC USER ID.
    protected static final String MAC_USER_ID = "Jason";


    private static final String HIGHLIGHT = "Highlight.txt";
    private static final String ACTIVITY = "Activity.txt";
    private static final String BRAIN_DUMP = "BrainDump.txt";

    protected static final int DESCRIPTION_LENGTH = 40;

    private final LocalDate today = LocalDate.now();
    private final LocalDate tomorrow = today.plusDays(1);
    private final LocalDate yesterday = today.minusDays(1);

    private final String todayDate = today.toString();
    private final String tmrDate = tomorrow.toString();
    private final String yesterdayDate = yesterday.toString();
    private final Calendar currentTime = Calendar.getInstance();

    private String currentDate = todayDate;
    protected Scanner scanner = new Scanner(System.in);

    private final HomePage homePage = new HomePage();

    protected int currentPage; // Activity page is 0, Highlight page is 1 and brain dump page is 2

    /**
     * To start the application program, just click the green arrow beside this method and click run.
     * NOTE: BEFORE YOU RUN THE PROGRAM, REMEMBER TO CHANGE THE "MAC_USER_ID" STRING TO YOUR MAC USER ID. THIS IS
     * IMPORTANT FOR THE COMPLETE RUN OF YOUR CODE. IF YOU DO NOT DO SO, THE PROGRAM WILL CRASH AS IT IS UNABLE TO FIND
     * THE DIRECTED PATH.
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        WhattaDay application = new WhattaDay();
        application.run();
    }

    public void run() throws IOException, URISyntaxException {
        boolean validity=false;

        System.out.println("THIS CODE IS CREATED BY JAYN AND JASON FOR THE WINTERHACK 2021 HACKATHON EVENT.\n");

        this.createDatabase();

        homePage.execute();
        if(homePage.getNewUserStatus()){
            this.createFile(homePage.getUsername());
        }

        while(!validity) {
            this.displayPage();
        }

    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void printDate(String date, boolean printTime){
        System.out.println("                            " + date);
        if(printTime) {
            System.out.println("                              " + currentTime.get(Calendar.HOUR_OF_DAY) + ":" +
                    currentTime.get(Calendar.MINUTE));
        }
    }

    public void displayPage() throws URISyntaxException {

        if(currentPage == 0){
            ActivityPage activityPage = new ActivityPage(homePage.getUsername(), yesterdayDate, todayDate, tmrDate);
            currentPage = activityPage.execute();
        } else if (currentPage == 1){
            HighlightPage highlightPage = new HighlightPage(homePage.getUsername(), yesterdayDate, todayDate, tmrDate);
            currentPage = highlightPage.execute();
        } else if (currentPage == 2){
            BrainDumpPage brainDumpPage = new BrainDumpPage(homePage.getUsername());
            currentPage = brainDumpPage.execute();
        }
    }

    public void createFile(String username) throws IOException  {

        String path = "/Users/"+MAC_USER_ID+"/Desktop/User Database";

        File newDir = new File(path + File.separator + username);
        boolean success = newDir.mkdir();
        File highlight = new File(path+File.separator+username+File.separator+HIGHLIGHT);
        highlight.createNewFile();
        File activity = new File(path+File.separator+username+File.separator+ACTIVITY);
        activity.createNewFile();
        File brainDump = new File(path+File.separator+username+File.separator+BRAIN_DUMP);
        brainDump.createNewFile();
    }

    public void createDatabase(){
        String path = "/Users/"+MAC_USER_ID+"/Desktop/";

        File newDir = new File(path+File.separator+"User Database");
        boolean success = newDir.mkdir();
    }

    public void printLine(){
        for(int i=0; i<70; i++){
            System.out.print("-");
        }
        System.out.println();
    }

    public boolean isNumber(String number){
        boolean numeric = true;

        try {
            Integer num = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            numeric = false;
        }

        return numeric;
    }

    public void printTask(int id, String[] desc, boolean status){
        StringBuilder line = new StringBuilder();
        int charCount=0;
        boolean firstLine=true;

        String number = String.format("%03d", id);

        System.out.print("|  "+number+"  | ");
        for (String s : desc) {
            charCount += s.length() + 1;
            if (charCount > 46) {
                for (int j = 0; j < 46 - line.length(); j++) {
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
                System.out.print("|       | ");
                line = new StringBuilder();
                charCount = 0;
            }
            line.append(s).append(" ");
            System.out.print(s + " ");
        }

        for(int i=0; i<46-line.length(); i++){
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
}
