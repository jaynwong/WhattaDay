import java.util.Comparator;

public class timeComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2){
        return Double.compare(o1.getTime(), o2.getTime());
    }
}
