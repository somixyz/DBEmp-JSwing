

package util; 
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File; 
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author Milos Dragovic
 */
public class ImageUtil {

    
    public void chooseImage(JLabel lAddImage, JLabel lClear, JLabel lImage){
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileNameExtensionFilter("JPG fles", "jpg"));
        int window = jfc.showOpenDialog(null);
        if(window == jfc.APPROVE_OPTION){
            lAddImage.setIcon(null);
            lClear.setIcon(null);
            // abs path of our file
            String pathFile = jfc.getSelectedFile().getAbsolutePath();
            
            try{
                Image img = ImageIO.read(new File(pathFile));
                //setting image siize
                img = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                ImageIcon ii = new ImageIcon(img);
                lImage.setIcon(ii);
                
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    
    }
    
    public void deleteImage(JLabel lAddImage, JLabel lClear, JLabel lImage){
        lAddImage.setIcon(null);
        lClear.setIcon(null);
        ImageIcon ii = new ImageIcon(getClass().getResource("/icon/profile.png"));
        lImage.setIcon(ii);
    }
    public byte[] getBytesOfImage(JLabel lImage){
        // convetring image from JLabel to byte[] 
        
        byte[] bImage = null;
        try {
            ImageIcon ii = (ImageIcon)lImage.getIcon();
            Image image = ii.getImage();
            BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(image, 0,0,null);
            g2.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            bImage = baos.toByteArray();
            baos.close();
            String bytes  = new String(bImage, "ISO-8859-1"); // conersion bytes image into String with loosing data
            System.out.println("hashcode profile image"+bytes.hashCode());
            if(bytes.hashCode()==-847326461){
                bImage=null;
            }
        } catch (Exception e) {
        }
        return bImage;
    } 
    
    
    
    
}
