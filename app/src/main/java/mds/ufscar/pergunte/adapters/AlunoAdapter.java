package mds.ufscar.pergunte.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mds.ufscar.pergunte.R;
import mds.ufscar.pergunte.models.Aluno;

/**
 * Created by Danilo on 08/01/2017.
 */

public class AlunoAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Aluno> mDataSource;

    public AlunoAdapter(Context context, ArrayList<Aluno> alunos) {
        mContext = context;
        mDataSource = alunos;
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

        final Aluno aluno = mDataSource.get(position);
        if (aluno != null) {
            // Get view for row item
            rowView = mInflater.inflate(R.layout.list_item_aluno, parent, false);

            // Get title element - nome da matéria
            final TextView titleTextView =
                    (TextView) rowView.findViewById(R.id.aluno_list_title);
            // Get subtitle element - informações adicionais?
            final TextView subtitleTextView =
                    (TextView) rowView.findViewById(R.id.aluno_list_subtitle);
            // Get thumbnail element - imagem da matéria?
            final ImageView thumbnailImageView =
                    (ImageView) rowView.findViewById(R.id.aluno_list_thumbnail);

            // Populando dados
            titleTextView.setText(aluno.getNome());
            subtitleTextView.setText(aluno.getEmail());

            Picasso.with(mContext).load("photoURL").placeholder(R.drawable.ic_materia).into(thumbnailImageView);
        }
        return rowView;
    }


}
