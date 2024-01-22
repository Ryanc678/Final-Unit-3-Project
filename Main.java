import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Main implements ActionListener {
    private static JLabel instructions;
    private static JLabel dateLabel;
    private static JLabel slash;
    private static JTextField month;
    private static JTextField day;
    private static JTextField year;
    private static JLabel taskLabel;
    private static JTextField taskEnter;
    private static JLabel list;
    private static JButton enterData;
    private static JButton deleteButton;
    static ArrayList<String> listItems = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
            printOut();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("To-do List");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        instructions = new JLabel("<html>To enter a task, enter the date and task below. To delete the task, enter the date and name of the task you want to delete.</html>");
        instructions.setBounds(10, 10, 580, 70);
        instructions.setVerticalAlignment(JLabel.TOP);
        panel.add(instructions);

        dateLabel = new JLabel("Task date: MM/DD/YYYY");
        dateLabel.setBounds(10, 90, 200, 25);
        panel.add(dateLabel);

        slash = new JLabel(" / ");
        slash.setBounds(210, 90, 20, 25);
        panel.add(slash);
        month = new JTextField(2);
        month.setBounds(180, 90, 25, 25);
        panel.add(month);
        day = new JTextField(2);
        day.setBounds(235, 90, 25, 25);
        panel.add(day);
        year = new JTextField(4);
        year.setBounds(270, 90, 50, 25);
        panel.add(year);

        taskLabel = new JLabel("Task to be completed");
        taskLabel.setBounds(10, 130, 200, 25);
        panel.add(taskLabel);

        taskEnter = new JTextField(20);
        taskEnter.setBounds(10, 160, 200, 25);
        panel.add(taskEnter);

        enterData = new JButton("Enter");
        enterData.setBounds(10, 200, 80, 25);
        enterData.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String data = month.getText() + "/" + day.getText() + "/" + year.getText() + ": " + taskEnter.getText().trim();
                        writeToFile(data);
                        print();
                    }
                }
        );
        panel.add(enterData);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(100, 200, 110, 25);
        deleteButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        deleteTask();
                        print();
                    }
                }
        );
        panel.add(deleteButton);

        list = new JLabel("");
        list.setBounds(10, 240, 580, 120);
        list.setVerticalAlignment(JLabel.TOP);
        panel.add(list);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public static void printOut() {
        try {
            File file = new File("data.txt");
            if (file.exists()) {
                Scanner scan = new Scanner(file);
                while (scan.hasNextLine()) {
                    String data = scan.nextLine();
                    listItems.add(data);
                }
                Collections.sort(listItems, (task1, task2) -> {
                    String[] date1 = task1.split("/", 3);
                    String[] date2 = task2.split("/", 3);
                    int result = date1[2].compareTo(date2[2]);
                    if (result == 0) {
                        result = date1[0].compareTo(date2[0]);
                        if (result == 0) {
                            result = date1[1].compareTo(date2[1]);
                        }
                    }
                    return result;
                });
                displayTasks();
                scan.close();
            }
        } catch (FileNotFoundException e) {
            showError("An error has occurred.");
            e.printStackTrace();
        }
    }

    public static void print() {
        listItems.clear();
        printOut();
    }

    public static void displayTasks() {
        StringBuilder tasks = new StringBuilder("<html>");
        for (String task : listItems) {
            tasks.append(task).append("<br>");
        }
        tasks.append("</html>");
        list.setText(tasks.toString());
    }

    public static void writeToFile(String data) {
        try {
            FileWriter writer = new FileWriter("data.txt", true);
            writer.write(data + "\n");
            writer.close();
        } catch (IOException e) {
            showError("An error has occurred.");
            e.printStackTrace();
        }
    }

    public static void deleteTask() {
        String taskToDelete = month.getText() + "/" + day.getText() + "/" + year.getText() + ": " + taskEnter.getText().trim();
        if (listItems.contains(taskToDelete)) {
            listItems.remove(taskToDelete);
            try {
                FileWriter writer = new FileWriter("data.txt");
                for (String task : listItems) {
                    writer.write(task + "\n");
                }
                writer.close();
                System.out.println("The task has been deleted.");
            } catch (IOException e) {
                showError("An error has occurred.");
                e.printStackTrace();
            }
        } else {
            showError("Task not found.");
        }
    }

    public static void showError(String message) {
        System.out.println("The error was: " + message);
    }
}
