import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

/**
 * The main backbone of the program.
 * Execute run, the green arrow beside the "public static void main(String[] args)" method.
 *
 * Keep in mind, to choose options in this program, you need to either input "<" or ">" if the program asks you
 * to choose either one of the options such as (eat) or (drink). "<" chooses (drink) and ">" chooses (eat).
 * For other user inputs such as entering your username and password, you do need to type in your username and password.
 * As well as choosing options on the screen.
 */
public class WhattaDay {
    protected static final String MAC_USER_ID = "Jason";
    private static final String HIGHLIGHT = "Highlight.txt";
    private static final String ACTIVITY = "Activity.txt";
    private static final String BRAIN_DUMP = "BrainDump.txt";

    private final LocalDate today = LocalDate.now();
    private final LocalDate tomorrow = today.plusDays(1);
    private final LocalDate yesterday = today.minusDays(1);

    private final String todayDate = today.toString();
    private final String tmrDate = tomorrow.toString();
    private final String yesterdayDate = yesterday.toString();

    private String currentDate = todayDate;
    protected Scanner scanner = new Scanner(System.in);

    private final HomePage homePage = new HomePage();

    protected int currentPage; // Activity page is 0, Highlight page is 1 and brain dump page is 2

    public void run() throws IOException, URISyntaxException {
        boolean validity=false;

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

    public void displayPage() throws URISyntaxException {

        if(currentPage == 0){
            ActivityPage activityPage = new ActivityPage(homePage.getUsername(), yesterdayDate, todayDate, tmrDate);
            currentPage = activityPage.execute();
        } else if (currentPage == 1){
            HighlightPage highlightPage = new HighlightPage(homePage.getUsername(), yesterdayDate, todayDate, tmrDate);
            currentPage = highlightPage.execute();
        } else if (currentPage == 2){
            BrainDumpPage brainDumpPage = new BrainDumpPage();
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

    public static void main(String[] args) throws IOException, URISyntaxException {
        WhattaDay application = new WhattaDay();
        application.run();
    }

    public void printLine(){
        for(int i=0; i<70; i++){
            System.out.print("-");
        }
        System.out.println();
    }
}
