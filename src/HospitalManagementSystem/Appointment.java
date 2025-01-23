package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Appointment {
    private Connection connection;

    private Scanner scanner;

    public Appointment(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void bookAppointment(Patient patient, Doctor doctor) {
        System.out.print("Enter Patient Id:");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id:");
        int doctorId = scanner.nextInt();
        System.out.print("Enter Appointment Date(YYYY-MM-DD):");
        scanner.nextLine();
        String appDate = scanner.nextLine();
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appDate)) {
                try {
                    String appQuery = "INSERT INTO appointment(patient_id,doctor_id,appointment_date) VALUES(?,?,?)";
                    PreparedStatement ps = connection.prepareStatement(appQuery);
                    ps.setInt(1, patientId);
                    ps.setInt(2, doctorId);
                    ps.setString(3, appDate);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment Booked");
                    } else {
                        System.out.println("Couldn't Book Appointment");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Sorry Doc is not available Appointment is full");
            }

        } else {
            System.out.println("Either Patient or Doctor Data Doesn't Exist");
        }

    }


    public boolean checkDoctorAvailability(int doctorId, String appDate) {
        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id=? AND appointment_date=?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, doctorId);
            ps.setString(2, appDate);
            ResultSet set = ps.executeQuery();
            if (set.next()) {
                int count = set.getInt(1);
                if (count <= 5) {//5 appointments per day
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void viewAppointment() {
        String query = "SELECT appointment.id, appointment.patient_id, appointment.doctor_id, appointment.appointment_date, " +
                "patient.name AS patient_name, doctor.name AS doctor_name " + "FROM appointment " +
                "JOIN patient ON appointment.patient_id = patient.id " +
                "JOIN doctor ON appointment.doctor_id = doctor.id";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("----APPOINTMENTS-----");
            System.out.println("+-------+-------+-------+-------------------+--------------------+----------------------+");
            System.out.println("|APP_ID |PAT_ID |DOC_ID | APPOINTMENT DATE  | PATIENT NAME       | DOCTOR NAME          |");
            System.out.println("+-------+-------+-------+-------------------+--------------------+----------------------+");
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("id");
                int patientId = resultSet.getInt("patient_id");
                int doctorId = resultSet.getInt("doctor_id");
                String appDate = resultSet.getString("appointment_date");
                String patientName = resultSet.getString("patient_name");
                String doctorName = resultSet.getString("doctor_name");

                System.out.printf("|%-7s|%-7s|%-7s|%-19s|%-20s|%-22s|\n", appointmentId, patientId, doctorId, appDate, patientName, doctorName);
                System.out.println("+-------+-------+-------+-------------------+--------------------+----------------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelAppointment() {
        System.out.print("Enter appointment ID to Cancel:");
        int appointmentId = scanner.nextInt();
        String query = "SELECT appointment.id, appointment.patient_id, appointment.doctor_id, appointment.appointment_date, " +
                "patient.name AS patient_name, doctor.name AS doctor_name " +
                "FROM appointment " +
                "JOIN patient ON appointment.patient_id = patient.id " +
                "JOIN doctor ON appointment.doctor_id = doctor.id " +
                "WHERE appointment.id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, appointmentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int patientId = resultSet.getInt("patient_id");
                int doctorId = resultSet.getInt("doctor_id");
                String appDate = resultSet.getString("appointment_date");
                String patientName = resultSet.getString("patient_name");
                String doctorName = resultSet.getString("doctor_name");

                System.out.println("----APPOINTMENT TO BE CANCELED-----");
                System.out.println("+-------+-------+-------+-------------------+--------------------+----------------------+");
                System.out.println("|APP_ID |PAT_ID |DOC_ID | APPOINTMENT DATE  | PATIENT NAME       | DOCTOR NAME          |");
                System.out.println("+-------+-------+-------+-------------------+--------------------+----------------------+");
                System.out.printf("|%-7s|%-7s|%-7s|%-19s|%-20s|%-22s|\n", appointmentId, patientId, doctorId, appDate, patientName, doctorName);
                System.out.println("+-------+-------+-------+-------------------+--------------------+----------------------+");

                System.out.print("Are you sure you want to cancel this appointment? (y/n): ");
                Scanner scanner = new Scanner(System.in);
                String confirm = scanner.nextLine();

                if (confirm.equalsIgnoreCase("Y")) {
                    String deleteQuery = "DELETE FROM appointment WHERE id = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, appointmentId);
                    int rowsAffected = deleteStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Appointment with ID " + appointmentId + " has been successfully canceled.");
                    }
                } else {
                    System.out.println("Appointment cancellation aborted.");
                }
            } else {
                System.out.println("No appointment found with ID " + appointmentId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
