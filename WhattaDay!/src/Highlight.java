public class Highlight {
    private String date;
    private String[] description;
    private boolean completionStat;

    public Highlight(String date, String[] description, boolean completionStat){
        this.date = date;
        this.description = description;
        this.completionStat = completionStat;
    }

    public String getDate() {
        return date;
    }

    public String[] getDescription() {
        return description;
    }
}
