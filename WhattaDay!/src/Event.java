public class Event {
    private final String id;
    private final String date;
    private final String time;
    private final String description;
    private final boolean status;

    public Event(String id, String date, String time, String description, boolean status){
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public boolean getStatus(){
        return status;
    }

}
