import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class CreateFile {
    private static final String SPENDING = "Spending.csv";
    private static final String HIGHLIGHT = "Highlight.csv";
    private static final String ACTIVITY = "Activity.csv";
    private static final String BRAIN_DUMP = "BrainDump.csv";

    public void createFile(String username) throws IOException, URISyntaxException {
        //File file = new File(username+"Activity.csv");
        //file.createNewFile();

        String resource = CreateFile.class.getName().replace(".", File.separator) + ".class";
        URL fileURL = ClassLoader.getSystemClassLoader().getResource(resource);

        String path = new File(fileURL.toURI()).getParent();
        String mySubFolder = username;
        File newDir = new File(path + File.separator + mySubFolder);
        boolean success = newDir.mkdir();
        File highlight = new File(path+File.separator+mySubFolder+File.separator+HIGHLIGHT);
        highlight.createNewFile();
        File activity = new File(path+File.separator+mySubFolder+File.separator+ACTIVITY);
        activity.createNewFile();
        File brainDump = new File(path+File.separator+mySubFolder+File.separator+BRAIN_DUMP);
        brainDump.createNewFile();
    }
}
