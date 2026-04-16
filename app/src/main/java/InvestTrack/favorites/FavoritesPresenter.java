package InvestTrack.favorites;

import java.lang.ref.WeakReference;

import android.util.Log;

public class FavoritesPresenter implements FavoritesContract.Presenter {

<<<<<<< ours
  public static String TAG = "InvesTrackSource.FavoritesPresenter";

  private WeakReference<FavoritesContract.View> view;
  private AppMediator mediator;
  private FavoritesContract.Model model;
  private FavoritesState state;

  public FavoritesPresenter(AppMediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public void onCreateCalled() {
    Log.e(TAG, "onCreateCalled()");

    // call the mediator initialize the state
    state = new FavoritesState();


    // use saved state if is necessary
    SavedPreviousFavoritesState savedState = getStateFromPreviousScreen();
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
    SavedNextFavoritesState savedState = getStateFromNextScreen();
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

  private FavoritesState getSavedScreenState() {
    return mediator.getFavoritesScreenState();
  }

  private void saveScreenState() {
    mediator.setFavoritesScreenState(state);
  }
=======
    public static String TAG = "InvesTrackSource.FavoritesPresenter";

    private WeakReference<FavoritesContract.View> view;
    private AppMediator mediator;
    private FavoritesContract.Model model;
    private FavoritesState state;

    public FavoritesPresenter(AppMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onCreateCalled() {
        Log.e(TAG, "onCreateCalled()");

        // call the mediator initialize the state
        state = new FavoritesState();


        // use saved state if is necessary
        SavedPreviousFavoritesState savedState = getStateFromPreviousScreen();
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
        SavedNextFavoritesState savedState = getStateFromNextScreen();
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

    private FavoritesState getSavedScreenState() {
        return mediator.getFavoritesScreenState();
    }

    private void saveScreenState() {
        mediator.setFavoritesScreenState(state);
    }
>>>>>>> theirs


  /*
  private void resetScreenState() {
    mediator.resetFavoritesScreenState();
  }
  */

<<<<<<< ours
  private SavedNextFavoritesState getStateFromNextScreen() {
    return mediator.getNextFavoritesScreenState();
  }

  private void passStateToNextScreen(NewNextFavoritesState state) {
    mediator.setNextFavoritesScreenState(state);
  }

  private void passStateToPreviousScreen(NewPreviousFavoritesState state) {
    mediator.setPreviousFavoritesScreenState(state);
  }

  private SavedPreviousFavoritesState getStateFromPreviousScreen() {
    return mediator.getPreviousFavoritesScreenState();
  }

  @Override
  public void injectView(WeakReference<FavoritesContract.View> view) {
    this.view = view;
  }

  @Override
  public void injectModel(FavoritesContract.Model model) {
    this.model = model;
  }
=======
    private SavedNextFavoritesState getStateFromNextScreen() {
        return mediator.getNextFavoritesScreenState();
    }

    private void passStateToNextScreen(NewNextFavoritesState state) {
        mediator.setNextFavoritesScreenState(state);
    }

    private void passStateToPreviousScreen(NewPreviousFavoritesState state) {
        mediator.setPreviousFavoritesScreenState(state);
    }

    private SavedPreviousFavoritesState getStateFromPreviousScreen() {
        return mediator.getPreviousFavoritesScreenState();
    }

    @Override
    public void injectView(WeakReference<FavoritesContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(FavoritesContract.Model model) {
        this.model = model;
    }
>>>>>>> theirs

}
