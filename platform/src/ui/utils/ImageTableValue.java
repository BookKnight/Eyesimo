package ui.utils;

/**
 * Created by Anton_Erde on 21.03.2017.
 */
public class ImageTableValue {

    private String imageName;
    private String imageClass;
    private String imagePath;

    public ImageTableValue( String _name, String _class, String _path) {

        imageName = _name;
        imageClass = _class;
        imagePath = _path;

    }

    public String getImageName() {
        return imageName;
    }

    public String getImageClass() {
        return imageClass;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }
}
