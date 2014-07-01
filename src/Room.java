import java.util.ArrayList;

/**
 * Created by Purav Shah
 */
public class Room {
    private String name;
    private ArrayList<Employee> employees;
    private int totalSpace, spaceOpen;

    public Room() {
    }

    Room(String name, int totalSpace) {
        this.totalSpace = totalSpace;
        this.spaceOpen = totalSpace;
        this.name = name;
        employees = new ArrayList<Employee>(totalSpace);
    }

    public void addEmployee(Employee e) {
        if (this.spaceOpen != 0) {
            this.employees.add(e);
            this.spaceOpen --;
        } //else return NotEnoughSpaceException
    }

    public void removeEmployee(Employee e) {
        if (this.employees.size() != 0) {
            for (int i=0; i<employees.size(); i++) {
                if (e.equals(employees.get(i))) {
                    employees.remove(employees.get(i));
                    break;
                }
            }
            spaceOpen++;
        }
    }

    public Employee getEmployee(String name, String date) {
        Employee e = new Employee();
        for (Employee employee : this.employees) {
            if (name.equals(employee.getName()) && date.equals(employee.getReservationDate())) {
                e = employee;
                break;
            }
        }
        return e;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(int totalSpace) {
        this.totalSpace = totalSpace;
    }

    public int getSpaceOpen() {
        return spaceOpen;
    }

    public void setSpaceOpen(int spaceOpen) {
        this.spaceOpen = spaceOpen;
    }

    public int getSize() {
        return this.employees.size();
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public boolean containsEmployee(Employee e) {
        boolean contains = false;
        for (Employee x : employees) {
            if (x.equals(e)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (totalSpace != room.totalSpace) return false;
        return !(name != null ? !name.equals(room.name) : room.name != null);

    }

    public String toString() {
        return "Locations{" +
                "name='" + name + '\'' +
                ", totalSpace=" + totalSpace +
                ", spaceOpen=" + spaceOpen +
                '}';
    }
}
