public class Event {
    private final String id;
    private final String date;
    private final double time;
    private final String[] description;

    public Event(String id, String date, double time, String[] description){
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getId(){
        return id;
    }

    public double getTime() {
        return time;
    }

    public String[] getDescription() {
        return description;
    }

    public String getDate(){
        return date;
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
