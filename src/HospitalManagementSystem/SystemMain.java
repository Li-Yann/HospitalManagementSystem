package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


public class SystemMain {

    public static final String url = "jdbc:mysql://localhost:3306/hospitalmanagementsystem";
    public static final String user = "root";
    public static final String password = "AdminUser";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            Patient patient = new Patient(connection, sc);
            Doctor doctor = new Doctor(connection);
            Appointment appointment = new Appointment(connection, sc);

            String again;
            do {
                System.out.println("------HOSPITAL MANAGEMENT SYSTEM--------");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. Search Patient");
                System.out.println("4. Update Patient");
                System.out.println("5. View Doctor");
                System.out.println("6. Book Appointment");
                System.out.println("7. View Appointment");
                System.out.println("8. Cancel Appointment");
                System.out.println("9. Exit");
                System.out.print("Enter The Choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        patient.addPatient();
                        break;
                    case 2:
                        patient.viewPatient();
                        break;
                    case 3:
                        patient.searchPatient();
                        break;
                    case 4:
                        patient.updatePatient();
                        break;
                    case 5:
                        doctor.viewDoctor();
                        break;
                    case 6:
                        appointment.bookAppointment(patient, doctor);
                        break;
                    case 7:
                        appointment.viewAppointment();
                        break;
                    case 8:
                        appointment.cancelAppointment();
                        break;
                    case 9:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Please Enter Valid Choice...");
                }
                System.out.print("Do you want to continue? (Y/N): ");
                again = sc.next();
            } while (again.equalsIgnoreCase("Y"));

            System.out.println("Exiting System.........");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
