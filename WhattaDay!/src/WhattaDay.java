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
 * Keep in mind, to choose options in this program, you need to either input "r" or "l" if the program asks you
 * to choose either one of the options such as (eat) or (drink). "r" chooses (drink) and "l" chooses (eat).
 * For other user inputs such as entering your username and password, you do need to type in your username and password.
 *
 */
public class WhattaDay {

    private LocalDate today = LocalDate.now();
    private LocalDate tomorrow = today.plusDays(1);

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

    private String todayDate = dateFormat.format(today);
    private String tmrDate = dateFormat.format(tomorrow);
    private Scanner scanner = new Scanner(System.in);

    private HomePage homePage = new HomePage();
    private ActivityPage activityPage = new ActivityPage(todayDate);
    private BrainDumpPage brainDumpPage = new BrainDumpPage();
    private HighlightPage highlightPage = new HighlightPage();

    public void run() throws IOException, URISyntaxException {
        boolean validity=false;
        homePage.execute();
        if(homePage.getNewUserStatus()){
            homePage.createFile(homePage.getUsername());
        }

        while(!validity) {
            activityPage.setUsername(homePage.getUsername());
            activityPage.execute();
        }

    }


    public static void main(String[] args) throws IOException, URISyntaxException {
        WhattaDay application = new WhattaDay();
        application.run();
    }
}
