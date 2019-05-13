package net.bashayer.baking.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.bashayer.baking.R;
import net.bashayer.baking.model.Step;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static net.bashayer.baking.Constants.IS_TWO_PANE_LAYOUT;
import static net.bashayer.baking.Constants.STEP;

public class RecipeStepFragment extends Fragment {

    private Step step;
    private Context context;
    private PlayerView videoView;
    private ExoPlayer exoPlayer;

    private boolean isTwoPaneLayout = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            isTwoPaneLayout = bundle.getBoolean(IS_TWO_PANE_LAYOUT);

            if (isLandscapeOrientation() && !isTwoPaneLayout) {
                videoView = view.findViewById(R.id.player_view_full);
            } else {
                videoView = view.findViewById(R.id.player_view);
            }

            setStep((Step) bundle.getSerializable(STEP));
        }
    }

    public void setStep(Step step) {
        this.step = step;
        TextView instruction = getActivity().findViewById(R.id.instruction);
        setPlayImage();

        instruction.setText(step.getDescription());
    }

    private void setPlayImage() {
        if (!step.getVideoURL().isEmpty()) {
            videoView.setVisibility(View.VISIBLE);
            videoView.setPlayer(getPlayer(getContext()));
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playVideo(step.getVideoURL());
                }
            });
        } else {
            videoView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "no video", Toast.LENGTH_LONG).show();
        }
    }

    public void playVideo(String url) {
        String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));

        MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(context, userAgent))
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(url));

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
    }

    private ExoPlayer getPlayer(Context context) {
        this.context = context;
        initializePlayer();
        return exoPlayer;
    }

    private void initializePlayer() {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, loadControl);
    }

    public boolean isLandscapeOrientation() {
        boolean result = false;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            result = true;
        }
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            videoView.setVisibility(View.GONE);
        }
    }


}
