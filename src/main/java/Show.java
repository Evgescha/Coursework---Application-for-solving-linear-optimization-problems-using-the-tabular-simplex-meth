package main.java;

import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Show {
    private final DefaultTableModel model = new DefaultTableModel();

    private JFrame frame;
    private JTextField fieldRow;
    private JTextField fieldCol;
    private JTextField fieldFunction;
    private JTable table;
    private String ans = "";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Show window = new Show();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Show() {
        initialize();
    }


    private void initialize() {
        frame = new JFrame("Решение табличным симплекс методом");
        frame.setBounds(100, 100, 436, 436);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel label = new JLabel("Количество строк");
        label.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label.setBounds(10, 11, 174, 17);
        frame.getContentPane().add(label);

        fieldRow = new JTextField();
        fieldRow.setBounds(171, 11, 86, 20);
        frame.getContentPane().add(fieldRow);
        fieldRow.setColumns(10);

        JButton button = new JButton("Создать таблицу");
        button.addActionListener(e -> {
            try {
                int row = Integer.parseInt(fieldRow.getText());
                createTable(row);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Неверное число строк.");
            }
        });
        button.setBounds(265, 7, 137, 23);
        frame.getContentPane().add(button);

        table = new JTable();
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.setBounds(10, 133, 392, 160);
        frame.getContentPane().add(table);

        JLabel label_1 = new JLabel("Функция");
        label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_1.setBounds(10, 295, 86, 23);
        frame.getContentPane().add(label_1);

        JLabel label_2 = new JLabel("Примем: -2 40 max");
        label_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
        label_2.setBounds(79, 294, 193, 23);
        frame.getContentPane().add(label_2);

        fieldCol = new JTextField();
        fieldCol.setBounds(171, 39, 86, 20);
        frame.getContentPane().add(fieldCol);
        fieldCol.setColumns(10);

        JButton button_1 = new JButton("Решить");
        button_1.addActionListener(e -> solve());
        button_1.setBounds(265, 328, 137, 23);
        frame.getContentPane().add(button_1);

        JLabel label_3 = new JLabel("Матрица");
        label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_3.setBounds(10, 81, 86, 20);
        frame.getContentPane().add(label_3);

        JLabel label_4 = new JLabel("Пример: 10 54 >= 7");
        label_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
        label_4.setBounds(107, 79, 180, 23);
        frame.getContentPane().add(label_4);

        JLabel label_5 = new JLabel("Количество столбцов");
        label_5.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_5.setBounds(10, 42, 174, 17);
        frame.getContentPane().add(label_5);

        fieldFunction = new JTextField();
        fieldFunction.setColumns(10);
        fieldFunction.setBounds(10, 329, 247, 20);
        frame.getContentPane().add(fieldFunction);

        JButton button_2 = new JButton("Открыть файл");
        button_2.addActionListener(e -> openFile());
        button_2.setBounds(265, 41, 137, 23);
        frame.getContentPane().add(button_2);

        JButton button_3 = new JButton("Сохранить");
        button_3.addActionListener(e -> save());
        button_3.setBounds(265, 363, 137, 23);
        frame.getContentPane().add(button_3);
    }

    void createTable(int row) {
        try {
            table.removeAll();
            table.setModel(model);
            model.setRowCount(row);
            model.setColumnCount(1);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setRowHeight(160 / row);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при создании таблицы. \n" +
                            "Проверьте правильность ввода");
        }
    }

    void solve() {
        try {
            //Read function
            String[] functionArray = fieldFunction.getText().split(" ");
            double[] function = new double[functionArray.length - 1];
            for (int i = 0; i < functionArray.length - 1; i++) {
                String s = functionArray[i];
                function[i] = Double.parseDouble(s);
            }
            boolean isFunctionMax = functionArray[functionArray.length - 1].equalsIgnoreCase("max");

            //Read constraintLeftSide
            int row = Integer.parseInt(fieldRow.getText());
            int col = Integer.parseInt(fieldCol.getText());
            double[][] constraintLeftSide = new double[row][col];
            Constraint[] constraintOperator = new Constraint[row];
            double[] constraintRightSide = new double[row];

            for (int i = 0; i < model.getRowCount(); i++) {
                String[] rowData = table.getValueAt(i, 0).toString().split(" ");
                for (int j = 0; j < col; j++) {
                    constraintLeftSide[i][j] = Integer.parseInt(rowData[j]);
                }
                //read operator
                constraintOperator[i] = Constraint.find(rowData[col]);
                //read RightSide
                constraintRightSide[i] = Integer.parseInt(rowData[col + 1]);
            }
            ans = Solv.solve(function, constraintLeftSide, constraintOperator, constraintRightSide, isFunctionMax);
            JOptionPane.showMessageDialog(null, ans);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Некоректные данные. Проверьте введёные вами условия.");
        }

    }

    void openFile() {
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            Scanner sc = null;
            try {
                sc = new Scanner(file);
                //число ограничений, число столбцов
                //функция
                // матрица
                int row = sc.nextInt();
                int col = sc.nextInt();
                sc.nextLine();
                createTable(row);
                fieldRow.setText(row + "");
                fieldCol.setText(col + "");
                fieldFunction.setText(sc.nextLine());
                for (int i = 0; i < row; i++) table.setValueAt(sc.nextLine(), i, 0);

            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Файл не найден или расширениe не поддерживается.");
                e.printStackTrace();
            }
        }
    }

    void save() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileWriter fr;

            try {
                fr = new FileWriter(file);
                fr.write(fieldRow.getText() + " " + fieldCol.getText() + "\n");
                fr.write(fieldFunction.getText() + "\n");
                for (int i = 0; i < model.getRowCount(); i++) {
                    fr.write(table.getValueAt(i, 0).toString() + "\n");
                }
                fr.write("\n\n");
                fr.write(ans);
                fr.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
