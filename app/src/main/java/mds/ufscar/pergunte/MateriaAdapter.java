package mds.ufscar.pergunte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mds.ufscar.pergunte.model.Materia;

/**
 * Created by Danilo on 08/01/2017.
 */

public class MateriaAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ListItem> mDataSource;

    public MateriaAdapter(Context context, ArrayList<ListItem> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        final ListItem item = mDataSource.get(position);
        if (item != null) {
            if (item.isSection()) {
                Section materiaSection = (Section) item;
                rowView = mInflater.inflate(R.layout.list_item_section, parent, false);

                rowView.setOnClickListener(null);
                rowView.setOnLongClickListener(null);
                rowView.setLongClickable(false);

                final TextView sectionView = (TextView) rowView.findViewById(R.id.list_item_section_text);
                sectionView.setText(materiaSection.getTitle());
            } else {
                Materia materia = (Materia) item;
                // Get view for row item
                rowView = mInflater.inflate(R.layout.list_item_materia, parent, false);

                // Get title element - nome da matéria
                final TextView titleTextView =
                        (TextView) rowView.findViewById(R.id.materia_list_title);
                // Get subtitle element - informações adicionais?
                final TextView subtitleTextView =
                        (TextView) rowView.findViewById(R.id.materia_list_subtitle);
                // Get detail element - número de perguntas?
                final TextView detailTextView =
                        (TextView) rowView.findViewById(R.id.materia_list_detail);
                // Get thumbnail element - imagem da matéria?
                final ImageView thumbnailImageView =
                        (ImageView) rowView.findViewById(R.id.materia_list_thumbnail);

                // Populando dados
                titleTextView.setText(materia.getNomeDisciplina());
                subtitleTextView.setText(materia.getDescricao());
                if (materia.getPerguntas() == null) {
                    detailTextView.setText("0");
                } else {
                    detailTextView.setText(String.valueOf(materia.getPerguntas().size()));
                }

                Picasso.with(mContext).load(materia.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);
            }
        }
        return rowView;
    }


}
