package InvestTrack.app;

public class AppMediator {

  private static AppMediator instance;

  private LoginToHomeState loginToHomeState;
  private HomeToDetailState homeToDetailState;
  private DetailToManageState detailToManageState;
  private ProfileToEditProfileState profileToEditProfileState;
  private ProfileToLanguageState profileToLanguageState;
  private ProfileToAppearanceState profileToAppearanceState;
  private ProfileToTermsState profileToTermsState;

  public static AppMediator getInstance() {
    if (instance == null) {
      instance = new AppMediator();
    }
    return instance;
  }

  public LoginToHomeState getNextHomeScreenState() {
    LoginToHomeState state = loginToHomeState;
    loginToHomeState = null;
    return state;
  }

  public void setNextHomeScreenState(LoginToHomeState state) {
    loginToHomeState = state;
  }

  public HomeToDetailState getNextDetailScreenState() {
    HomeToDetailState state = homeToDetailState;
    homeToDetailState = null;
    return state;
  }

  public void setNextDetailScreenState(HomeToDetailState state) {
    homeToDetailState = state;
  }

  public DetailToManageState getNextManageScreenState() {
    DetailToManageState state = detailToManageState;
    detailToManageState = null;
    return state;
  }

  public void setNextManageScreenState(DetailToManageState state) {
    detailToManageState = state;
  }

  public ProfileToEditProfileState getNextEditProfileScreenState() {
    ProfileToEditProfileState state = profileToEditProfileState;
    profileToEditProfileState = null;
    return state;
  }

  public void setNextEditProfileScreenState(ProfileToEditProfileState state) {
    profileToEditProfileState = state;
  }

  public ProfileToLanguageState getNextLanguageScreenState() {
    ProfileToLanguageState state = profileToLanguageState;
    profileToLanguageState = null;
    return state;
  }

  public void setNextLanguageScreenState(ProfileToLanguageState state) {
    profileToLanguageState = state;
  }

  public ProfileToAppearanceState getNextAppearanceScreenState() {
    ProfileToAppearanceState state = profileToAppearanceState;
    profileToAppearanceState = null;
    return state;
  }

  public void setNextAppearanceScreenState(ProfileToAppearanceState state) {
    profileToAppearanceState = state;
  }

  public ProfileToTermsState getNextTermsScreenState() {
    ProfileToTermsState state = profileToTermsState;
    profileToTermsState = null;
    return state;
  }

  public void setNextTermsScreenState(ProfileToTermsState state) {
    profileToTermsState = state;
  }
}
