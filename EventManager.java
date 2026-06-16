import java.util.ArrayList;
import java.io.*;

public class EventManager {
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Registration> registrations = new ArrayList<>();
    private int eventCounter = 1;
    private int registrationCounter = 1;

    private static final String EVENTS_FILE = "events.txt";
    private static final String STUDENTS_FILE = "students.txt";
    private static final String REGISTRATIONS_FILE = "registrations.txt";

    public EventManager() {
        loadEvents();
        loadStudents();
        loadRegistrations();
    }

    // ── File Saving ──────────────────────────────────────────────

    private void saveEvents() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(EVENTS_FILE))) {
            for (Event e : events) {
                // Each event saved as one line, fields separated by |
                pw.println(e.getEventId() + "|" + e.getName() + "|" + e.getDescription() + "|" +
                           e.getDate() + "|" + e.getType() + "|" + e.getLocation() + "|" +
                           e.getCapacity() + "|" + e.getRegisteredCount() + "|" + e.getStatus());
            }
        } catch (IOException ex) {
            System.out.println("❌ Error saving events: " + ex.getMessage());
        }
    }

    private void saveStudents() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENTS_FILE))) {
            for (Student s : students) {
                pw.println(s.getStudentId() + "|" + s.getName() + "|" + s.getEmail() + "|" + s.getCourse());
            }
        } catch (IOException ex) {
            System.out.println("❌ Error saving students: " + ex.getMessage());
        }
    }

    private void saveRegistrations() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(REGISTRATIONS_FILE))) {
            for (Registration r : registrations) {
                pw.println(r.getRegistrationId() + "|" +
                           r.getStudent().getStudentId() + "|" +
                           r.getEvent().getEventId());
            }
        } catch (IOException ex) {
            System.out.println("❌ Error saving registrations: " + ex.getMessage());
        }
    }

    // ── File Loading ─────────────────────────────────────────────

    private void loadEvents() {
        File file = new File(EVENTS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length < 9) continue;
                Event e = new Event(p[0], p[1], p[2], p[3], p[4], p[5], Integer.parseInt(p[6]));
                // Restore registered count and status
                int regCount = Integer.parseInt(p[7]);
                for (int i = 0; i < regCount; i++) e.addRegistration();
                if (p[8].equals("Completed")) e.markCompleted();
                events.add(e);
                // Update counter so new IDs don't clash
                int num = Integer.parseInt(p[0].replace("EVT", ""));
                if (num >= eventCounter) eventCounter = num + 1;
            }
        } catch (IOException ex) {
            System.out.println("❌ Error loading events: " + ex.getMessage());
        }
    }

    private void loadStudents() {
        File file = new File(STUDENTS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length < 4) continue;
                students.add(new Student(p[0], p[1], p[2], p[3]));
            }
        } catch (IOException ex) {
            System.out.println("❌ Error loading students: " + ex.getMessage());
        }
    }

    private void loadRegistrations() {
        File file = new File(REGISTRATIONS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length < 3) continue;
                Student student = findStudent(p[1]);
                Event event = findEvent(p[2]);
                if (student != null && event != null) {
                    registrations.add(new Registration(p[0], student, event));
                    // Update counter so new IDs don't clash
                    int num = Integer.parseInt(p[0].replace("REG", ""));
                    if (num >= registrationCounter) registrationCounter = num + 1;
                }
            }
        } catch (IOException ex) {
            System.out.println("❌ Error loading registrations: " + ex.getMessage());
        }
    }

    // ── Validation Methods ───────────────────────────────────────

    public boolean isValidName(String name) {
        return name.matches("[a-zA-Z ]+");
    }

    public boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    public boolean isValidStudentId(String id) {
        return id.matches("[0-9]+");
    }

    public boolean studentExists(String studentId) {
        return findStudent(studentId) != null;
    }

    // ── Admin Methods ────────────────────────────────────────────

    public void addEvent(String name, String description, String date, String type, String location, int capacity) {
        for (Event e : events) {
            if (e.getName().equalsIgnoreCase(name) && e.getDate().equals(date)) {
                System.out.println("❌ An event with the same name and date already exists.");
                return;
            }
        }
        String eventId = "EVT" + eventCounter++;
        Event event = new Event(eventId, name, description, date, type, location, capacity);
        events.add(event);
        saveEvents();
        System.out.println("✅ Event created successfully! ID: " + eventId);
    }

    public void viewAllEvents() {
        if (events.isEmpty()) {
            System.out.println("❌ No events available.");
            return;
        }
        System.out.println("\n=== All Events ===");
        for (Event e : events) {
            System.out.println(e);
        }
    }

    public void viewEventDetails(String eventId) {
        Event event = findEvent(eventId);
        if (event == null) {
            System.out.println("❌ Event not found.");
            return;
        }
        System.out.println(event.toDetailedString());
    }

    public void viewEventAttendees(String eventId) {
        Event event = findEvent(eventId);
        if (event == null) {
            System.out.println("❌ Event not found.");
            return;
        }
        System.out.println("\n=== Attendees for " + event.getName() + " ===");
        boolean found = false;
        for (Registration r : registrations) {
            if (r.getEvent().getEventId().equalsIgnoreCase(eventId)) {
                System.out.println(r.getStudent());
                found = true;
            }
        }
        if (!found) System.out.println("No registrations yet.");
    }

    public void markEventCompleted(String eventId) {
        Event event = findEvent(eventId);
        if (event == null) {
            System.out.println("❌ Event not found.");
            return;
        }
        event.markCompleted();
        saveEvents();
        System.out.println("✅ Event marked as Completed.");
    }

    // ── Reports ──────────────────────────────────────────────────

    public void reportTotalRegistrations() {
        System.out.println("\n=== Registration Report ===");
        System.out.println("Total Registrations: " + registrations.size());
        for (Event e : events) {
            System.out.println(e.getName() + " → " + e.getRegisteredCount() + "/" + e.getCapacity());
        }
    }

    public void reportFullEvents() {
        System.out.println("\n=== Full Events ===");
        boolean found = false;
        for (Event e : events) {
            if (e.getStatus().equals("Full")) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) System.out.println("No events are full.");
    }

    // ── Search Methods ────────────────────────────────────────────

    public void searchEventByName(String keyword) {
        System.out.println("\n=== Search Results ===");
        boolean found = false;
        for (Event e : events) {
            if (e.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) System.out.println("No events found.");
    }

    public void searchEventByDate(String date) {
        System.out.println("\n=== Events on " + date + " ===");
        boolean found = false;
        for (Event e : events) {
            if (e.getDate().equals(date)) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) System.out.println("No events found on this date.");
    }

    public void searchStudentByName(String keyword) {
        System.out.println("\n=== Student Search Results ===");
        boolean found = false;
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("No students found.");
    }

    public void searchStudentById(String studentId) {
        Student s = findStudent(studentId);
        if (s == null) {
            System.out.println("❌ Student not found.");
            return;
        }
        System.out.println(s);
    }

    // ── Student Methods ───────────────────────────────────────────

    public void addStudent(String studentId, String name, String email, String course) {
        if (studentExists(studentId)) {
            System.out.println("❌ Student ID already registered.");
            return;
        }
        Student student = new Student(studentId, name, email, course);
        students.add(student);
        saveStudents();
        System.out.println("✅ Student registered successfully! ID: " + studentId);
    }

    public void registerForEvent(String studentId, String eventId) {
        Student student = findStudent(studentId);
        Event event = findEvent(eventId);

        if (student == null) {
            System.out.println("❌ Student not found.");
            return;
        }
        if (event == null) {
            System.out.println("❌ Event not found.");
            return;
        }
        if (!event.hasSlots()) {
            System.out.println("❌ Event is full.");
            return;
        }
        if (event.getStatus().equals("Completed")) {
            System.out.println("❌ This event is already completed.");
            return;
        }
        for (Registration r : registrations) {
            if (r.getStudent().getStudentId().equals(studentId) &&
                r.getEvent().getEventId().equalsIgnoreCase(eventId)) {
                System.out.println("❌ Already registered for this event.");
                return;
            }
        }

        String regId = "REG" + registrationCounter++;
        Registration registration = new Registration(regId, student, event);
        registrations.add(registration);
        event.addRegistration();
        saveEvents();
        saveRegistrations();
        System.out.println("✅ Registration successful! ID: " + regId);
        System.out.println("   Remaining slots: " + event.getRemainingSlots());
    }

    public void cancelRegistration(String registrationId, boolean confirmed) {
        if (!confirmed) {
            System.out.println("❌ Cancellation aborted.");
            return;
        }
        Registration toRemove = null;
        for (Registration r : registrations) {
            if (r.getRegistrationId().equalsIgnoreCase(registrationId)) {
                toRemove = r;
                break;
            }
        }
        if (toRemove == null) {
            System.out.println("❌ Registration not found.");
            return;
        }
        toRemove.getEvent().removeRegistration();
        registrations.remove(toRemove);
        saveEvents();
        saveRegistrations();
        System.out.println("✅ Registration cancelled successfully.");
    }

    public void viewStudentRegistrations(String studentId) {
        Student student = findStudent(studentId);
        if (student == null) {
            System.out.println("❌ Student not found.");
            return;
        }
        System.out.println("\n=== Registrations for " + student.getName() + " ===");
        boolean found = false;
        for (Registration r : registrations) {
            if (r.getStudent().getStudentId().equals(studentId)) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) System.out.println("No registrations found.");
    }

    // ── Helper Methods ────────────────────────────────────────────

    private Student findStudent(String studentId) {
        for (Student s : students) {
            if (s.getStudentId().equals(studentId)) return s;
        }
        return null;
    }

    private Event findEvent(String eventId) {
        for (Event e : events) {
            if (e.getEventId().equalsIgnoreCase(eventId)) return e;
        }
        return null;
    }
}