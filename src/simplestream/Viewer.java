package simplestream;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

public class Viewer extends JPanel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private int width;
	private int height;

	
    public Viewer(int width, int height) {
		this.width = width;
		this.height = height;
		this.image = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
	}

	private int[] toIntArray(byte[] barr) {
            int[] result = new int[barr.length];
            for(int i=0;i<barr.length;i++)result[i]=barr[i];
            return result;
    }

    
    
    public void ViewerInput(byte[] image_bytes){
    	
    	WritableRaster raster = image.getRaster();
        raster.setPixels(0, 0, width, height, toIntArray(image_bytes));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }

}

