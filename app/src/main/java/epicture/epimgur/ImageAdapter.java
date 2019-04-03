package epicture.epimgur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> imagesLink;

    public ImageAdapter(Context c, List<String> i) {
        context = c;
        imagesLink = i;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return imagesLink.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
        }
        Glide.with(context).load(imagesLink.get(position)).into((ImageView) convertView);

        return convertView;
    }
}
