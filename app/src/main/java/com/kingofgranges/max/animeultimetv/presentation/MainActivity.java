package com.kingofgranges.max.animeultimetv.presentation;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.kingofgranges.max.animeultimetv.R;
import com.kingofgranges.max.animeultimetv.data.AnimeUltime;
import com.kingofgranges.max.animeultimetv.data.AnimeUltimeService;
import com.kingofgranges.max.animeultimetv.data.SearchNetworkModel;
import com.kingofgranges.max.animeultimetv.domain.AnimeModel;
import com.kingofgranges.max.animeultimetv.presentation.animedetails.AnimeDetailsActivity;

import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public final AnimeUltime au = new AnimeUltime();

    private AnimeUltimeService auService;
    private Call<List<SearchNetworkModel>> searchCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initRetrofit();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://v5.anime-ultime.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        auService = retrofit.create(AnimeUltimeService.class);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    updateSearch(s);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        return true;
    }

    public void updateSearch(String query) throws IOException, JSONException {
        if (query.length() < 2) return;

        if (searchCall != null) searchCall.cancel();

        searchCall = auService.search(query);
        searchCall.enqueue(new Callback<List<SearchNetworkModel>>() {
            @Override
            public void onResponse(Call<List<SearchNetworkModel>> call, Response<List<SearchNetworkModel>> response) {
                List<String> titles = new ArrayList<>(response.body().size());
                for (SearchNetworkModel item : response.body()) {
                    titles.add(item.getTitle());
                }

                final ListView listView = (ListView) findViewById(R.id.searchCompletion);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, titles);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AnimeModel data = au.getPageInformation(position);
                        if (data == null) {
                            Toast.makeText(getApplicationContext(), "Error during the process for fetching data about the anime :/", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent details = new Intent(MainActivity.this, AnimeDetailsActivity.class);
                        details.putExtra(AnimeDetailsActivity.EXTRA_ANIME, data);
                        startActivity(details);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<SearchNetworkModel>> call, Throwable t) {
                // Do nothing
            }
        });
    }
}