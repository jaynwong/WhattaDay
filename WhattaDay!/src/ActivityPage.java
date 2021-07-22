import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class ActivityPage extends CreateFile implements Executable{

    private final HashMap<String, Event> activities = new HashMap<>();
    private String username;
    private String date;

    public ActivityPage(String date){
        this.date = date;
    }

    public void setDate(String date){
        this.date = date;
    }

    @Override
    public void execute() throws URISyntaxException {
        this.readFile();

        System.out.println();
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void readFile() throws URISyntaxException {
        String resource = CreateFile.class.getName().replace(".", File.separator) + ".class";
        URL fileURL = ClassLoader.getSystemClassLoader().getResource(resource);

        String path = new File(fileURL.toURI()).getParent();

        try (BufferedReader reader = new BufferedReader(new FileReader(path+File.separator+username+
                File.separator+"Activity.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split(",");
                String id = info[0];
                String readDate = info[1];
                String time = info[2];
                String desc = info[3];
                boolean status = Boolean.parseBoolean(info[4]);

                if(readDate.equals(date)) {
                    activities.put(time, new Event(id, readDate, time, desc, status));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
