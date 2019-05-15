package net.bashayer.baking.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.bashayer.baking.R;
import net.bashayer.baking.VideoPlayerConfig;
import net.bashayer.baking.model.Step;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static net.bashayer.baking.Constants.IS_TWO_PANE_LAYOUT;
import static net.bashayer.baking.Constants.STEP;

public class RecipeStepFragment extends Fragment implements Player.EventListener {

    private Step step;
    private Context context;
    private PlayerView playerView;
    private TextView instruction;
    private ExoPlayer exoPlayer;

    private boolean isTwoPaneLayout = false;

    SimpleExoPlayer player;
    Handler mHandler;
    Runnable mRunnable;

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
                playerView = view.findViewById(R.id.player_view_full);
            } else {
                playerView = view.findViewById(R.id.player_view);
            }

            setStep((Step) bundle.getSerializable(STEP));
        }
    }

    public void setStep(Step step) {
        this.step = step;
        instruction = getActivity().findViewById(R.id.instruction);
        instruction.setText(step.getDescription());
        setPlayImage();
    }

    private void setPlayImage() {
        if (!step.getVideoURL().isEmpty() && playerView != null) {
            playerView.setVisibility(View.VISIBLE);

            initializePlayer();
            buildMediaSource(Uri.parse(step.getVideoURL()));

        } else if (playerView != null) {
            playerView.setVisibility(View.GONE);
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

    private void initializePlayer2() {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, loadControl);
    }

    private void initializePlayer() {
        if (player == null) {
            // 1. Create a default TrackSelector
            LoadControl loadControl = new DefaultLoadControl(
                    new DefaultAllocator(true, 16),
                    VideoPlayerConfig.MIN_BUFFER_DURATION,
                    VideoPlayerConfig.MAX_BUFFER_DURATION,
                    VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                    VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            // 2. Create the player
            player = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultRenderersFactory(getContext()), trackSelector, loadControl);
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);
        }
    }


    private void buildMediaSource(Uri mUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mUri);
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        player.addListener(this);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }

    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pausePlayer();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    public boolean isLandscapeOrientation() {
        boolean result = false;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            result = true;
        }
        return result;
    }

}
