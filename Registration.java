public class Registration {
    private String registrationId;
    private Student student;
    private Event event;

    public Registration(String registrationId, Student student, Event event) {
        this.registrationId = registrationId;
        this.student = student;
        this.event = event;
    }

    public String getRegistrationId() { return registrationId; }
    public Student getStudent() { return student; }
    public Event getEvent() { return event; }

    @Override
    public String toString() {
        return "Registration ID: " + registrationId +
               " | Student: " + student.getName() +
               " | Event: " + event.getName();
    }
}