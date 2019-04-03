package epicture.epimgur;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class PostAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Post> posts;

    public PostAdapter(Context c, List<Post> p) {
        context = c;
        posts = p;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return posts.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = posts.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        TextView title = convertView.findViewById(R.id.post_tile);
        ImageView images = convertView.findViewById(R.id.post_images);
        TextView tags = convertView.findViewById(R.id.post_tags);

        title.setText(post.title);
        if (post.is_album == false) {
            Glide.with(context).load(post.link).apply(new RequestOptions().placeholder(new ColorDrawable(Color.BLACK)).error(R.drawable.ic_broken_image).diskCacheStrategy(DiskCacheStrategy.ALL)).into(images);;
        } else if (post.images != null) {
            for (Image image : post.images) {
                Glide.with(context).load(image.link).apply(new RequestOptions().placeholder(new ColorDrawable(Color.BLACK)).error(R.drawable.ic_broken_image).diskCacheStrategy(DiskCacheStrategy.ALL)).into(images);
            }
        }
        String tag = "";
        if (post.tags != null && post.tags.size() > 0) {
            for (Tag t : post.tags) {
                tag += t.name + " ";
            }
        }
        tags.setText(tag);

        return convertView;
    }
}
