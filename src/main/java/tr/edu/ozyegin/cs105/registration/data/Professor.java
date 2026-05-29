package tr.edu.ozyegin.cs105.registration.data;

public class Professor extends Employee {

    private final String title;

    public Professor(String firstName, String lastName, String employeeNumber, String title) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeNumber = employeeNumber;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "[Professor] " + super.toString();
    }

    @Override
    public String idCardInfo() {
        return title + "/" + firstName + "/" + lastName + "/" + employeeNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Professor other) {
            return this.employeeNumber.equals(other.employeeNumber);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.employeeNumber.hashCode();
    }
}
