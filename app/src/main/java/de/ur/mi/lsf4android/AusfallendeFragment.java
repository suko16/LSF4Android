package de.ur.mi.lsf4android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

public class AusfallendeFragment extends android.support.v4.app.Fragment {

    public Veranstaltung veranstaltung;
    private TextView title;
    private TextView begin;
    private TextView ende;
    private TableLayout table;
    private TableRow row;
    private int rowCount = 0;

    public AusfallendeFragment() {
        // Required empty public constructor
    }

    public static AusfallendeFragment newInstance() {
        AusfallendeFragment fragment = new AusfallendeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        new DownloadLSFTask().execute("https://lsf.uni-regensburg.de/qisserver/rds?state=currentLectures&type=1&next=CurrentLectures.vm&nextdir=ressourcenManager&navigationPosition=lectures%2CcanceledLectures&breadcrumb=canceledLectures&topitem=lectures&subitem=canceledLectures&&HISCalendar_Date=27.04.2017&asi=");
        //title = (TextView) getView().findViewById(R.id.title_tabelle);
        return inflater.inflate(R.layout.fragment_ausfallende, container, false);
    }

    private void callDetailActivity(String titel) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.TITEL_EXTRA, titel);
        startActivity(intent);
    }

    private class DownloadLSFTask extends AsyncTask<String, Integer, ArrayList<String[]>> {
        protected ArrayList<String[]> doInBackground(String... urls) {
            ArrayList<String[]> result = new ArrayList<>();
            try {
                Document doc  = Jsoup.connect(urls[0]).get();
                Element table = doc.select("table").last();
                Elements rows = table.select("tr");
                for(Element row : rows) {
                    String[] string_row = new String[3];
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
                            case 3:
                                string_row[2] = column.select("a").text();
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
            // Tabelle in fragment_ausfallende.xml bauen und mit result befüllen
            //String titel = result.get(1)[2];
            //callDetailActivity(titel);
            table = (TableLayout) getView().findViewById(R.id.table);
            addRow("Beginn", "Ende", "Titel");

            for(int i= 1; i< rowCount ; i++){
                    veranstaltung = new Veranstaltung(result.get(i)[0], result.get(i)[1], result.get(i)[2]);
                    addRow(veranstaltung.getBeginn(), veranstaltung.getEnde(), veranstaltung.getTitel());
            }
        }

        private void addRow (String begin_text, String end_text, String title_text ){
            int counter = 0;
            counter++;

            row = new TableRow(getActivity());
          //  row = (TableRow) getView().findViewById(R.id.row1);
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

            title = new TextView(getActivity());
            title.setId(counter+100);
            title.setText(title_text);
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
            row.addView(title);

            table.addView(row, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                    )
            );

        }
    }

}
