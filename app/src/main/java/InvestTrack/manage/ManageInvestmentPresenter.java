package InvestTrack.manage;

import java.lang.ref.WeakReference;

import android.util.Log;

public class ManageInvestmentPresenter implements ManageInvestmentContract.Presenter {

<<<<<<< ours
  public static String TAG = "InvesTrackSource.ManageInvestmentPresenter";

  private WeakReference<ManageInvestmentContract.View> view;
  private AppMediator mediator;
  private ManageInvestmentContract.Model model;
  private ManageInvestmentState state;

  public ManageInvestmentPresenter(AppMediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public void onCreateCalled() {
    Log.e(TAG, "onCreateCalled()");

    // call the mediator initialize the state
    state = new ManageInvestmentState();


    // use saved state if is necessary
    SavedPreviousManageInvestmentState savedState = getStateFromPreviousScreen();
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
    SavedNextManageInvestmentState savedState = getStateFromNextScreen();
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

  private ManageInvestmentState getSavedScreenState() {
    return mediator.getManageInvestmentScreenState();
  }

  private void saveScreenState() {
    mediator.setManageInvestmentScreenState(state);
  }
=======
    public static String TAG = "InvesTrackSource.ManageInvestmentPresenter";

    private WeakReference<ManageInvestmentContract.View> view;
    private AppMediator mediator;
    private ManageInvestmentContract.Model model;
    private ManageInvestmentState state;

    public ManageInvestmentPresenter(AppMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onCreateCalled() {
        Log.e(TAG, "onCreateCalled()");

        // call the mediator initialize the state
        state = new ManageInvestmentState();


        // use saved state if is necessary
        SavedPreviousManageInvestmentState savedState = getStateFromPreviousScreen();
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
        SavedNextManageInvestmentState savedState = getStateFromNextScreen();
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

    private ManageInvestmentState getSavedScreenState() {
        return mediator.getManageInvestmentScreenState();
    }

    private void saveScreenState() {
        mediator.setManageInvestmentScreenState(state);
    }
>>>>>>> theirs


  /*
  private void resetScreenState() {
    mediator.resetManageInvestmentScreenState();
  }
  */

<<<<<<< ours
  private SavedNextManageInvestmentState getStateFromNextScreen() {
    return mediator.getNextManageInvestmentScreenState();
  }

  private void passStateToNextScreen(NewNextManageInvestmentState state) {
    mediator.setNextManageInvestmentScreenState(state);
  }

  private void passStateToPreviousScreen(NewPreviousManageInvestmentState state) {
    mediator.setPreviousManageInvestmentScreenState(state);
  }

  private SavedPreviousManageInvestmentState getStateFromPreviousScreen() {
    return mediator.getPreviousManageInvestmentScreenState();
  }

  @Override
  public void injectView(WeakReference<ManageInvestmentContract.View> view) {
    this.view = view;
  }

  @Override
  public void injectModel(ManageInvestmentContract.Model model) {
    this.model = model;
  }
=======
    private SavedNextManageInvestmentState getStateFromNextScreen() {
        return mediator.getNextManageInvestmentScreenState();
    }

    private void passStateToNextScreen(NewNextManageInvestmentState state) {
        mediator.setNextManageInvestmentScreenState(state);
    }

    private void passStateToPreviousScreen(NewPreviousManageInvestmentState state) {
        mediator.setPreviousManageInvestmentScreenState(state);
    }

    private SavedPreviousManageInvestmentState getStateFromPreviousScreen() {
        return mediator.getPreviousManageInvestmentScreenState();
    }

    @Override
    public void injectView(WeakReference<ManageInvestmentContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(ManageInvestmentContract.Model model) {
        this.model = model;
    }
>>>>>>> theirs

}
