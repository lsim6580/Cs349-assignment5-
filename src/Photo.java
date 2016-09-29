import javax.swing.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by luke on 9/25/2016.
 */
public class Photo implements Serializable{
    private ImageIcon source;
    private String description;
    private String date;

    Photo(){

        this.date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

    }


    public ImageIcon getSource() {
        return source;
    }

    public void setSource(ImageIcon source) {
        this.source = source;
    }

    public void setSource(String source){
        this.source = new ImageIcon(source);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
