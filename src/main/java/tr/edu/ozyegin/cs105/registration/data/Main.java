package tr.edu.ozyegin.cs105.registration.data;

import tr.edu.ozyegin.cs105.registration.data.seed.MockDatabase;

public class Main {
    public static void main(String[] args) {

        MockDatabase db = new MockDatabase();
        db.seed();

        System.out.println("=== Catalog ===");
        System.out.println("Students:    " + db.students().count());
        System.out.println("Professors:  " + db.employees().findAllProfessors().size());
        System.out.println("Staff:       " + db.employees().findAllStaff().size());
        System.out.println("Courses:     " + db.courses().count());
        System.out.println("Enrollments: " + db.enrollments().count());

        System.out.println();
        System.out.println("=== Sample student and their courses ===");
        Student sample = db.students().findAll().get(0);
        System.out.println(sample);
        for (Course course : db.enrollments().findCoursesOf(sample.getStudentNumber())) {
            System.out.println("  - " + course);
        }

        System.out.println();
        System.out.println("=== CS105 roster ===");
        Course cs105 = db.courses().findAll().stream()
                .filter(c -> c.getCourseCode().equals("CS105"))
                .findFirst()
                .orElseThrow();
        for (Professor instructor : db.teachingAssignments().findInstructorsOf(cs105.getCourseId())) {
            System.out.println("Instructor: " + instructor.getTitle() + " "
                    + instructor.getFirstName() + " " + instructor.getLastName());
        }
        for (Student s : db.enrollments().findStudentsIn(cs105.getCourseId())) {
            System.out.println("  " + s);
        }
    }
}
