package com.tuempresa.investtrack.core.navigation;

public class AppMediator {

  private static final AppMediator INSTANCE = new AppMediator();

  private HomeToDetailState homeToDetailState;
  private DetailToManageState detailToManageState;

  private AppMediator() {
  }

  public static AppMediator getInstance() {
    return INSTANCE;
  }

  public void setNextDetailScreenState(HomeToDetailState state) {
    homeToDetailState = state;
  }

  public HomeToDetailState getNextDetailScreenState() {
    HomeToDetailState state = homeToDetailState;
    homeToDetailState = null;
    return state;
  }

  public void setNextManageScreenState(DetailToManageState state) {
    detailToManageState = state;
  }

  public DetailToManageState getNextManageScreenState() {
    DetailToManageState state = detailToManageState;
    detailToManageState = null;
    return state;
  }
}
