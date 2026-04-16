package InvestTrack.manage;

import android.util.Log;

<<<<<<< ours
public class ManageInvestmentModel implements ManageInvestmentContract.Model {

  public static String TAG = "InvesTrackSource.ManageInvestmentModel";

  private String data;

  public ManageInvestmentModel(String data) {
    this.data = data;
  }

  @Override
  public String getStoredData() {
    // Log.e(TAG, "getStoredData()");

    return data;
  }


  @Override
  public String getSavedData() {
    // Log.e(TAG, "getSavedData()");

    return data;
  }


  @Override
  public String getCurrentData() {
    // Log.e(TAG, "getCurrentData()");

    return data;
  }

  @Override
  public void setCurrentData(String data) {
    // Log.e(TAG, "setCurrentData()");

    this.data = data;
  }

  @Override
  public void onUpdatedDataFromRecreatedScreen(String data) {
    // Log.e(TAG, "onUpdatedDataFromRecreatedScreen()");


  }

  @Override
  public void onUpdatedDataFromNextScreen(String data) {
    // Log.e(TAG, "onUpdatedDataFromNextScreen()");


  }

  @Override
  public void onUpdatedDataFromPreviousScreen(String data) {
    // Log.e(TAG, "onUpdatedDataFromPreviousScreen()");


  }
=======
public class
ManageInvestmentModel implements ManageInvestmentContract.Model {

    public static String TAG = "InvesTrackSource.ManageInvestmentModel";

    private String data;

    public ManageInvestmentModel(String data) {
        this.data = data;
    }

    @Override
    public String getStoredData() {
        // Log.e(TAG, "getStoredData()");

        return data;
    }


    @Override
    public String getSavedData() {
        // Log.e(TAG, "getSavedData()");

        return data;
    }


    @Override
    public String getCurrentData() {
        // Log.e(TAG, "getCurrentData()");

        return data;
    }

    @Override
    public void setCurrentData(String data) {
        // Log.e(TAG, "setCurrentData()");

        this.data = data;
    }

    @Override
    public void onUpdatedDataFromRecreatedScreen(String data) {
        // Log.e(TAG, "onUpdatedDataFromRecreatedScreen()");


    }

    @Override
    public void onUpdatedDataFromNextScreen(String data) {
        // Log.e(TAG, "onUpdatedDataFromNextScreen()");


    }

    @Override
    public void onUpdatedDataFromPreviousScreen(String data) {
        // Log.e(TAG, "onUpdatedDataFromPreviousScreen()");


    }
>>>>>>> theirs
}
