package com.example.android.bakingrecipe.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.R2;
import com.example.android.bakingrecipe.models.RecipeStep;
import com.example.android.bakingrecipe.utils.ArgKeys;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepDetailsFragment extends Fragment implements ExoPlayer.EventListener{


    private static final String LOG_TAG = RecipeStepDetailsFragment.class.getSimpleName();
    private ArrayList<RecipeStep> mRecipeStepsLists;
    private int mPosition;

    @BindView(R2.id.next_btn) Button mNextButton;

    @BindView(R2.id.prev_btn) Button mPrevButton;

    @BindView(R2.id.playerView) SimpleExoPlayerView mExoPlayerView;

    @BindView(R2.id.thumbnail) ImageView mThumbnail;

    @BindView(R2.id.tv_step_description) TextView mStepDescription;


    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public RecipeStepDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_details, container, false);

        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mRecipeStepsLists = savedInstanceState.getParcelableArrayList(ArgKeys.RECIPE_STEPS_ARG_ID);
            mPosition = savedInstanceState.getInt(ArgKeys.RECIPE_POSITION_ARG_ID);
        } else {
            mRecipeStepsLists = getArguments().getParcelableArrayList(ArgKeys.RECIPE_STEPS_ARG_ID);
            mPosition = getArguments().getInt(ArgKeys.RECIPE_POSITION_ARG_ID);
        }
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        cleanUpVideo();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpUI();
    }

    @OnClick(R2.id.next_btn)
    public void nextRecipe(View view) {
        if (mPosition < mRecipeStepsLists.size()-1) {
            mPosition = mPosition+1;
            cleanUpVideo();
            setUpUI();
        }
    }

    @OnClick(R2.id.prev_btn)
    public void prevRecipe(View view) {
        if (mPosition > 0) {
            mPosition = mPosition-1;
            cleanUpVideo();
            setUpUI();
        }
    }

    private void setUpUI(){

        RecipeStep recipeStep = mRecipeStepsLists.get(mPosition);

        // Hide or show video frame
        if (TextUtils.isEmpty(recipeStep.getVideoURL())) {
            mExoPlayerView.setVisibility(View.GONE);
            // Load the question mark as the background image until the user answers the question.
            mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.cake_ingredients));
        } else {
            mExoPlayerView.setVisibility(View.VISIBLE);

            // Load the question mark as the background image until the user answers the question.
            mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.cake_ingredients));

            // Initialize the Media Ses
            initializeMediaSession();

            Uri uri =  Uri.parse(recipeStep.getVideoURL());
            initializeVideo(uri);
        }

        // Hide or show Image thumbnail
        if (TextUtils.isEmpty(recipeStep.getThumbnailURL())) {
            mThumbnail.setVisibility(View.GONE);
        } else {
            mThumbnail.setVisibility(View.VISIBLE);

            Glide.with(getContext())
                    .load(recipeStep.getThumbnailURL())
                    .placeholder(R.drawable.cake_ingredients)
                    .centerCrop()
                    .dontAnimate()
                    .into(mThumbnail);
        }

        // Hide or show description
        if (TextUtils.isEmpty(recipeStep.getDescription())) {
            mStepDescription.setVisibility(View.GONE);
        } else {
            mStepDescription.setVisibility(View.VISIBLE);
            mStepDescription.setText(recipeStep.getDescription());
        }

        // Hide or show next button
        if (mPosition == mRecipeStepsLists.size()-1) {
            mNextButton.setVisibility(View.GONE);
        } else {
            mNextButton.setVisibility(View.VISIBLE);
        }

        // Hide or show prev button
        if (mPosition == 0) {
            mPrevButton.setVisibility(View.GONE);
        } else {
            mPrevButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ArgKeys.SCROLLED_POSITION_ARG_ID, mPosition);
        outState.putParcelableArrayList(ArgKeys.RECIPE_INGREDIENTS_LISTS_ARG_ID, mRecipeStepsLists);
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), getString(R.string.app_name));

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    private void initializeVideo(Uri videoURL) {
        Log.d(LOG_TAG, "Initializing video...");
        Log.d(LOG_TAG, videoURL.toString());

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(videoURL, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanUpVideo();
        if(mMediaSession!=null) {
            mMediaSession.setActive(false);
        }
    }
    private void cleanUpVideo() {
        // Clear video id present
        Log.d(LOG_TAG, "Clearing video...");
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
