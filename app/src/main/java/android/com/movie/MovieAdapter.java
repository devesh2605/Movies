package android.com.movie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class MovieAdapter extends BaseAdapter {

    private Context context;
    private List<MovieModels> models;

    public MovieAdapter(Context context, List<MovieModels> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        convertView = view.inflate(context, R.layout.list_view_item, null);

        TextView movieName = (TextView) convertView.findViewById(R.id.textViewMovieName);
        TextView movieYear = (TextView) convertView.findViewById(R.id.textViewYear);

        movieName.setText(models.get(position).getMovie());
        movieYear.setText("Year: " + models.get(position).getYear());

        convertView.setTag(models.get(position).getMovie());

        return convertView;
    }
}
