package com.kingofgranges.max.animeultimetv.presentation.animedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kingofgranges.max.animeultimetv.R;
import com.kingofgranges.max.animeultimetv.data.AnimeUltime;
import com.kingofgranges.max.animeultimetv.domain.AnimeEpisodeModel;
import com.kingofgranges.max.animeultimetv.presentation.StreamActivity;

import java.util.List;

public class AnimeEpisodesFragment extends Fragment {

    private String animeName;
    private List<AnimeEpisodeModel> episodes;

    public void setEpisodes(String animeName, List<AnimeEpisodeModel> episodes) {
        this.animeName = animeName;
        this.episodes = episodes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_anime_episodes, container, false);

        final ListView listEpisode = (ListView) view.findViewById(R.id.listEpisode);
        final Context copyOfThis = getContext();
        listEpisode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AnimeUltime au = new AnimeUltime();

                String videoLink = au.getVideoLink(getContext(), AnimeUltime.mainUrlv5 + episodes.get(position).getLink());
                Intent stream = new Intent(copyOfThis, StreamActivity.class);
                stream.putExtra("streamURL", videoLink);

                startActivity(stream);
            }
        });

        if(episodes == null)
            getActivity().onBackPressed();

        if(episodes != null)
            listEpisode.setAdapter(new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, episodes));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
