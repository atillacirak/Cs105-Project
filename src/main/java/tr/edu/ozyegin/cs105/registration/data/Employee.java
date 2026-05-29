package tr.edu.ozyegin.cs105.registration.data;

import tr.edu.ozyegin.cs105.registration.data.Person;

public abstract class Employee extends Person {
    protected String employeeNumber;

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    @Override
    public String toString() {
        return "First Name: " + firstName + " Last Name: " + lastName + " Employee Number: " + employeeNumber;
    }
}
