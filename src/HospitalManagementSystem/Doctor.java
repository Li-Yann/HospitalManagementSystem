package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Doctor {

    private Connection connection;

    public Doctor(Connection connection) {
        this.connection = connection;
    }


    public void viewDoctor() {
        String query = "SELECT * from doctor";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rSet = ps.executeQuery();
            System.out.println("----DOCTOR'S DATA-----");
            System.out.println("+----+-------------------------+------+----------+");
            System.out.println("| ID |         NAME            | SPECIALIZATION  |");
            System.out.println("+----+-------------------------+-----------------+");
            while (rSet.next()) {
                int id = rSet.getInt("id");
                String name = rSet.getString("name");
                String specialization = rSet.getString("specialization");
                System.out.printf("|%-4s|%-25s|%-17s|\n", id, name, specialization);
                System.out.println("+----+-------------------------+-----------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {
        String query = "SELECT * FROM doctor WHERE id=?";
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
}
