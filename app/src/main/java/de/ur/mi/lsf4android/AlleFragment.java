package de.ur.mi.lsf4android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AlleFragment extends android.support.v4.app.Fragment //implements View.OnClickListener {

{

    public TextView title;
    private TextView number;
    private TableLayout table;
    private TableRow row;
    private int rowCount = 0;

    public EigeneVeranstaltungenDataSource dataSource;
    public ArrayList<Button> buttonList;
    int rowCounter;


    public AlleFragment() {

        // Required empty public constructor
    }

    public static AlleFragment newInstance() {
        AlleFragment fragment = new AlleFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_alle, container, false);
        new DownloadLSFTask().execute("https://lsf.uni-regensburg.de/qisserver/rds?state=wtree&search=1&root120171=40852|40107|37173|40116|37288|37231&trex=step");
        dataSource = new EigeneVeranstaltungenDataSource(getActivity());
        buttonList = new ArrayList<>();

        return view;
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
                Document doc = Jsoup.connect(urls[0]).get();
                Element table = doc.select("table").last();
                Elements rows = table.select("tr");
                for (Element row : rows) {
                    String[] string_row = new String[3];
                    Elements columns = row.select("td");
                    int i = 0;
                    rowCount++;
                    for (Element column : columns) {
                        switch (i) {
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

            table = (TableLayout) getView().findViewById(R.id.table_alle);
            addRow("Number", "Titel");

            for (int i = 1; i < rowCount; i++) {
                addRow(result.get(i)[0], result.get(i)[1]);

            }
        }





        private void addRow (final String number_text, final String title_text){
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


            number = new TextView(getActivity());
            number.setId(counter+100);
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
           title.setLayoutParams(
                   new TableRow.LayoutParams(
                           TableRow.LayoutParams.MATCH_PARENT,
                           TableRow.LayoutParams.WRAP_CONTENT
                   )
           );



           row.addView(title);


               final String ueberschrift = title_text;
           title.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   callDetailActivity(ueberschrift);
               }
           });

            buttonList.add(new Button(getActivity()));
            row.addView(buttonList.get(rowCounter));

            final String nummer = number_text;

            buttonList.get(rowCounter).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {

                    dataSource.createVeranstaltung(ueberschrift, nummer);
                    CharSequence text = ueberschrift + " wurde in Eigene Veranstaltungen gespeichert";
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
                    toast.show();


                }
            });

            rowCounter++;

          //  row.addView(new Button (getActivity()));
           //row.addView(new Button (getActivity(), null, R.styles.Button));

           table.addView(row, new TableLayout.LayoutParams(
                           TableLayout.LayoutParams.MATCH_PARENT,
                           TableLayout.LayoutParams.WRAP_CONTENT
                   )
           );

       }

    }
}