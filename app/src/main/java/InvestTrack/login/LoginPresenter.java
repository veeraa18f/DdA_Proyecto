package InvestTrack.login;

import java.lang.ref.WeakReference;

import android.util.Log;

public class LoginPresenter implements LoginContract.Presenter {

    public static String TAG = "InvesTrackSource.LoginPresenter";

    private WeakReference<LoginContract.View> view;
    private AppMediator mediator;
    private LoginContract.Model model;
    private LoginState state;

    public LoginPresenter(AppMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onCreateCalled() {
        Log.e(TAG, "onCreateCalled()");

        // call the mediator initialize the state
        state = new LoginState();


        // use saved state if is necessary
        SavedPreviousLoginState savedState = getStateFromPreviousScreen();
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
        SavedNextLoginState savedState = getStateFromNextScreen();
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

    private LoginState getSavedScreenState() {
        return mediator.getLoginScreenState();
    }

    private void saveScreenState() {
        mediator.setLoginScreenState(state);
    }


  /*
  private void resetScreenState() {
    mediator.resetLoginScreenState();
  }
  */

    private SavedNextLoginState getStateFromNextScreen() {
        return mediator.getNextLoginScreenState();
    }

    private void passStateToNextScreen(NewNextLoginState state) {
        mediator.setNextLoginScreenState(state);
    }

    private void passStateToPreviousScreen(NewPreviousLoginState state) {
        mediator.setPreviousLoginScreenState(state);
    }

    private SavedPreviousLoginState getStateFromPreviousScreen() {
        return mediator.getPreviousLoginScreenState();
    }

    @Override
    public void injectView(WeakReference<LoginContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(LoginContract.Model model) {
        this.model = model;
    }

}
