package tr.edu.ozyegin.cs105.registration.ui;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import tr.edu.ozyegin.cs105.registration.business.RegistrationService;
import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Student;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/**
 * Shows a single student's weekly course schedule as a timetable grid:
 * days run across the columns (Mon-Fri) and time runs down the rows in
 * 30-minute slots. Each course is drawn as a coloured block that spans the
 * slots it occupies.
 */
public class ScheduleViewController {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    /** Days shown as columns, in order. */
    private static final DayOfWeek[] DAYS = {
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    };

    private static final int SLOT_MINUTES = 30;
    private static final double SLOT_HEIGHT = 28;
    private static final double TIME_COL_WIDTH = 70;

    /** Fallback grid bounds used when the student has no scheduled courses. */
    private static final LocalTime DEFAULT_START = LocalTime.of(9, 0);
    private static final LocalTime DEFAULT_END = LocalTime.of(17, 0);

    /** A small palette so adjacent course blocks are easy to tell apart. */
    private static final String[] BLOCK_COLORS = {
            "#4F86C6", "#5FA052", "#C65F5F", "#B07CC6", "#C69A4F", "#4FB0A8"
    };

    @FXML private ComboBox<Student> studentComboBox;
    @FXML private Label headerLabel;
    @FXML private GridPane scheduleGrid;

    private RegistrationService service;

    @FXML
    public void initialize() {
        studentComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldS, newS) -> showSchedule(newS));
    }

    public void setService(RegistrationService service) {
        this.service = service;
        studentComboBox.setItems(service.allStudents());
        if (!studentComboBox.getItems().isEmpty()) {
            studentComboBox.getSelectionModel().selectFirst();
        }
    }

    /** Selects {@code student} in the picker (which triggers the grid to redraw). */
    public void selectStudent(Student student) {
        studentComboBox.getSelectionModel().select(student);
    }

    private void showSchedule(Student student) {
        scheduleGrid.getChildren().clear();
        scheduleGrid.getColumnConstraints().clear();
        scheduleGrid.getRowConstraints().clear();

        if (student == null) {
            headerLabel.setText("Select a student");
            return;
        }
        headerLabel.setText("Schedule for " + student.getFirstName() + " " + student.getLastName()
                + "  (" + student.getStudentNumber() + ")");

        List<Course> courses = service.coursesFor(student.getStudentNumber());

        LocalTime gridStart = earliestStart(courses);
        LocalTime gridEnd = latestEnd(courses);
        int slotCount = (int) (Duration.between(gridStart, gridEnd).toMinutes() / SLOT_MINUTES);

        buildGridSkeleton(gridStart, slotCount);
        placeCourses(courses, gridStart);
    }

    /** Lays out the day headers, time column, and empty slot grid. */
    private void buildGridSkeleton(LocalTime gridStart, int slotCount) {
        // Column 0 = time labels; columns 1..N = one per day.
        ColumnConstraints timeCol = new ColumnConstraints(TIME_COL_WIDTH);
        timeCol.setHalignment(HPos.CENTER);
        scheduleGrid.getColumnConstraints().add(timeCol);
        for (int i = 0; i < DAYS.length; i++) {
            ColumnConstraints dayCol = new ColumnConstraints();
            dayCol.setHgrow(Priority.ALWAYS);
            dayCol.setHalignment(HPos.CENTER);
            dayCol.setMinWidth(90);
            scheduleGrid.getColumnConstraints().add(dayCol);
        }

        // Row 0 = day headers; rows 1..slotCount = time slots.
        RowConstraints headerRow = new RowConstraints();
        headerRow.setMinHeight(SLOT_HEIGHT);
        scheduleGrid.getRowConstraints().add(headerRow);
        for (int i = 0; i < slotCount; i++) {
            RowConstraints slotRow = new RowConstraints(SLOT_HEIGHT);
            scheduleGrid.getRowConstraints().add(slotRow);
        }

        Label corner = new Label("Time");
        corner.setStyle("-fx-font-weight: bold;");
        scheduleGrid.add(corner, 0, 0);

        for (int d = 0; d < DAYS.length; d++) {
            Label dayHeader = new Label(DAYS[d].getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            dayHeader.setStyle("-fx-font-weight: bold;");
            scheduleGrid.add(dayHeader, d + 1, 0);
        }

        for (int s = 0; s < slotCount; s++) {
            LocalTime slotTime = gridStart.plusMinutes((long) s * SLOT_MINUTES);
            Label timeLabel = new Label(slotTime.format(TIME_FORMAT));
            scheduleGrid.add(timeLabel, 0, s + 1);

            // Thin, slightly transparent rule at the top of every time slot so the
            // hours read like a timetable. Added before the course blocks so the
            // blocks sit on top of the lines.
            addTimeLine(s + 1, VPos.TOP);
        }
        // Close the bottom edge of the last slot.
        addTimeLine(slotCount, VPos.BOTTOM);
    }

    /** Adds a 1px translucent horizontal line spanning all columns of {@code row}. */
    private void addTimeLine(int row, VPos vAlign) {
        Region line = new Region();
        line.setMinHeight(1);
        line.setPrefHeight(1);
        line.setMaxHeight(1);
        line.setMaxWidth(Double.MAX_VALUE);
        line.setStyle("-fx-background-color: rgba(0, 0, 0, 0.12);");
        GridPane.setColumnSpan(line, DAYS.length + 1);
        GridPane.setValignment(line, vAlign);
        GridPane.setFillWidth(line, true);
        scheduleGrid.add(line, 0, row);
    }

    /** Draws each course as a block spanning its day column and time rows. */
    private void placeCourses(List<Course> courses, LocalTime gridStart) {
        for (Course course : courses) {
            int column = dayColumn(course.getDayOfWeek());
            if (column < 0 || course.getStartTime() == null || course.getEndTime() == null) {
                continue; // course without a slot, or on a day we don't display
            }

            int startSlot = slotIndex(gridStart, course.getStartTime());
            int span = Math.max(1, slotIndex(gridStart, course.getEndTime()) - startSlot);

            Label code = new Label(course.getCourseCode());
            code.setStyle("-fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold;");

            // A faint line under the course code, separating it from the time range.
            Region underline = new Region();
            underline.setMinHeight(1);
            underline.setPrefHeight(1);
            underline.setMaxHeight(1);
            underline.setMaxWidth(Double.MAX_VALUE);
            underline.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6);");

            Label time = new Label(course.getStartTime().format(TIME_FORMAT) + "–"
                    + course.getEndTime().format(TIME_FORMAT));
            time.setStyle("-fx-text-fill: white; -fx-font-size: 10;");

            VBox block = new VBox(3, code, underline, time);
            block.setAlignment(Pos.CENTER);
            block.setMaxWidth(Double.MAX_VALUE);
            block.setMaxHeight(Double.MAX_VALUE);
            String color = BLOCK_COLORS[Math.abs(course.getCourseId()) % BLOCK_COLORS.length];
            block.setStyle("-fx-background-color: " + color + ";"
                    + " -fx-padding: 4; -fx-background-radius: 4;"
                    + " -fx-border-color: rgba(0, 0, 0, 0.45);"
                    + " -fx-border-width: 1; -fx-border-radius: 4;");

            scheduleGrid.add(block, column, startSlot + 1, 1, span);
        }
    }

    /** Column for a day, or -1 if that day isn't shown. Column 0 holds the time labels. */
    private static int dayColumn(DayOfWeek day) {
        for (int i = 0; i < DAYS.length; i++) {
            if (DAYS[i] == day) {
                return i + 1;
            }
        }
        return -1;
    }

    /** 0-based slot index of {@code time} relative to {@code gridStart}. */
    private static int slotIndex(LocalTime gridStart, LocalTime time) {
        return (int) (Duration.between(gridStart, time).toMinutes() / SLOT_MINUTES);
    }

    private static LocalTime earliestStart(List<Course> courses) {
        LocalTime earliest = null;
        for (Course c : courses) {
            if (c.getStartTime() != null && (earliest == null || c.getStartTime().isBefore(earliest))) {
                earliest = c.getStartTime();
            }
        }
        return earliest == null ? DEFAULT_START : earliest;
    }

    private static LocalTime latestEnd(List<Course> courses) {
        LocalTime latest = null;
        for (Course c : courses) {
            if (c.getEndTime() != null && (latest == null || c.getEndTime().isAfter(latest))) {
                latest = c.getEndTime();
            }
        }
        if (latest == null) {
            return DEFAULT_END;
        }
        // Guarantee at least one slot of height even for a degenerate range.
        LocalTime start = earliestStart(courses);
        return latest.isAfter(start) ? latest : start.plusMinutes(SLOT_MINUTES);
    }
}
