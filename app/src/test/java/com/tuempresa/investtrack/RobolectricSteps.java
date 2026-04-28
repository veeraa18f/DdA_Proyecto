package com.tuempresa.investtrack;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.test.core.app.ApplicationProvider;

import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowToast;

import com.tuempresa.investtrack.core.navigation.AppMediator;
import com.tuempresa.investtrack.core.navigation.DetailToManageState;
import com.tuempresa.investtrack.core.navigation.HomeToDetailState;
import com.tuempresa.investtrack.data.model.Asset;
import com.tuempresa.investtrack.data.repository.InvestTrackRepository;
import com.tuempresa.investtrack.feature.portfolio.detail.DetailActivity;
import com.tuempresa.investtrack.feature.portfolio.favorites.FavoritesActivity;
import com.tuempresa.investtrack.feature.auth.forgot.ForgotPasswordActivity;
import com.tuempresa.investtrack.feature.portfolio.home.HomeActivity;
import com.tuempresa.investtrack.feature.auth.login.LoginActivity;
import com.tuempresa.investtrack.feature.portfolio.add.AddInvestmentActivity;
import com.tuempresa.investtrack.feature.portfolio.manage.ManageInvestmentActivity;
import com.tuempresa.investtrack.feature.profile.edit.EditProfileActivity;
import com.tuempresa.investtrack.feature.profile.view.ProfileActivity;
import com.tuempresa.investtrack.feature.auth.register.RegisterActivity;
import com.tuempresa.investtrack.core.session.AppPreferences;

public class RobolectricSteps {

    private static final String PREFS_NAME = "investtrack_preferences";

    private final Context context = ApplicationProvider.getApplicationContext();

    private ActivityController<LoginActivity> loginCtrl;
    private ActivityController<RegisterActivity> registerCtrl;
    private ActivityController<ForgotPasswordActivity> forgotCtrl;
    private ActivityController<HomeActivity> homeCtrl;
    private ActivityController<DetailActivity> detailCtrl;
    private ActivityController<FavoritesActivity> favoritesCtrl;
    private ActivityController<AddInvestmentActivity> addCtrl;
    private ActivityController<ManageInvestmentActivity> manageCtrl;
    private ActivityController<ProfileActivity> profileCtrl;
    private ActivityController<EditProfileActivity> editProfileCtrl;

    public void resetApp() {
        closeControllers();
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit();
        InvestTrackRepository.resetInstanceForTests(context);
        AppMediator.getInstance().getNextDetailScreenState();
        AppMediator.getInstance().getNextManageScreenState();
        ShadowToast.reset();
    }

    public void closeControllers() {
        destroyIfNeeded(editProfileCtrl);
        destroyIfNeeded(profileCtrl);
        destroyIfNeeded(manageCtrl);
        destroyIfNeeded(addCtrl);
        destroyIfNeeded(favoritesCtrl);
        destroyIfNeeded(detailCtrl);
        destroyIfNeeded(homeCtrl);
        destroyIfNeeded(forgotCtrl);
        destroyIfNeeded(registerCtrl);
        destroyIfNeeded(loginCtrl);
    }

    public void iniciamosLogin() {
        loginCtrl = Robolectric.buildActivity(LoginActivity.class);
        loginCtrl.create().resume().visible();
    }

    public void iniciamosRegistro() {
        registerCtrl = Robolectric.buildActivity(RegisterActivity.class);
        registerCtrl.create().resume().visible();
    }

    public void iniciamosForgotPassword() {
        forgotCtrl = Robolectric.buildActivity(ForgotPasswordActivity.class);
        forgotCtrl.create().resume().visible();
    }

    public void iniciamosHomeComoInvitado() {
        AppPreferences.enableGuestMode(context);
        iniciamosHome();
    }

    public void iniciamosHomeComoDemo() {
        loginAsDemo();
        iniciamosHome();
    }

    public void iniciamosHome() {
        homeCtrl = Robolectric.buildActivity(HomeActivity.class);
        homeCtrl.create().resume().visible();
    }

    public void iniciamosDetalle(String assetId) {
        AppMediator.getInstance().setNextDetailScreenState(new HomeToDetailState(assetId));
        detailCtrl = Robolectric.buildActivity(DetailActivity.class);
        detailCtrl.create().resume().visible();
    }

    public void iniciamosDetallePorDefecto() {
        detailCtrl = Robolectric.buildActivity(DetailActivity.class);
        detailCtrl.create().resume().visible();
    }

    public void iniciamosFavoritosComoDemo() {
        loginAsDemo();
        favoritesCtrl = Robolectric.buildActivity(FavoritesActivity.class);
        favoritesCtrl.create().resume().visible();
    }

    public void iniciamosFavoritosComoInvitado() {
        AppPreferences.enableGuestMode(context);
        favoritesCtrl = Robolectric.buildActivity(FavoritesActivity.class);
        favoritesCtrl.create().resume().visible();
    }

    public void iniciamosAddInvestmentComoDemo() {
        loginAsDemo();
        addCtrl = Robolectric.buildActivity(AddInvestmentActivity.class);
        addCtrl.create().resume().visible();
    }

    public void iniciamosManageInvestmentComoDemo(String assetId) {
        loginAsDemo();
        AppMediator.getInstance().setNextManageScreenState(new DetailToManageState(assetId));
        manageCtrl = Robolectric.buildActivity(ManageInvestmentActivity.class);
        manageCtrl.create().resume().visible();
    }

    public void iniciamosManageInvestmentComoInvitado(String assetId) {
        AppPreferences.enableGuestMode(context);
        AppMediator.getInstance().setNextManageScreenState(new DetailToManageState(assetId));
        manageCtrl = Robolectric.buildActivity(ManageInvestmentActivity.class);
        manageCtrl.create().resume().visible();
    }

    public void iniciamosPerfilComoDemo() {
        loginAsDemo();
        profileCtrl = Robolectric.buildActivity(ProfileActivity.class);
        profileCtrl.create().resume().visible();
    }

    public void iniciamosPerfilComoInvitado() {
        AppPreferences.enableGuestMode(context);
        profileCtrl = Robolectric.buildActivity(ProfileActivity.class);
        profileCtrl.create().resume().visible();
    }

    public void iniciamosEditarPerfilComoDemo() {
        loginAsDemo();
        editProfileCtrl = Robolectric.buildActivity(EditProfileActivity.class);
        editProfileCtrl.create().resume().visible();
    }

    public void iniciamosEditarPerfilComoInvitado() {
        AppPreferences.enableGuestMode(context);
        editProfileCtrl = Robolectric.buildActivity(EditProfileActivity.class);
        editProfileCtrl.create().resume().visible();
    }

    public void rotamosHome() {
        homeCtrl = rotate(homeCtrl, HomeActivity.class);
    }

    public void rotamosDetalle() {
        detailCtrl = rotate(detailCtrl, DetailActivity.class);
    }

    public void rotamosLogin() {
        loginCtrl = rotate(loginCtrl, LoginActivity.class);
    }

    public void rotamosRegistro() {
        registerCtrl = rotate(registerCtrl, RegisterActivity.class);
    }

    public void rotamosForgotPassword() {
        forgotCtrl = rotate(forgotCtrl, ForgotPasswordActivity.class);
    }

    public void rotamosFavoritos() {
        favoritesCtrl = rotate(favoritesCtrl, FavoritesActivity.class);
    }

    public void rotamosAddInvestment() {
        addCtrl = rotate(addCtrl, AddInvestmentActivity.class);
    }

    public void rotamosManageInvestment() {
        manageCtrl = rotate(manageCtrl, ManageInvestmentActivity.class);
    }

    public void rotamosPerfil() {
        profileCtrl = rotate(profileCtrl, ProfileActivity.class);
    }

    public void rotamosEditarPerfil() {
        editProfileCtrl = rotate(editProfileCtrl, EditProfileActivity.class);
    }

    public void escribimosLogin(String email, String password) {
        setText(loginCtrl.get(), R.id.login_email_input, email);
        setText(loginCtrl.get(), R.id.login_password_input, password);
    }

    public void pulsamosLogin() {
        loginCtrl.get().findViewById(R.id.login_button).performClick();
    }

    public void pulsamosSignupDesdeLogin() {
        loginCtrl.get().findViewById(R.id.login_signup_button).performClick();
    }

    public void pulsamosForgotDesdeLogin() {
        loginCtrl.get().findViewById(R.id.login_forgot_password).performClick();
    }

    public void pulsamosGuestDesdeLogin() {
        loginCtrl.get().findViewById(R.id.login_guest_button).performClick();
    }

    public void escribimosRegistro(String name, String email, String password) {
        setText(registerCtrl.get(), R.id.input_register_name, name);
        setText(registerCtrl.get(), R.id.input_register_email, email);
        setText(registerCtrl.get(), R.id.input_register_password, password);
    }

    public void pulsamosCrearCuenta() {
        registerCtrl.get().findViewById(R.id.btn_register_submit).performClick();
    }

    public void escribimosForgot(String email, String password, String confirmPassword) {
        setText(forgotCtrl.get(), R.id.input_forgot_email, email);
        setText(forgotCtrl.get(), R.id.input_forgot_password, password);
        setText(forgotCtrl.get(), R.id.input_forgot_confirm_password, confirmPassword);
    }

    public void pulsamosResetPassword() {
        forgotCtrl.get().findViewById(R.id.btn_forgot_submit).performClick();
    }

    public void buscamosEnHome(String query) {
        setText(homeCtrl.get(), R.id.input_home_search, query);
    }

    public void pulsamosFiltroStock() {
        homeCtrl.get().findViewById(R.id.filter_home_stock).performClick();
    }

    public void pulsamosFiltroCrypto() {
        homeCtrl.get().findViewById(R.id.filter_home_crypto).performClick();
    }

    public void pulsamosAddInvestmentDesdeHome() {
        homeCtrl.get().findViewById(R.id.btn_home_add_investment).performClick();
    }

    public void seleccionamosAddInvestmentEnPosicion(int position) {
        selectLatestDialogItem(position);
    }

    public void escribimosCantidadCatalogo(String quantity) {
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        assertThat(dialog, notNullValue());

        EditText input = dialog.findViewById(R.id.input_add_catalog_quantity);
        assertThat(input, notNullValue());
        input.setText(quantity);
    }

    public void confirmamosCantidadCatalogo() {
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        assertThat(dialog, notNullValue());
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        shadowOf(Looper.getMainLooper()).idle();
    }

    public void pulsamosRemoveInvestmentDesdeHome() {
        homeCtrl.get().findViewById(R.id.btn_home_remove_investment).performClick();
    }

    public void seleccionamosRemoveInvestmentEnPosicion(int position) {
        selectLatestDialogItem(position);
    }

    private void selectLatestDialogItem(int position) {
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        assertThat(dialog, notNullValue());

        ListView listView = dialog.getListView();
        ListAdapter adapter = listView.getAdapter();
        View itemView = adapter.getView(position, null, listView);
        listView.performItemClick(itemView, position, adapter.getItemId(position));
    }

    public void comprobamosRemoveInvestmentDeshabilitado() {
        View button = homeCtrl.get().findViewById(R.id.btn_home_remove_investment);
        assertThat(button.isEnabled(), equalTo(false));
    }

    public void comprobamosRemoveInvestmentHabilitado() {
        View button = homeCtrl.get().findViewById(R.id.btn_home_remove_investment);
        assertThat(button.isEnabled(), equalTo(true));
    }

    public void pulsamosAssetHomeEnPosicion(int position) {
        LinearLayout container = homeCtrl.get().findViewById(R.id.container_home_assets);
        container.getChildAt(position).performClick();
    }

    public void escribimosNuevaInversion(
            String name,
            String ticker,
            String type,
            String currentPrice,
            String quantity,
            String averagePrice
    ) {
        setText(addCtrl.get(), R.id.input_add_name, name);
        setText(addCtrl.get(), R.id.input_add_ticker, ticker);
        setText(addCtrl.get(), R.id.input_add_current_price, currentPrice);
        setText(addCtrl.get(), R.id.input_add_quantity, quantity);
        setText(addCtrl.get(), R.id.input_add_average_price, averagePrice);

        RadioGroup typeGroup = addCtrl.get().findViewById(R.id.group_add_type);
        typeGroup.check("Crypto".equalsIgnoreCase(type)
                ? R.id.option_add_crypto
                : R.id.option_add_stock);
    }

    public void pulsamosGuardarNuevaInversion() {
        addCtrl.get().findViewById(R.id.btn_add_investment).performClick();
    }

    public void pulsamosFavoritoEnDetalle() {
        detailCtrl.get().findViewById(R.id.btn_detail_favorite).performClick();
    }

    public void pulsamosManageEnDetalle() {
        detailCtrl.get().findViewById(R.id.btn_manage_investment).performClick();
    }

    public void cambiamosPrecioEnManage(String price) {
        setText(manageCtrl.get(), R.id.input_manage_price, price);
    }

    public void cambiamosCantidadEnManage(String quantity) {
        setText(manageCtrl.get(), R.id.input_manage_quantity, quantity);
    }

    public void pulsamosActualizarPrecio() {
        manageCtrl.get().findViewById(R.id.btn_update_investment).performClick();
    }

    public void pulsamosDarkModeEnPerfil() {
        SwitchCompat switchCompat = profileCtrl.get().findViewById(R.id.switch_profile_dark_mode);
        switchCompat.performClick();
    }

    public void pulsamosEditarPerfilDesdePerfil() {
        profileCtrl.get().findViewById(R.id.btn_profile_edit).performClick();
    }

    public void pulsamosCambiarFotoDesdePerfil() {
        profileCtrl.get().findViewById(R.id.btn_profile_change_photo).performClick();
    }

    public void escribimosEditarPerfil(String username, String phone, String email) {
        setText(editProfileCtrl.get(), R.id.input_edit_profile_username, username);
        setText(editProfileCtrl.get(), R.id.input_edit_profile_phone, phone);
        setText(editProfileCtrl.get(), R.id.input_edit_profile_email, email);
    }

    public void pulsamosGuardarPerfil() {
        editProfileCtrl.get().findViewById(R.id.btn_edit_profile_save).performClick();
    }

    public void comprobamosToast(String expectedText) {
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(expectedText));
    }

    public void comprobamosToastContiene(String expectedText) {
        assertThat(ShadowToast.getTextOfLatestToast(), containsString(expectedText));
    }

    public void comprobamosLoginAbreHome() {
        assertStartedActivity(loginCtrl.get(), HomeActivity.class);
    }

    public void comprobamosLoginAbreRegistro() {
        assertStartedActivity(loginCtrl.get(), RegisterActivity.class);
    }

    public void comprobamosLoginAbreForgot() {
        assertStartedActivity(loginCtrl.get(), ForgotPasswordActivity.class);
    }

    public void comprobamosHomeAbreAddInvestment() {
        assertStartedActivity(homeCtrl.get(), AddInvestmentActivity.class);
    }

    public void comprobamosHomeAbreDetalle() {
        assertStartedActivity(homeCtrl.get(), DetailActivity.class);
    }

    public void comprobamosDetalleAbreManage() {
        assertStartedActivity(detailCtrl.get(), ManageInvestmentActivity.class);
    }

    public void comprobamosRegistroFinaliza() {
        assertTrue(registerCtrl.get().isFinishing());
    }

    public void comprobamosForgotFinaliza() {
        assertTrue(forgotCtrl.get().isFinishing());
    }

    public void comprobamosAddFinaliza() {
        assertTrue(addCtrl.get().isFinishing());
    }

    public void comprobamosManageFinaliza() {
        assertTrue(manageCtrl.get().isFinishing());
    }

    public void comprobamosFavoritesFinaliza() {
        assertTrue(favoritesCtrl.get().isFinishing());
    }

    public void comprobamosEditProfileFinaliza() {
        assertTrue(editProfileCtrl.get().isFinishing());
    }

    public void comprobamosPerfilNoAbreEditarPerfil() {
        Intent intent = shadowOf(profileCtrl.get()).getNextStartedActivity();
        assertThat(intent, equalTo(null));
    }

    public void comprobamosHomeTieneInversiones(int expectedCount) {
        assertThat(assetCount(), equalTo(expectedCount));
    }

    public void comprobamosHomeTieneAlMenosInversiones(int expectedMinimum) {
        assertTrue(assetCount() >= expectedMinimum);
    }

    public void comprobamosHomeTieneFavoritos(int expectedCount) {
        LinearLayout container = homeCtrl.get().findViewById(R.id.container_home_favorites);
        assertThat(container.getChildCount(), equalTo(expectedCount));
    }

    public void comprobamosHomePrimeraInversion(String expectedName) {
        assertThat(homeAssetName(0), equalTo(expectedName));
    }

    public void comprobamosHomePrimeraInversionCantidad(String expectedQuantity) {
        assertThat(homeAssetQuantity(0), equalTo(expectedQuantity));
    }

    public void comprobamosDetalleNombre(String expectedName) {
        TextView textView = detailCtrl.get().findViewById(R.id.tv_detail_asset_name);
        assertThat(textView.getText().toString(), equalTo(expectedName));
    }

    public void comprobamosDetalleFavorito(String expectedStatus) {
        TextView textView = detailCtrl.get().findViewById(R.id.tv_detail_favorite_status);
        assertThat(textView.getText().toString(), equalTo(expectedStatus));
    }

    public void comprobamosFavoritosTieneInversiones(int expectedCount) {
        LinearLayout container = favoritesCtrl.get().findViewById(R.id.container_favorites_assets);
        assertThat(container.getChildCount(), equalTo(expectedCount));
    }

    public void comprobamosPrimerFavorito(String expectedName) {
        LinearLayout container = favoritesCtrl.get().findViewById(R.id.container_favorites_assets);
        TextView name = container.getChildAt(0).findViewById(R.id.tv_favorite_name);
        assertThat(name.getText().toString(), equalTo(expectedName));
    }

    public void comprobamosPreviewManage(String value, String profit, String returnPercent) {
        TextView valueText = manageCtrl.get().findViewById(R.id.tv_manage_preview_value);
        TextView profitText = manageCtrl.get().findViewById(R.id.tv_manage_preview_profit);
        TextView returnText = manageCtrl.get().findViewById(R.id.tv_manage_preview_return);
        assertThat(valueText.getText().toString(), equalTo(value));
        assertThat(profitText.getText().toString(), equalTo(profit));
        assertThat(returnText.getText().toString(), equalTo(returnPercent));
    }

    public void comprobamosPrecioActualizado(String assetId, double expectedPrice) {
        Asset asset = repository().getAssetById(AppPreferences.getActiveUserId(context), assetId);
        assertThat(asset, notNullValue());
        assertThat(asset.getCurrentPrice(), equalTo(expectedPrice));
    }

    public void comprobamosCantidadActualizada(String assetId, double expectedQuantity) {
        Asset asset = repository().getAssetById(AppPreferences.getActiveUserId(context), assetId);
        assertThat(asset, notNullValue());
        assertThat(asset.getQuantity(), equalTo(expectedQuantity));
    }

    public void comprobamosPerfil(String username, String email, String phone) {
        TextView usernameText = profileCtrl.get().findViewById(R.id.tv_profile_name);
        TextView emailText = profileCtrl.get().findViewById(R.id.tv_profile_email);
        TextView phoneText = profileCtrl.get().findViewById(R.id.tv_profile_phone);
        assertThat(usernameText.getText().toString(), equalTo(username));
        assertThat(emailText.getText().toString(), equalTo(email));
        assertThat(phoneText.getText().toString(), equalTo(phone));
    }

    public void comprobamosDarkModeActivado() {
        assertTrue(AppPreferences.isDarkModeEnabled(context));
    }

    public void comprobamosUsuarioRegistradoExiste(String email) {
        assertTrue(repository().userExistsByEmail(email));
    }

    public void comprobamosLoginConCredenciales(String email, String password) {
        assertThat(repository().authenticateUser(email, password), notNullValue());
    }

    public void comprobamosLoginFormulario(String email, String password) {
        assertEditText(loginCtrl.get(), R.id.login_email_input, email);
        assertEditText(loginCtrl.get(), R.id.login_password_input, password);
    }

    public void comprobamosRegistroFormulario(String name, String email, String password) {
        assertEditText(registerCtrl.get(), R.id.input_register_name, name);
        assertEditText(registerCtrl.get(), R.id.input_register_email, email);
        assertEditText(registerCtrl.get(), R.id.input_register_password, password);
    }

    public void comprobamosForgotFormulario(String email, String password, String confirmPassword) {
        assertEditText(forgotCtrl.get(), R.id.input_forgot_email, email);
        assertEditText(forgotCtrl.get(), R.id.input_forgot_password, password);
        assertEditText(forgotCtrl.get(), R.id.input_forgot_confirm_password, confirmPassword);
    }

    public void comprobamosAddFormulario(
            String name,
            String ticker,
            String currentPrice,
            String quantity,
            String averagePrice
    ) {
        assertEditText(addCtrl.get(), R.id.input_add_name, name);
        assertEditText(addCtrl.get(), R.id.input_add_ticker, ticker);
        assertEditText(addCtrl.get(), R.id.input_add_current_price, currentPrice);
        assertEditText(addCtrl.get(), R.id.input_add_quantity, quantity);
        assertEditText(addCtrl.get(), R.id.input_add_average_price, averagePrice);
    }

    public void comprobamosManagePrecioInput(String price) {
        assertEditText(manageCtrl.get(), R.id.input_manage_price, price);
    }

    public void comprobamosManageCantidadInput(String quantity) {
        assertEditText(manageCtrl.get(), R.id.input_manage_quantity, quantity);
    }

    public void comprobamosEditarPerfilFormulario(String username, String phone, String email) {
        assertEditText(editProfileCtrl.get(), R.id.input_edit_profile_username, username);
        assertEditText(editProfileCtrl.get(), R.id.input_edit_profile_phone, phone);
        assertEditText(editProfileCtrl.get(), R.id.input_edit_profile_email, email);
    }

    public void comprobamosPerfilGuardado(String username, String phone, String email) {
        assertThat(AppPreferences.getProfileUsername(context), equalTo(username));
        assertThat(AppPreferences.getProfilePhone(context), equalTo(phone));
        assertThat(AppPreferences.getProfileEmail(context), equalTo(email));
    }

    public void comprobamosInvitadoActivo() {
        assertTrue(AppPreferences.isGuestMode(context));
    }

    public void loginAsDemo() {
        AppPreferences.setLoggedIn(context, true, InvestTrackRepository.DEMO_USER_ID);
    }

    public void loginAsUser(String email, String password) {
        com.tuempresa.investtrack.data.local.UserEntity user = repository().authenticateUser(email, password);
        assertThat(user, notNullValue());
        AppPreferences.setLoggedIn(context, true, user.id);
    }

    private int assetCount() {
        LinearLayout container = homeCtrl.get().findViewById(R.id.container_home_assets);
        return container.getChildCount();
    }

    private String homeAssetName(int position) {
        LinearLayout container = homeCtrl.get().findViewById(R.id.container_home_assets);
        TextView name = container.getChildAt(position).findViewById(R.id.tv_investment_name);
        return name.getText().toString();
    }

    private String homeAssetQuantity(int position) {
        LinearLayout container = homeCtrl.get().findViewById(R.id.container_home_assets);
        TextView quantity = container.getChildAt(position).findViewById(R.id.tv_investment_quantity);
        return quantity.getText().toString();
    }

    private InvestTrackRepository repository() {
        return InvestTrackRepository.getInstance(context);
    }

    private void setText(Activity activity, int viewId, String text) {
        EditText input = activity.findViewById(viewId);
        input.setText(text);
    }

    private void assertEditText(Activity activity, int viewId, String expectedText) {
        EditText input = activity.findViewById(viewId);
        assertThat(input.getText().toString(), equalTo(expectedText));
    }

    private void assertStartedActivity(Activity activity, Class<?> expectedClass) {
        Intent intent = shadowOf(activity).getNextStartedActivity();
        assertThat(intent, notNullValue());
        assertThat(intent.getComponent().getClassName(), equalTo(expectedClass.getName()));
    }

    private <T extends Activity> ActivityController<T> rotate(
            ActivityController<T> controller,
            Class<T> activityClass
    ) {
        Bundle bundle = new Bundle();
        controller.saveInstanceState(bundle).pause().stop().destroy();
        return Robolectric.buildActivity(activityClass)
                .create(bundle)
                .restoreInstanceState(bundle)
                .resume()
                .visible();
    }

    private void destroyIfNeeded(ActivityController<? extends Activity> controller) {
        if (controller != null && controller.get() != null) {
            try {
                controller.pause().stop().destroy();
            } catch (RuntimeException ignored) {
                // Some tests intentionally finish an activity before teardown.
            }
        }
    }
}
