public class BrainDump {
    private int dumpID;
    private String[] dumpDesc;
    private boolean taskCompletion;
    private String dumpDate;

    public BrainDump(int dumpID, String dumpDate, String[] dumpDesc, boolean taskCompletion){
        this.dumpID = dumpID;
        this.dumpDate = dumpDate;
        this.dumpDesc = dumpDesc;
        this.taskCompletion = taskCompletion;
    }

    public int getDumpID() {
        return dumpID;
    }

    public String getDumpDate() {
        return dumpDate;
    }

    public boolean isTaskCompletion() {
        return taskCompletion;
    }

    public String[] getDumpDesc() {
        return dumpDesc;
    }

    public void setCompletionStat(boolean taskCompletion){
        this.taskCompletion = taskCompletion;
    }

    public String getDescriptionText(){
        StringBuilder text = new StringBuilder();
        for(int i=0; i<dumpDesc.length;i++){
            if(i+1<dumpDesc.length) {
                text.append(dumpDesc[i]).append(" ");
            } else {
                text.append(dumpDesc[i]);
            }
        }
        return text.toString();
    }

}
