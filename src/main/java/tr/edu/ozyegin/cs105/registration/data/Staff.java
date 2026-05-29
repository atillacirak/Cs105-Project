package tr.edu.ozyegin.cs105.registration.data;

public class Staff extends Employee {

    public Staff(String firstName, String lastName, String employeeNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeNumber = employeeNumber;
    }

    @Override
    public String idCardInfo() {
        return "Staff/" + firstName + "/" + lastName + "/" + employeeNumber;
    }

    @Override
    public String toString() {
        return "[Staff] " + super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Staff other) {
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
