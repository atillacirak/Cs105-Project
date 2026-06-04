package tr.edu.ozyegin.cs105.registration.data;

import java.time.LocalTime;
import java.time.DayOfWeek;

public class Course {
    private final int courseId;
    private final int capacity;
    private final String courseCode;
    private final String title;


    private final LocalTime startTime;



    private final LocalTime endTime;
    private final DayOfWeek dayOfWeek;


    public Course(int courseId, String courseCode, String title, int capacity, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.title = title;
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
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

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
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
