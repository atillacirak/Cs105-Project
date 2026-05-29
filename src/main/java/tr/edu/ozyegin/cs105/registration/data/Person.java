package tr.edu.ozyegin.cs105.registration.data;

public abstract class Person {
    protected String firstName;
    protected String lastName;


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public abstract String idCardInfo();

    @Override
    public String toString() {
        return "Person: " + " First Name: " + firstName + " Last Name: " + lastName;
    }
}
