package tr.edu.ozyegin.cs105.registration.data;

public class Course {
    private final int courseId;
    private final int capacity;
    private final String courseCode;
    private final String title;


    public Course(int courseId, String courseCode, String title, int capacity) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.title = title;
        this.capacity = capacity;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return courseCode + " - " + title;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Course other) {
            return this.courseId == other.courseId;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.courseId);
    }
}
