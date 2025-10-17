import java.io.*;
import java.util.*;

public class Main {

    static class Train {
        String trainName;
        String departureTime;
        String arrivalTime;
        String route;
        int seatsAvailable;
        String dateOfJourney;
        int nextSeatNumber;

        Train(String trainName, String departureTime, String arrivalTime, String route, int seatsAvailable, String dateOfJourney) {
            this.trainName = trainName;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.route = route;
            this.seatsAvailable = seatsAvailable;
            this.dateOfJourney = dateOfJourney;
            this.nextSeatNumber = 1;
        }
    }

    static final Map<String, Integer> classPrices = new HashMap<>();
    static {
        classPrices.put("Economy", 3000);
        classPrices.put("AC Business", 5000);
    }

    static List<Reservation> reservations = new ArrayList<>();

    static class Reservation {
        String cnicNumber;
        String name;
        int age;
        String trainName;
        String route;
        String travelClass;
        int seatNumber;
        int price;
        String journeyDate;

        Reservation(String cnicNumber, String name, int age, String trainName, String route, String travelClass, int seatNumber, int price, String journeyDate) {
            this.cnicNumber = cnicNumber;
            this.name = name;
            this.age = age;
            this.trainName = trainName;
            this.route = route;
            this.travelClass = travelClass;
            this.seatNumber = seatNumber;
            this.price = price;
            this.journeyDate = journeyDate;
        }

        void displayReservationDetails() {
            System.out.printf("+-------------------------------------------------------------+\n");
            System.out.printf("| CNIC Number: %-15s | Name: %-20s |\n", cnicNumber, name);
            System.out.printf("| Train: %-20s | Route: %-20s |\n", trainName, route);
            System.out.printf("| Class: %-12s | Seat: %-5d | Price: %-7d | Date: %-10s |\n", travelClass, seatNumber, price, journeyDate);
            System.out.printf("+-------------------------------------------------------------+\n");
        }
    }

    //  Merge Sort Implementation
    private static void mergeSortReservationsByPrice(List<Reservation> list) {
        if (list.size() <= 1) return;

        int mid = list.size() / 2;
        List<Reservation> left = new ArrayList<>(list.subList(0, mid));
        List<Reservation> right = new ArrayList<>(list.subList(mid, list.size()));

        mergeSortReservationsByPrice(left);
        mergeSortReservationsByPrice(right);

        merge(list, left, right);
    }

    private static void merge(List<Reservation> list, List<Reservation> left, List<Reservation> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).price <= right.get(j).price) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            list.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            list.set(k++, right.get(j++));
        }
    }

    private static void saveReservationsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("reservations.txt"))) {
            for (Reservation reservation : reservations) {
                writer.printf("%s,%s,%d,%s,%s,%s,%d,%d,%s\n",
                        reservation.cnicNumber, reservation.name, reservation.age,
                        reservation.trainName, reservation.route, reservation.travelClass,
                        reservation.seatNumber, reservation.price, reservation.journeyDate);
            }
        } catch (IOException e) {
            System.out.println("Error saving reservations: " + e.getMessage());
        }
    }

    private static void loadReservationsFromFile() {
        File file = new File("reservations.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 9) continue;

                Reservation reservation = new Reservation(
                        parts[0], parts[1], Integer.parseInt(parts[2]),
                        parts[3], parts[4], parts[5],
                        Integer.parseInt(parts[6]), Integer.parseInt(parts[7]), parts[8]
                );
                reservations.add(reservation);
            }
        } catch (IOException e) {
            System.out.println("Error loading reservations: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadReservationsFromFile();

        Train[] trains = {
                new Train("Green Line Express", "09:30", "16:00", "Karachi to Lahore", 50, "02-05-2025"),
                new Train("Karakoram Express", "12:15", "19:45", "Lahore to Karachi", 60, "05-05-2025"),
                new Train("Tezgam Express", "08:00", "15:30", "Rawalpindi to Karachi", 40, "07-05-2025"),
                new Train("Pakistan Express", "14:45", "22:00", "Multan to Faisalabad", 55, "08-05-2025"),
                new Train("Super Fast Express", "10:00", "18:00", "Islamabad to Lahore", 80, "15-05-2025")
        };

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("*********************************************");
            System.out.println("       Welcome to the Train Booking System      ");
            System.out.println("*********************************************\n");

            System.out.println("Please select an option:");
            System.out.println("1. Book a Ticket\n2. View Reservations\n3. View Your Reservation\n4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("Select the train number to book (1 to " + trains.length + "):");
                for (int i = 0; i < trains.length; i++) {
                    System.out.printf("%d. Train: %-20s | Route: %-30s | Departure: %-8s | Arrival: %-8s | Seats Available: %-3d | Date: %s\n",
                            i + 1, trains[i].trainName, trains[i].route, trains[i].departureTime, trains[i].arrivalTime,
                            trains[i].seatsAvailable, trains[i].dateOfJourney);
                }

                int trainIndex = scanner.nextInt() - 1;
                scanner.nextLine();

                if (trainIndex >= 0 && trainIndex < trains.length) {
                    Train selectedTrain = trains[trainIndex];

                    System.out.printf("You selected: %s | Route: %s | Departure: %s | Arrival: %s | Date: %s\n",
                            selectedTrain.trainName, selectedTrain.route, selectedTrain.departureTime, selectedTrain.arrivalTime, selectedTrain.dateOfJourney);

                    if (selectedTrain.seatsAvailable > 0) {
                        System.out.println("\nAvailable Classes:");
                        System.out.println("1. Economy - Price: " + classPrices.get("Economy"));
                        System.out.println("2. AC Business - Price: " + classPrices.get("AC Business"));

                        System.out.print("\nSelect class (1 or 2): ");
                        int classChoice = scanner.nextInt();
                        scanner.nextLine();

                        String selectedClass = "";
                        int price = 0;

                        if (classChoice == 1) {
                            selectedClass = "Economy";
                            price = classPrices.get("Economy");
                        } else if (classChoice == 2) {
                            selectedClass = "AC Business";
                            price = classPrices.get("AC Business");
                        } else {
                            System.out.println("Invalid class choice! Please select either 1 or 2.");
                            continue;
                        }

                        String journeyDate = selectedTrain.dateOfJourney;

                        System.out.println("Enter your 11-digit CNIC number:");
                        String cnicNumber = scanner.nextLine();
                        while (cnicNumber.length() != 11 || !cnicNumber.matches("\\d{11}")) {
                            System.out.println("Invalid CNIC number! Please enter a valid 11-digit CNIC number:");
                            cnicNumber = scanner.nextLine();
                        }

                        System.out.println("Enter your name:");
                        String name = scanner.nextLine();
                        while (name.trim().isEmpty()) {
                            System.out.println("Name cannot be empty! Please enter a valid name:");
                            name = scanner.nextLine();
                        }

                        System.out.println("Enter your age:");
                        int age = scanner.nextInt();
                        scanner.nextLine();

                        int seatNumber = selectedTrain.nextSeatNumber++;
                        selectedTrain.seatsAvailable--;

                        Reservation reservation = new Reservation(cnicNumber, name, age, selectedTrain.trainName, selectedTrain.route,
                                selectedClass, seatNumber, price, journeyDate);

                        reservations.add(reservation);
                        saveReservationsToFile();

                        System.out.println("\nTicket booked successfully!");
                        reservation.displayReservationDetails();
                    } else {
                        System.out.println("Sorry, no seats available on this train.");
                    }
                } else {
                    System.out.println("Invalid train selection! Please select a valid train number.");
                }

            } else if (choice == 2) {
                System.out.println("\nView all reservations:");
                if (reservations.isEmpty()) {
                    System.out.println("No reservations found.");
                } else {
                    mergeSortReservationsByPrice(reservations);
                    for (Reservation reservation : reservations) {
                        reservation.displayReservationDetails();
                    }
                }

            } else if (choice == 3) {
                System.out.println("\nEnter your 11-digit CNIC number to view your reservation details:");
                String cnicToSearch = scanner.nextLine();

                Optional<Reservation> reservation = reservations.stream()
                        .filter(r -> r.cnicNumber.equals(cnicToSearch))
                        .findFirst();

                if (reservation.isPresent()) {
                    reservation.get().displayReservationDetails();
                } else {
                    System.out.println("No reservation found for the provided CNIC number.");
                }

            } else if (choice == 4) {
                System.out.println("Thank you for using the Train Booking System! Goodbye!");
                break;
            } else {
                System.out.println("Invalid option! Please select a valid option (1, 2, 3, or 4).");
            }
        }

        scanner.close();
    }
}
