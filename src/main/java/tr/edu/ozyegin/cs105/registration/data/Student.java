package tr.edu.ozyegin.cs105.registration.data;

public class Student extends Person {

    private final String studentNumber;

    public Student(String firstName, String lastName, String studentNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    @Override
    public String idCardInfo() {
        return "Student/" + firstName + "/" + lastName + "/" + studentNumber;
    }

    @Override
    public String toString() {
        return "[Student " + studentNumber + "] " + firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student other) {
            return this.studentNumber.equals(other.studentNumber);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.studentNumber.hashCode();
    }
}
