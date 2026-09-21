package sg.edu.ntu.simple_crm.service;

public class DemoService {
    public int calculateAge(int yearOfBirth, int currentyear) {
        return currentyear - yearOfBirth;
    }

    public String formatFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }
}
