import java.util.Scanner;

public class Main {
    private static final String ADMIN_PASSWORD = "admin123";
    private static Scanner scanner = new Scanner(System.in);
    private static EventManager manager = new EventManager();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=============================");
            System.out.println("   🎯 EventHive — Main Menu");
            System.out.println("=============================");
            System.out.println("1. Admin Panel");
            System.out.println("2. Student Panel");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                // Admin password check
                System.out.print("Enter Admin Password: ");
                String password = scanner.nextLine();
                if (!password.equals(ADMIN_PASSWORD)) {
                    System.out.println("❌ Incorrect password.");
                    continue;
                }
                adminPanel();

            } else if (choice.equals("2")) {
                studentPanel();

            } else if (choice.equals("3")) {
                System.out.println("👋 Goodbye!");
                break;
            } else {
                System.out.println("❌ Invalid choice.");
            }
        }
        scanner.close();
    }

    // ── Admin Panel ──────────────────────────────────────────────

    static void adminPanel() {
        while (true) {
            System.out.println("\n=== Admin Panel ===");
            System.out.println("1.  Create Event");
            System.out.println("2.  View All Events");
            System.out.println("3.  View Event Details");
            System.out.println("4.  View Event Attendees");
            System.out.println("5.  Mark Event as Completed");
            System.out.println("6.  Search Event by Name");
            System.out.println("7.  Search Event by Date");
            System.out.println("8.  Search Student by Name");
            System.out.println("9.  Search Student by ID");
            System.out.println("10. Report — Total Registrations");
            System.out.println("11. Report — Full Events");
            System.out.println("12. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createEvent();
                    break;
                case "2":
                    manager.viewAllEvents();
                    break;
                case "3":
                    manager.viewAllEvents();
                    System.out.print("Enter Event ID: ");
                    manager.viewEventDetails(scanner.nextLine());
                    break;
                case "4":
                    manager.viewAllEvents();
                    System.out.print("Enter Event ID: ");
                    manager.viewEventAttendees(scanner.nextLine());
                    break;
                case "5":
                    manager.viewAllEvents();
                    System.out.print("Enter Event ID to mark completed: ");
                    manager.markEventCompleted(scanner.nextLine());
                    break;
                case "6":
                    System.out.print("Enter event name keyword: ");
                    manager.searchEventByName(scanner.nextLine());
                    break;
                case "7":
                    System.out.print("Enter date (DD/MM/YYYY): ");
                    manager.searchEventByDate(scanner.nextLine());
                    break;
                case "8":
                    System.out.print("Enter student name keyword: ");
                    manager.searchStudentByName(scanner.nextLine());
                    break;
                case "9":
                    System.out.print("Enter student ID: ");
                    manager.searchStudentById(scanner.nextLine());
                    break;
                case "10":
                    manager.reportTotalRegistrations();
                    break;
                case "11":
                    manager.reportFullEvents();
                    break;
                case "12":
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }

    // ── Student Panel ────────────────────────────────────────────

    static void studentPanel() {
        while (true) {
            System.out.println("\n=== Student Panel ===");
            System.out.println("1. Register as Student");
            System.out.println("2. View All Events");
            System.out.println("3. View Event Details");
            System.out.println("4. Register for Event");
            System.out.println("5. View My Registrations");
            System.out.println("6. Cancel Registration");
            System.out.println("7. Search Event by Name");
            System.out.println("8. Search Event by Date");
            System.out.println("9. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerStudent();
                    break;
                case "2":
                    manager.viewAllEvents();
                    break;
                case "3":
                    manager.viewAllEvents();
                    System.out.print("Enter Event ID: ");
                    manager.viewEventDetails(scanner.nextLine());
                    break;
                case "4":
                    manager.viewAllEvents();
                    System.out.print("Your Student ID: ");
                    String studentId = scanner.nextLine();
                    System.out.print("Event ID: ");
                    String eventId = scanner.nextLine();
                    manager.registerForEvent(studentId, eventId);
                    break;
                case "5":
                    System.out.print("Your Student ID: ");
                    manager.viewStudentRegistrations(scanner.nextLine());
                    break;
                case "6":
                    System.out.print("Your Student ID: ");
                    manager.viewStudentRegistrations(scanner.nextLine());
                    System.out.print("Registration ID to cancel: ");
                    String regId = scanner.nextLine();
                    System.out.print("Are you sure? (yes/no): ");
                    String confirm = scanner.nextLine();
                    manager.cancelRegistration(regId, confirm.equalsIgnoreCase("yes"));
                    break;
                case "7":
                    System.out.print("Enter event name keyword: ");
                    manager.searchEventByName(scanner.nextLine());
                    break;
                case "8":
                    System.out.print("Enter date (DD/MM/YYYY): ");
                    manager.searchEventByDate(scanner.nextLine());
                    break;
                case "9":
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }

    // ── Helper Input Methods ─────────────────────────────────────

    static void createEvent() {
        System.out.print("Event Name: ");
        String name = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Date (DD/MM/YYYY): ");
        String date = scanner.nextLine();

        System.out.print("Type (Physical/Online): ");
        String type = scanner.nextLine();

        System.out.print("Location/Link: ");
        String location = scanner.nextLine();

        int capacity = 0;
        while (true) {
            System.out.print("Capacity: ");
            try {
                capacity = Integer.parseInt(scanner.nextLine());
                if (capacity > 0) break;
                System.out.println("❌ Capacity must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
        manager.addEvent(name, description, date, type, location, capacity);
    }

    static void registerStudent() {
        // Student ID — numbers only
        String studentId = "";
        while (true) {
            System.out.print("Campus Student ID (numbers only): ");
            studentId = scanner.nextLine();
            if (manager.isValidStudentId(studentId)) break;
            System.out.println("❌ Student ID must contain numbers only.");
        }

        // Name — letters only
        String name = "";
        while (true) {
            System.out.print("Full Name: ");
            name = scanner.nextLine();
            if (manager.isValidName(name)) break;
            System.out.println("❌ Name must contain letters only.");
        }

        // Email validation
        String email = "";
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine();
            if (manager.isValidEmail(email)) break;
            System.out.println("❌ Invalid email. Must contain @ and .");
        }

        System.out.print("Course: ");
        String course = scanner.nextLine();

        manager.addStudent(studentId, name, email, course);
    }
}