package InvestTrack.profile;

import java.lang.ref.WeakReference;

import android.util.Log;

public class ProfilePresenter implements ProfileContract.Presenter {

<<<<<<< ours
  public static String TAG = "InvesTrackSource.ProfilePresenter";

  private WeakReference<ProfileContract.View> view;
  private AppMediator mediator;
  private ProfileContract.Model model;
  private ProfileState state;

  public ProfilePresenter(AppMediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public void onCreateCalled() {
    Log.e(TAG, "onCreateCalled()");

    // call the mediator initialize the state
    state = new ProfileState();


    // use saved state if is necessary
    SavedPreviousProfileState savedState = getStateFromPreviousScreen();
    if (savedState != null) {

      // update the model if is necessary
      model.onUpdatedDataFromPreviousScreen(savedState.data);

    }

  }

  @Override
  public void onRecreateCalled() {
    Log.e(TAG, "onRecreateCalled()");

    // call the mediator to initialize the state
    state = getSavedScreenState();

    // update the model if is necessary
    model.onUpdatedDataFromRecreatedScreen(state.data);
  }

  @Override
  public void onResumeCalled() {
    Log.e(TAG, "onResumeCalled()");


    // use passed state if is necessary
    SavedNextProfileState savedState = getStateFromNextScreen();
    if (savedState != null) {

      // update the model if is necessary
      model.onUpdatedDataFromNextScreen(savedState.data);

    }

    // call the model and initialize the state
    state.data = model.getCurrentData();

    // update the view
    view.get().onRefreshViewWithUpdatedData(state);

  }

  @Override
  public void onBackButtonPressed() {
    Log.e(TAG, "onBackButtonPressed()");

  }

  @Override
  public void onPauseCalled() {
    Log.e(TAG, "onPauseCalled()");

    // save the state
    saveScreenState();
  }

  @Override
  public void onDestroyCalled() {
    Log.e(TAG, "onDestroyCalled()");

    // reset the state if is necessary
    //resetScreenState();
  }

  private ProfileState getSavedScreenState() {
    return mediator.getProfileScreenState();
  }

  private void saveScreenState() {
    mediator.setProfileScreenState(state);
  }
=======
    public static String TAG = "InvesTrackSource.ProfilePresenter";

    private WeakReference<ProfileContract.View> view;
    private AppMediator mediator;
    private ProfileContract.Model model;
    private ProfileState state;

    public ProfilePresenter(AppMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onCreateCalled() {
        Log.e(TAG, "onCreateCalled()");

        // call the mediator initialize the state
        state = new ProfileState();


        // use saved state if is necessary
        SavedPreviousProfileState savedState = getStateFromPreviousScreen();
        if (savedState != null) {

            // update the model if is necessary
            model.onUpdatedDataFromPreviousScreen(savedState.data);

        }

    }

    @Override
    public void onRecreateCalled() {
        Log.e(TAG, "onRecreateCalled()");

        // call the mediator to initialize the state
        state = getSavedScreenState();

        // update the model if is necessary
        model.onUpdatedDataFromRecreatedScreen(state.data);
    }

    @Override
    public void onResumeCalled() {
        Log.e(TAG, "onResumeCalled()");


        // use passed state if is necessary
        SavedNextProfileState savedState = getStateFromNextScreen();
        if (savedState != null) {

            // update the model if is necessary
            model.onUpdatedDataFromNextScreen(savedState.data);

        }

        // call the model and initialize the state
        state.data = model.getCurrentData();

        // update the view
        view.get().onRefreshViewWithUpdatedData(state);

    }

    @Override
    public void onBackButtonPressed() {
        Log.e(TAG, "onBackButtonPressed()");

    }

    @Override
    public void onPauseCalled() {
        Log.e(TAG, "onPauseCalled()");

        // save the state
        saveScreenState();
    }

    @Override
    public void onDestroyCalled() {
        Log.e(TAG, "onDestroyCalled()");

        // reset the state if is necessary
        //resetScreenState();
    }

    private ProfileState getSavedScreenState() {
        return mediator.getProfileScreenState();
    }

    private void saveScreenState() {
        mediator.setProfileScreenState(state);
    }
>>>>>>> theirs


  /*
  private void resetScreenState() {
    mediator.resetProfileScreenState();
  }
  */

<<<<<<< ours
  private SavedNextProfileState getStateFromNextScreen() {
    return mediator.getNextProfileScreenState();
  }

  private void passStateToNextScreen(NewNextProfileState state) {
    mediator.setNextProfileScreenState(state);
  }

  private void passStateToPreviousScreen(NewPreviousProfileState state) {
    mediator.setPreviousProfileScreenState(state);
  }

  private SavedPreviousProfileState getStateFromPreviousScreen() {
    return mediator.getPreviousProfileScreenState();
  }

  @Override
  public void injectView(WeakReference<ProfileContract.View> view) {
    this.view = view;
  }

  @Override
  public void injectModel(ProfileContract.Model model) {
    this.model = model;
  }
=======
    private SavedNextProfileState getStateFromNextScreen() {
        return mediator.getNextProfileScreenState();
    }

    private void passStateToNextScreen(NewNextProfileState state) {
        mediator.setNextProfileScreenState(state);
    }

    private void passStateToPreviousScreen(NewPreviousProfileState state) {
        mediator.setPreviousProfileScreenState(state);
    }

    private SavedPreviousProfileState getStateFromPreviousScreen() {
        return mediator.getPreviousProfileScreenState();
    }

    @Override
    public void injectView(WeakReference<ProfileContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(ProfileContract.Model model) {
        this.model = model;
    }
>>>>>>> theirs

}
