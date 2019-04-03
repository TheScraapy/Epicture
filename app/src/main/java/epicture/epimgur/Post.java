package epicture.epimgur;

import java.util.List;

public class Post {

    public String id;
    public String title;
    public Object description;
    public Integer datetime;
    public String type;
    public String link;
    public Integer points;
    public Boolean is_album = false;
    public Boolean favorite = false;
    public Object nsfw = null;
    public List<Tag> tags = null;
    public List<Image> images = null;

}