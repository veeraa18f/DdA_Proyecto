package InvestTrack.app;

public class AppMediator {

  private static AppMediator instance;

  private LoginToHomeState loginToHomeState;
  private HomeToDetailState homeToDetailState;
  private DetailToManageState detailToManageState;
  private ManageToHomeState manageToHomeState;
  private HomeToFavoritesState homeToFavoritesState;

  private UserState userState;

  public static AppMediator getInstance() {
    if (instance == null) {
      instance = new AppMediator();
    }
    return instance;
  }

  // 🔹 Login → Home
  public void setNextHomeScreenState(LoginToHomeState state) {
    loginToHomeState = state;
  }

  public LoginToHomeState getNextHomeScreenState() {
    LoginToHomeState state = loginToHomeState;
    loginToHomeState = null;
    return state;
  }

  // 🔹 Home → Detail
  public void setNextDetailScreenState(HomeToDetailState state) {
    homeToDetailState = state;
  }

  public HomeToDetailState getNextDetailScreenState() {
    HomeToDetailState state = homeToDetailState;
    homeToDetailState = null;
    return state;
  }

  // 🔹 Detail → Manage
  public void setNextManageScreenState(DetailToManageState state) {
    detailToManageState = state;
  }

  public DetailToManageState getNextManageScreenState() {
    DetailToManageState state = detailToManageState;
    detailToManageState = null;
    return state;
  }

  // 🔹 Manage → Home
  public void setPreviousHomeScreenState(ManageToHomeState state) {
    manageToHomeState = state;
  }

  public ManageToHomeState getPreviousHomeScreenState() {
    ManageToHomeState state = manageToHomeState;
    manageToHomeState = null;
    return state;
  }

  // 🔹 Home → Favorites
  public void setNextFavoritesScreenState(HomeToFavoritesState state) {
    homeToFavoritesState = state;
  }

  public HomeToFavoritesState getNextFavoritesScreenState() {
    HomeToFavoritesState state = homeToFavoritesState;
    homeToFavoritesState = null;
    return state;
  }

  // 🔹 Usuario global
  public void setUserState(UserState state) {
    userState = state;
  }

  public UserState getUserState() {
    return userState;
  }
}