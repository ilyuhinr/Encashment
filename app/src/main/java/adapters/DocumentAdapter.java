package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import alliancesofware.ru.encashment.R;
import bean.DocumentEncashment;

/**
 * Created by User on 14.08.2017.
 */

public class DocumentAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DocumentEncashment> documents;
    private LayoutInflater lInflater;

    public DocumentAdapter(Context context, ArrayList<DocumentEncashment> documentEncashments) {
        this.mContext = context;
        this.documents = documentEncashments;
        lInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return documents != null ? documents.size() : 0;
    }

    @Override
    public DocumentEncashment getItem(int position) {
        return documents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.doc_item_row, parent, false);
        }
        DocumentEncashment documentEncashment = getItem(position);

        TextView nameDoc = (TextView) view.findViewById(R.id.number_item_doc);
        TextView dateDoc = (TextView) view.findViewById(R.id.date_doc);
        TextView contarctorName = (TextView) view.findViewById(R.id.name_contarcting_doc);
        TextView numberDoc = (TextView) view.findViewById(R.id.no_docs);
        TextView summ = (TextView) view.findViewById(R.id.sum_docs);

        nameDoc.setText(String.valueOf(position + 1));

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateDoc.setText(sdf.format(documentEncashment.getDateDoc().getTime()));
        String status = "";
        if(documentEncashment.getStatus() == 0){
            status = "Не выполнено";
        }
        else if(documentEncashment.getStatus() == 1){
            status = "Выполнено, не выгружено";
        }
        else {
            status = "Выполнено, выгружено";
        }
        contarctorName.setText(documentEncashment.getContractorName() + "\n" + status);
        numberDoc.setText(documentEncashment.getNumber());
        summ.setText(documentEncashment.getSumm() + " руб. ");
        return view;
    }
}
