import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 9/25/2016.
 */
public class PhotoContainer implements Serializable{
    private static final long serialVersionUID = 123L;
    List<Photo> photoList;
    static int current = 0;
    PhotoContainer(){
        photoList = new ArrayList<Photo>();
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public void add(Photo photo){
        photoList.add(photo);
    }
    public void delete(int x){
        photoList.remove(x);
        if(photoList.size() == current){
            current--;
        }

    }

    public int length(){
        return photoList.size();
    }

    public Photo getPhoto(int x){
        return photoList.get(x);
    }

    public Photo getCurrent(){
        return photoList.get(current);
    }

    public void next(){
        if(hasNext()) {
            current++;
        }
    }
    public int getIndex(){
        return current;
    }
    public void prev(){
        if(hasPrev()) {
            current--;
        }
    }

    public boolean hasNext(){
        if(current < length()-1){
            return true;
        }
        else return false;
    }

    public boolean hasPrev(){
        if(current > 0){
            return true;
        }
        else return false;
    }
}
