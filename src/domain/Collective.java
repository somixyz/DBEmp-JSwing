
package domain;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import util.ImageUtil;

/**
 *
 * @author Milos Dragovic
 */
public class Collective {

    private List<Employee> collective;
    private String nameCollective;

    public Collective(String nameCollective) {
        this.collective = new ArrayList<>();
        this.nameCollective = nameCollective;
    }

    public List<Employee> getCollective() {
        return collective;
    }

    public void setCollective(List<Employee> collective) {
        this.collective = collective;
    }
    private Employee newEmployee (String id, String fullName , String workplace, String dateOfEmp, byte[] img){
    Employee employee = new Employee(id, fullName, workplace, dateOfEmp, img);
    return employee;
    
    }

    private void addEmployee(Employee e){
        this.collective.add(e);
    }
    private Object[][] showEmployees(){
        Object[][] data = new Object[this.collective.size()][4];  //4 fields of Employee class... ID,Full name...
        Iterator<Employee> it = collective.iterator();
        int count = 0;
        while (it.hasNext()) {
            Object[] dataEmployee = new Object[4];
            Employee employee = it.next();
            dataEmployee[0]=employee.getId();
            dataEmployee[1]=employee.getFullName();
            dataEmployee[2]=employee.getWorkplace();
            dataEmployee[3]=employee.getDateOfEmployment(); 
            // add data about Employee into data set
            data[count]=dataEmployee;
            count++;
        }
        return data;
    }
    
    public void showDataInTable(JTable table){
        String[] nameColons = {"ID", "FullName", "Workplace", "DateOfEmployment"};
        table.setModel(new javax.swing.table.DefaultTableModel(showEmployees(), nameColons));
        table.getColumnModel().getColumn(0).setPreferredWidth(110);
        table.getColumnModel().getColumn(1).setPreferredWidth(195);
        table.getColumnModel().getColumn(2).setPreferredWidth(195);
        table.getColumnModel().getColumn(3).setPreferredWidth(140); 
    }
    public void refreshGUI(JTextField id, JTextField fullname, JTextField workplace, JTextField dateOfEmp, JLabel img, JTextField search, JLabel numOfData, JLabel typeSearch){
        id.setText(null);
        fullname.setText(null);
        workplace.setText(null);
        dateOfEmp.setText(null);
        ImageIcon ii = new ImageIcon(getClass().getResource("/icon/profile.png"));
        img.setIcon(ii);
        search.setText(null);  
    }
    private String[] idDateOfEmployment(){
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = new Date();
        String id = df.format(date);
        
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date1 = new Date();
        String dateOfEmployment = df.format(date1);
        
        String[] idDateOfEmplyment = {id,dateOfEmployment};
        return idDateOfEmplyment;
    }
    
    public void enterData(JTextField id, JTextField fullname, JTextField workplace, JTextField dateOfEmp, JLabel img,
                    JTextField search, JLabel numData, JLabel typeSearch, JTable table, byte[] bImage ){
    
        addEmployee(newEmployee(idDateOfEmployment()[0], fullname.getText().toUpperCase(), workplace.getText().toUpperCase(), idDateOfEmployment()[1], bImage));
        showDataInTable(table);
        refreshGUI(id, fullname, workplace, dateOfEmp, img, search, numData, typeSearch);
        serialize(this.collective);
    }
    
    private void serialize(List<Employee> collective){
        try {
            FileOutputStream fos = new FileOutputStream("Memory.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(collective);
            oos.close();
            fos.close();
        } catch (Exception e) {
        } 
    }
    public void deserialize(){
        try {
            FileInputStream fis = new FileInputStream("Memory.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.collective = (List<Employee>) ois.readObject();
            ois.close();
            fis.close(); 
        } catch (Exception e) {
        }
    }
    
    //load data from memoty.txt into table
    public void load (JTable table){ 
        try {
        deserialize();
        showDataInTable(table);
        } catch (Exception e) {
        } 
    }
    /*
    *   get image, if exist, from memory file
    **/
    private ImageIcon getImageFromFile(String id){
        Iterator<Employee> it = this.collective.iterator();
        ImageIcon ii = null;
        while (it.hasNext()) {
            Employee employee = it.next();
            if(employee.getId().equals(id)){
                if(employee.getImage()==null){
                    break;
                }else{
                    byte[] bytesImage = employee.getImage();
                    ii = new ImageIcon(bytesImage);
                    return ii;
                }
            }
        }
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/icon/profile.png"));
        return defaultIcon;
    } 
    public void showDataEmployee(JTable table, JTextField id, JTextField fullname, JTextField workplace, JTextField dateOfEmp, JLabel img){
        id.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
        fullname.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
        workplace.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
        dateOfEmp.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
        img.setIcon(getImageFromFile(String.valueOf(table.getValueAt(table.getSelectedRow(), 0))));
    }
    
    
    private void editData(String id, String fullname, String workplace, JLabel image, JTable table){
        ImageUtil i = new ImageUtil();
        Iterator<Employee> it = collective.iterator();
        int count =0;
        while (it.hasNext()) {
            Employee employee = it.next();
            if(employee.getId().equals(id)){
                this.collective.get(count).setFullName(fullname);
                this.collective.get(count).setWorkplace(workplace);
                this.collective.get(count).setImage(i.getBytesOfImage(image));
                showDataInTable(table); 
                break;
            }
            count++;
        }
    }
    
    public void edit(JTextField id, JTextField fullname, JTextField workplace, JTextField dateOfEmp,
            JLabel image, JTextField search, JLabel numData, JLabel typeSearch, JTable table){
       
        editData(id.getText(), fullname.getText().toUpperCase(), workplace.getText().toUpperCase(), image, table);
        refreshGUI(id, fullname, workplace, dateOfEmp, image, search, numData, typeSearch);
        serialize(this.collective); 
    }  
    
    private void searchStartWith(String lookingPatern, JTable table){
        List<Employee> temporary = new ArrayList<>();
        for(Employee e : collective){
            if(lookingPatern.length() <= e.getId().length()){
                if(lookingPatern.equalsIgnoreCase(e.getId().substring(0,lookingPatern.length()))){
                    temporary.add(e); 
                    continue;
                }
            }
            if(lookingPatern.length() <= e.getFullName().length()){
                if(lookingPatern.equalsIgnoreCase(e.getFullName().substring(0,lookingPatern.length()))){
                    temporary.add(e); 
                    continue;
                }
            }
            if(lookingPatern.length() <= e.getWorkplace().length()){
                if(lookingPatern.equalsIgnoreCase(e.getWorkplace().substring(0,lookingPatern.length()))){
                    temporary.add(e); 
                    continue;
                }
            }
        }
        Object[][] data = new Object[temporary.size()][4];
        int count = 0;
        for(Employee e: temporary){
            Object dataEmployee[] = new Object[4];
            dataEmployee[0]=e.getId();
            dataEmployee[1]=e.getFullName();
            dataEmployee[2]=e.getWorkplace();
            dataEmployee[3]=e.getDateOfEmployment(); 
            data[count] = dataEmployee;
            count++;
        }
        String[] nameColon = {"ID", "FullName", "Workplace", "Date"};
        table.setModel(new DefaultTableModel(data, nameColon));
        table.getColumnModel().getColumn(0).setPreferredWidth(110);
        table.getColumnModel().getColumn(1).setPreferredWidth(195);
        table.getColumnModel().getColumn(2).setPreferredWidth(195);
        table.getColumnModel().getColumn(3).setPreferredWidth(140);
    }
    
   private void searchContains(String lookingPatern, JTable table){
   
       lookingPatern = lookingPatern.toUpperCase();
       List<Employee> temp = new ArrayList<>();
       for(Employee e: collective){
           if(e.getId().contains(lookingPatern)||e.getFullName().contains(lookingPatern)||e.getWorkplace().contains(lookingPatern)){
               temp.add(e);
           }
       }
       Object[][] data = new Object[temp.size()][4];
       int count = 0;
       for(Employee e : temp){
           Object[] dataEmployee = new Object[4];
           dataEmployee[0]=e.getId();
           dataEmployee[1]=e.getFullName();
           dataEmployee[2]=e.getWorkplace();
           dataEmployee[3]=e.getDateOfEmployment(); 
           data[count] = dataEmployee;
           count++;
       }
       String[] nameColon = { "ID", "FullName", "Workplace", "Date"};
       table.setModel(new DefaultTableModel(data, nameColon));
       table.getColumnModel().getColumn(0).setPreferredWidth(110);
       table.getColumnModel().getColumn(1).setPreferredWidth(195);
       table.getColumnModel().getColumn(2).setPreferredWidth(195);
       table.getColumnModel().getColumn(3).setPreferredWidth(140); 
   }
   
   public void search (String lookingPatern, JComboBox param, JTable table, JLabel timeSearch, JLabel numData){
       if(param.getSelectedItem().toString().equals("Start with")){
           long startTime = System.nanoTime();
           searchStartWith(lookingPatern, table);
           long endTime = System.nanoTime();
           long timeiInMiliSec = (endTime-startTime)/1000000;
           timeSearch.setText("Time of searching : "+timeiInMiliSec);
           numData.setText("Number of data "+ String.valueOf(table.getRowCount()));
       }
       else if(param.getSelectedItem().toString().equals("Contains")){
           long startTime = System.nanoTime();
           searchContains(lookingPatern, table);
           long endTime = System.nanoTime();
           long timeiInMiliSec = (endTime-startTime)/1000000;
           timeSearch.setText("Time of searching : "+timeiInMiliSec);
           numData.setText("Number of data "+ String.valueOf(table.getRowCount()));
       }
   }
    public void delete(String id, JTable table, JLabel numData){
        Iterator<Employee> it = collective.iterator();
        while (it.hasNext()) {
            Employee e = it.next();
            if(e.getId().equals(id)){
            it.remove();}
        }
        showDataInTable(table);
        numData.setText("Number of data: "+table.getRowCount());
        serialize(collective);
    }
    
    /** Export file from ArrayList into Excel file, excel 2003 with extenstion xls
     */
    public void exportCSV(JFrame frame){
        JFileChooser jfc = new JFileChooser(".");
        int r = jfc.showSaveDialog(frame);
        if(r==JFileChooser.APPROVE_OPTION){
            try {
               File f = jfc.getSelectedFile();
                OutputStream os = new FileOutputStream(f);
                //these 3 bytes--> for encoding export in file like UTF-8 to write serbina latin letters
                os.write(239);
                os.write(187);
                os.write(191);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(os,"UTF-8"));
                for(Employee e : collective){
                    pw.println(e.getId()+","+e.getFullName()+","+e.getWorkplace()+","+e.getDateOfEmployment());
                    pw.flush();
                }
                pw.close();
                os.close();
                JOptionPane.showMessageDialog(null, "You successfully exported your data", "INFO", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Here is some error", "INFO",JOptionPane.ERROR_MESSAGE);
            }
        } 
    }
    public void importCSV(JTable table , JFrame frame){
        JFileChooser jfc = new JFileChooser(".");
        jfc.setFileFilter(new FileNameExtensionFilter("CSV files(.csv)","csv"));
        int r = jfc.showOpenDialog(frame);
        if(r==JFileChooser.APPROVE_OPTION){
            File f = jfc.getSelectedFile();
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line ;
                while ((line=br.readLine())!=null) {                    
                    String[] data = line.split(",");
                    Employee e = new Employee(data[0], data[1], data[2], data[3], null);
                    addEmployee(e); 
                }
                br.close();
                showDataInTable(table);
                serialize(collective);
                JOptionPane.showMessageDialog(null, "You successfully imported your data", "INFO", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
               JOptionPane.showMessageDialog(null, "Here is some error", "INFO",JOptionPane.ERROR_MESSAGE); 
            } 
        }  
    }
    public void exportExcel2003(){
        ImageIcon bigIcon = new ImageIcon(getClass().getResource("/icon/excel03-64.png"));
        ImageIcon smallIcon = new ImageIcon(getClass().getResource("/icon/excel03.png"));
        
        JTextField nameFile = new JTextField(10);
        JTextField nameList = new JTextField(10);
        
        JPanel tfPanel = new JPanel(new BorderLayout());
        JPanel labelPanel = new JPanel(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        tfPanel.add(nameFile, BorderLayout.NORTH);
        tfPanel.add(nameList, BorderLayout.SOUTH);
        
        labelPanel.add(new JLabel("<html>Name file:<br><br>Name list:</html>"),BorderLayout.CENTER);
        
        mainPanel.add(tfPanel,BorderLayout.EAST);
        mainPanel.add(labelPanel, BorderLayout.WEST);
        
        Object[] options = {"Export", "Give up"};
        int dialog = JOptionPane.showOptionDialog(null, mainPanel, "Enter name of file and list", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, bigIcon, options, options[0]);
        if(dialog == JOptionPane.OK_OPTION){
            try {
                Workbook wb = new HSSFWorkbook();
                Sheet list = wb.createSheet();
                for (int i = 0; i < collective.size(); i++) {
                    Row row = list.createRow(i);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(collective.get(i).getId());
                    cell = row.createCell(1);
                    cell.setCellValue(collective.get(i).getFullName());
                    cell = row.createCell(2);
                    cell.setCellValue(collective.get(i).getWorkplace());
                    cell = row.createCell(3);
                    cell.setCellValue(collective.get(i).getDateOfEmployment());
                    if(i==65535) { 
                        break;
                    }
                }
                JFileChooser jfc = new JFileChooser(new File(".")){
                    @Override
                    protected JDialog createDialog(Component parent )throws HeadlessException {
                        JDialog dialog = super.createDialog(parent);
                        dialog.setTitle("Saved");
                        dialog.setIconImage(smallIcon.getImage());
                        return dialog;
                    }};
                    jfc.setSelectedFile(new File(nameFile.getText()+".xls"));
                    int save = jfc.showSaveDialog(null);
                    if(save==JFileChooser.APPROVE_OPTION){
                        File file = jfc.getSelectedFile();
                        FileOutputStream fos = new FileOutputStream(file);
                        wb.write(fos);
                        fos.close();
                        wb.close();
                        JOptionPane.showMessageDialog(null, "You successfully exported your data","INFO",JOptionPane.INFORMATION_MESSAGE,bigIcon);
                        
                    } 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Here is some Error", "INFO",JOptionPane.ERROR_MESSAGE);
            }
        }  
    }
    
    
    
    
    
    
    
}
