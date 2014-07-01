import java.util.ArrayList;

/**
 * Created by Purav Shah
 */
public class ArrayRooms {
    private ArrayList<Room> locations;

    public ArrayRooms() {
        locations = new ArrayList<Room>();
    }

    public void addRoom(Room l) throws Exception{
        boolean exists = false;
        for (int i=0; i < locations.size(); i++) {
            if (l.getName().equals(locations.get(i).getName())) {
                exists = true;
            }
        }
        if (!exists || locations.size() == 0)
            locations.add(l);
        if (exists)
            throw new Exception("Location already exists");
        // Else throw AlreadyALocationException
    }

    public void removeRoom(Room l) {
        locations.remove(l);
    }

    public Room geRoom(String city){
       // boolean exists = false;
        Room item = new Room();
        for (int i=0; i<locations.size(); i++) {
            if (city.equals(locations.get(i).getName())) {
                //exists = true;
                item = locations.get(i);
                continue;
            }
        }
        //if (exists)
            return item;
        //else
        //    throw new Exception("Does not exist");
    }

    public int getSize() {
        return locations.size();
    }

    public boolean containsRoom(Room room1) {
        boolean contains = false;
        for (Room l : locations) {
            if (room1.equals(l))
                contains = true;
        }
        return contains;
    }
}
