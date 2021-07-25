public class Highlight {
    private int taskID;
    private String date;
    private String[] description;
    private boolean completionStat;
    private String highlight;

    public Highlight(int taskID, String date, String[] description, boolean completionStat, String highlight){
        this.taskID = taskID;
        this.date = date;
        this.description = description;
        this.completionStat = completionStat;
        this.highlight = highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getHighlight() {
        return highlight;
    }

    public String getDate() {
        return date;
    }

    public String[] getDescription() {
        return description;
    }

    public int getTaskID() {
        return taskID;
    }

    public boolean isCompletionStat() {
        return completionStat;
    }

    public void setCompletionStat(boolean completionStat) {
        this.completionStat = completionStat;
    }

    public String getDescriptionText(){
        StringBuilder text = new StringBuilder();
        for(int i=0; i<description.length;i++){
            if(i+1<description.length) {
                text.append(description[i]).append(" ");
            } else {
                text.append(description[i]);
            }
        }
        return text.toString();
    }
}
