/**
 * Created by Purav Shah
 */
public class Employee {
    private int number, row, col;
    private String name, room;
    private String reservationDate, reservationTime;

    public Employee() {
    }

    public Employee(int number, String name, String room, String date) {
        this.number = number;
        this.name = name;
        this.room = room;
        this.reservationDate = date;
    }

    public String getRoom() {
        return room;
    }

    public String getName() {
        return name;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public int getNumber() {
        return number;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReservationTime(String time) {
        reservationTime = time;
    }

    public void setReservationDate(String date) {
        reservationDate = date;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (room != null ? !room.equals(employee.room) : employee.room != null) return false;
        if (name != null ? !name.equals(employee.name) : employee.name != null) return false;
        if (reservationDate != null ? !reservationDate.equals(employee.reservationDate) : employee.reservationDate != null)
            return false;
        if (reservationTime != null ? !reservationTime.equals(employee.reservationTime) : employee.reservationTime != null)
            return false;

        return true;
    }
}
