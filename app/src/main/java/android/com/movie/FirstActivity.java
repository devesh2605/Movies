package android.com.movie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends Activity {

    private Button buttonHit;
    private ListView listView;
    private MovieAdapter movieAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        buttonHit = (Button) findViewById(R.id.buttonHit);
        listView = (ListView) findViewById(R.id.listview);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");

        buttonHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesDemoList.txt");
            }
        });
    }

    public class JsonTask extends AsyncTask<String, String, List<MovieModels>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<MovieModels> doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentJson = new JSONObject(finalJson);
                JSONArray parentJsonArray = parentJson.getJSONArray("movies");

                List<MovieModels> movieModelsList = new ArrayList<>();

                for (int i = 0; i < parentJsonArray.length(); i++) {
                    JSONObject finalObject = parentJsonArray.getJSONObject(i);
                    String movieName = finalObject.getString("movie");
                    int movieYear = finalObject.getInt("year");
                    MovieModels models = new MovieModels();
                    models.setMovie(movieName);
                    models.setYear(movieYear);

                    movieModelsList.add(models);
                }
                return movieModelsList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieModels> s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            movieAdapter = new MovieAdapter(getApplicationContext(), s);
            listView.setAdapter(movieAdapter);
        }
    }
}
