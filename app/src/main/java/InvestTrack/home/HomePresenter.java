package InvestTrack.home;

import java.lang.ref.WeakReference;

import android.util.Log;

public class HomePresenter implements HomeContract.Presenter {

<<<<<<< ours
  public static String TAG = "InvesTrackSource.HomePresenter";

  private WeakReference<HomeContract.View> view;
  private AppMediator mediator;
  private HomeContract.Model model;
  private HomeState state;

  public HomePresenter(AppMediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public void onCreateCalled() {
    Log.e(TAG, "onCreateCalled()");

    // call the mediator initialize the state
    state = new HomeState();


    // use saved state if is necessary
    SavedPreviousHomeState savedState = getStateFromPreviousScreen();
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
    SavedNextHomeState savedState = getStateFromNextScreen();
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

  private HomeState getSavedScreenState() {
    return mediator.getHomeScreenState();
  }

  private void saveScreenState() {
    mediator.setHomeScreenState(state);
  }
=======
    public static String TAG = "InvesTrackSource.HomePresenter";

    private WeakReference<HomeContract.View> view;
    private AppMediator mediator;
    private HomeContract.Model model;
    private HomeState state;

    public HomePresenter(AppMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onCreateCalled() {
        Log.e(TAG, "onCreateCalled()");

        // call the mediator initialize the state
        state = new HomeState();


        // use saved state if is necessary
        SavedPreviousHomeState savedState = getStateFromPreviousScreen();
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
        SavedNextHomeState savedState = getStateFromNextScreen();
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

    private HomeState getSavedScreenState() {
        return mediator.getHomeScreenState();
    }

    private void saveScreenState() {
        mediator.setHomeScreenState(state);
    }
>>>>>>> theirs


  /*
  private void resetScreenState() {
    mediator.resetHomeScreenState();
  }
  */

<<<<<<< ours
  private SavedNextHomeState getStateFromNextScreen() {
    return mediator.getNextHomeScreenState();
  }

  private void passStateToNextScreen(NewNextHomeState state) {
    mediator.setNextHomeScreenState(state);
  }

  private void passStateToPreviousScreen(NewPreviousHomeState state) {
    mediator.setPreviousHomeScreenState(state);
  }

  private SavedPreviousHomeState getStateFromPreviousScreen() {
    return mediator.getPreviousHomeScreenState();
  }

  @Override
  public void injectView(WeakReference<HomeContract.View> view) {
    this.view = view;
  }

  @Override
  public void injectModel(HomeContract.Model model) {
    this.model = model;
  }
=======
    private SavedNextHomeState getStateFromNextScreen() {
        return mediator.getNextHomeScreenState();
    }

    private void passStateToNextScreen(NewNextHomeState state) {
        mediator.setNextHomeScreenState(state);
    }

    private void passStateToPreviousScreen(NewPreviousHomeState state) {
        mediator.setPreviousHomeScreenState(state);
    }

    private SavedPreviousHomeState getStateFromPreviousScreen() {
        return mediator.getPreviousHomeScreenState();
    }

    @Override
    public void injectView(WeakReference<HomeContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(HomeContract.Model model) {
        this.model = model;
    }
>>>>>>> theirs

}
