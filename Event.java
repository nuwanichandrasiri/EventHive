public class Event {
    private String eventId;
    private String name;
    private String description;
    private String date;
    private String type;
    private String location;
    private int capacity;
    private int registeredCount;
    private String status; // Upcoming, Full, Completed

    public Event(String eventId, String name, String description, String date, String type, String location, int capacity) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.date = date;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
        this.registeredCount = 0;
        this.status = "Upcoming";
    }

    // Getters
    public String getEventId() { return eventId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public int getRegisteredCount() { return registeredCount; }
    public int getRemainingSlots() { return capacity - registeredCount; }
    public String getStatus() { return status; }

    public boolean hasSlots() {
        return registeredCount < capacity;
    }

    public void addRegistration() {
        registeredCount++;
        // Auto mark as Full when capacity reached
        if (registeredCount >= capacity) status = "Full";
    }

    public void removeRegistration() {
        if (registeredCount > 0) {
            registeredCount--;
            // Reopen if a slot becomes available
            if (!status.equals("Completed")) status = "Upcoming";
        }
    }

    public void markCompleted() {
        status = "Completed";
    }

    @Override
    public String toString() {
        return "[" + eventId + "] " + name + " | " + date + " | " + type +
               " | " + location + " | Slots: " + registeredCount + "/" + capacity +
               " | Status: " + status;
    }

    public String toDetailedString() {
        return "\n--- Event Details ---" +
               "\nID       : " + eventId +
               "\nName     : " + name +
               "\nDesc     : " + description +
               "\nDate     : " + date +
               "\nType     : " + type +
               "\nLocation : " + location +
               "\nCapacity : " + capacity +
               "\nRemaining: " + getRemainingSlots() +
               "\nStatus   : " + status;
    }
}