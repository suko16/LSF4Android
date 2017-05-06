package de.ur.mi.lsf4android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class AusfallendeFragment extends android.support.v4.app.Fragment {

    public Veranstaltung veranstaltung;
    private TextView title;
    private TextView begin;
    private TextView ende;
    private TextView number;
    public TableLayout table;
    public TableRow row;
    private int rowCount = 0;
    private EigeneVeranstaltungenDataSource dataSource;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public ArrayList<String[]> result;

    public AusfallendeFragment() {
        // Required empty public constructor
    }

    public static AusfallendeFragment newInstance() {
        AusfallendeFragment fragment = new AusfallendeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new DownloadLSFTask().execute("https://lsf.uni-regensburg.de/qisserver/rds?state=currentLectures&type=1&next=CurrentLectures.vm&nextdir=ressourcenManager&navigationPosition=lectures%2CcanceledLectures&breadcrumb=canceledLectures&topitem=lectures&subitem=canceledLectures&&HISCalendar_Date=27.04.2017&asi=");

        View view = inflater.inflate(R.layout.fragment_ausfallende, container, false);
        table = (TableLayout) view.findViewById(R.id.table_ausfallende);

        return view;
    }


    //ruft die Detailansicht der Veranstaltung auf
    private void callDetailActivity(String titel) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.TITEL_EXTRA, titel);
        startActivity(intent);
    }

    private class DownloadLSFTask extends AsyncTask<String, Integer, ArrayList<String[]>> {
        protected ArrayList<String[]> doInBackground(String... urls) {
           result = new ArrayList<>();
            try {
                Document doc  = Jsoup.connect(urls[0]).get();
                Element table = doc.select("table").last();
                Elements rows = table.select("tr");
                for(Element row : rows) {
                    String[] string_row = new String[4];
                    Elements columns = row.select("td");
                    int i = 0;
                    rowCount++;
                    for(Element column : columns) {
                        switch(i) {
                            case 0:
                                string_row[0] = column.text();
                                break;
                            case 1:
                                string_row[1] = column.text();
                                break;
                            case 2:
                                string_row[2] = column.text();
                                break;
                            case 3:
                                string_row[3] = column.select("a").text();
                                break;
                        }
                        i++;
                    }
                    result.add(string_row);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                return result;
            }
        }


        protected void onPostExecute(ArrayList<String[]> result) {

            addRow("Beginn", "Ende", "Nummer", "Titel");
            for(int i= 1; i< rowCount ; i++){
                    veranstaltung = new Veranstaltung(result.get(i)[0], result.get(i)[1], result.get(i)[2], result.get(i)[3]);
                    addRow(veranstaltung.getBeginn(), veranstaltung.getEnde(), veranstaltung.getNumber(), veranstaltung.getTitel());
            }

        }





        //Fügt in der Tabelle die Zeilen mit den entsprechenden Werten hinzu
        private void addRow (String begin_text, String end_text, String number_text, String title_text ){
            int counter = 0;
            counter++;

            row = new TableRow(getActivity());
            row.setId(counter);
            row.setLayoutParams(
                    new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    )
            );

            begin = new TextView(getActivity());
            begin.setId(counter);
            begin.setText(begin_text);
            begin.setLayoutParams(
                    new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    )
            );

            row.addView(begin);

            ende = new TextView(getActivity());
            ende.setId(counter+200);
            ende.setText(end_text);
            ende.setLayoutParams(
                    new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    )
            );

            row.addView(ende);


            number = new TextView(getActivity());
            number.setId(counter+300);
            number.setText(number_text);
            number.setLayoutParams(
                    new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    )
            );




            row.addView(number);


            title = new TextView(getActivity());
            title.setId(counter+100);
            title.setText(title_text);
            title.setBackgroundColor(0xFFFFFF);
            title.setLayoutParams(
                    new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    )
            );


            final String ueberschrift = title_text;
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callDetailActivity(ueberschrift);
                }
            });



            //Überprüft auf Übereinstimmungen zwischen Datenbank und Ausfallenden

            dataSource = new EigeneVeranstaltungenDataSource(getActivity());
            dataSource.open();
            List<EigeneV_Objekt> Veranstaltungsliste = dataSource.getAllVeranstaltungen();

            for (int j = 0; j < Veranstaltungsliste.size(); j++){
                if(Veranstaltungsliste.get(j).getNumber().equals(number_text)){
                    row.setBackgroundColor(0xFF00FF00);
                }
            }




            row.addView(title);



            table.addView(row, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                    )
            );

        }



    }

}
