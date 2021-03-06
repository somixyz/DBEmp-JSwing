

package domain;

import java.io.Serializable;

/**
 *
 * @author Milos Dragovic
 */
public class Employee implements Serializable{

    private String id, fullName, workplace, date;
    private byte[] image;

    public Employee(String id, String fullName, String workplace, String dateOfEmployment, byte[] image) {
        this.id = id;
        this.fullName = fullName;
        this.workplace = workplace;
        this.date = dateOfEmployment;
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getDateOfEmployment() {
        return date;
    }

    public void setDateOfEmployment(String dateOfdatEmployment) {
        this.date = dateOfdatEmployment;
    }
    
     
    
    
    
}
