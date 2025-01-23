package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

    private Connection connection;

    private Scanner sc;

    public Patient(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void addPatient() {
        sc.nextLine();
        System.out.print("Enter Patient's Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Patient's Age: ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Patient's Gender: ");
        String gender = sc.nextLine();

        try {
            String query = "INSERT into patient(name,age,gender) VALUES(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient Added Successfully");
            } else {
                System.out.println("Failed To Add patient");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatient() {
        String query = "SELECT * from patient";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rSet = ps.executeQuery();
            System.out.println("----PATIENT'S DATA-----");
            System.out.println("+----+--------------------+------+--------+");
            System.out.println("| ID |  NAME              | AGE  |GENDER  |");
            System.out.println("+----+--------------------+------+--------+");
            while (rSet.next()) {
                int id = rSet.getInt("id");
                String name = rSet.getString("name");
                int age = rSet.getInt("age");
                String gender = rSet.getString("gender");
                System.out.printf("|%-4s|%-20s|%-6s|%-8s|\n", id, name, age, gender);
                System.out.println("+----+--------------------+------+--------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        String query = "SELECT * FROM patient WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rSet = ps.executeQuery();
            if (rSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int searchPatient() {
        System.out.print("Enter Patient's Id: ");
        int pid = sc.nextInt();
        if (getPatientById(pid)) {
            String query = "SELECT * FROM patient WHERE id = ?";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, pid);
                ResultSet rSet = ps.executeQuery();

                if (rSet.next()) {
                    int id = rSet.getInt("id");
                    String name = rSet.getString("name");
                    int age = rSet.getInt("age");
                    String gender = rSet.getString("gender");

                    System.out.println("---- PATIENT'S DATA -----");
                    System.out.println("+----+--------------------+------+--------+");
                    System.out.println("| ID |  NAME              | AGE  | GENDER |");
                    System.out.println("+----+--------------------+------+--------+");
                    System.out.printf("|%-4d|%-20s|%-6d|%-8s|\n", id, name, age, gender);
                    System.out.println("+----+--------------------+------+--------+");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Patient with ID " + pid + " not found.");
        }

        return pid;//To access it in updatePatient()
    }

    public void updatePatient() {

        int pid = searchPatient();

        if (getPatientById(pid)) {
            System.out.println("Do you want to update this patient's details? (y/n)");
            sc.nextLine();
            String resp = sc.nextLine();

            if (resp.equalsIgnoreCase("Y")) {
                System.out.print("Enter new name:");
                String newName = sc.nextLine();
                System.out.print("Enter new age: ");
                int newAge = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter new gender: ");
                String newGender = sc.nextLine();

                String updateQuery = "UPDATE patient SET name = ?, age = ?, gender = ? WHERE id = ?";
                try {
                    PreparedStatement update = connection.prepareStatement(updateQuery);
                    update.setString(1, newName);
                    update.setInt(2, newAge);
                    update.setString(3, newGender);
                    update.setInt(4, pid);

                    int rowsUpdated = update.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Patient details updated successfully.");
                    } else {
                        System.out.println("Failed to update patient details.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


