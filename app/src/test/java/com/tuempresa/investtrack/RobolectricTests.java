package com.tuempresa.investtrack;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = com.tuempresa.investtrack.core.app.InvestTrackApplication.class, sdk = 34)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RobolectricTests {

    private final RobolectricSteps steps = new RobolectricSteps();

    @Before
    public void setUp() {
        steps.resetApp();
    }

    @After
    public void tearDown() {
        steps.closeControllers();
    }

    @Test
    public void test01LoginInvalidoMuestraError() {
        steps.iniciamosLogin();
        steps.escribimosLogin("demo", "123");
        steps.pulsamosLogin();
        steps.comprobamosToast("Enter a valid email and a password with at least 6 characters.");
    }

    @Test
    public void test02LoginDemoAbreHome() {
        steps.iniciamosLogin();
        steps.escribimosLogin("demo@investtrack.app", "password");
        steps.pulsamosLogin();
        steps.comprobamosLoginAbreHome();
    }

    @Test
    public void test03LoginInvitadoAbreHomeYActivaInvitado() {
        steps.iniciamosLogin();
        steps.pulsamosGuestDesdeLogin();
        steps.comprobamosLoginAbreHome();
        steps.comprobamosInvitadoActivo();
    }

    @Test
    public void test04LoginAbreRegistro() {
        steps.iniciamosLogin();
        steps.pulsamosSignupDesdeLogin();
        steps.comprobamosLoginAbreRegistro();
    }

    @Test
    public void test05LoginAbreForgotPassword() {
        steps.iniciamosLogin();
        steps.pulsamosForgotDesdeLogin();
        steps.comprobamosLoginAbreForgot();
    }

    @Test
    public void test06RegistroValidaNombre() {
        steps.iniciamosRegistro();
        steps.escribimosRegistro("", "nuevo@investtrack.app", "password");
        steps.pulsamosCrearCuenta();
        steps.comprobamosToast("Enter a username.");
    }

    @Test
    public void test07RegistroDetectaEmailDuplicado() {
        steps.iniciamosRegistro();
        steps.escribimosRegistro("Demo", "demo@investtrack.app", "password");
        steps.pulsamosCrearCuenta();
        steps.comprobamosToast("There is already an account with that email.");
    }

    @Test
    public void test08RegistroCreaUsuario() {
        steps.iniciamosRegistro();
        steps.escribimosRegistro("Alicia", "alicia@investtrack.app", "password");
        steps.pulsamosCrearCuenta();
        steps.comprobamosUsuarioRegistradoExiste("alicia@investtrack.app");
        steps.comprobamosRegistroFinaliza();
    }

    @Test
    public void test08bUsuarioNuevoEmpiezaSinInversiones() {
        steps.iniciamosRegistro();
        steps.escribimosRegistro("Alicia", "alicia@investtrack.app", "password");
        steps.pulsamosCrearCuenta();
        steps.loginAsUser("alicia@investtrack.app", "password");
        steps.iniciamosHome();
        steps.comprobamosHomeTieneInversiones(0);
        steps.comprobamosHomeTieneFavoritos(0);
        steps.comprobamosRemoveInvestmentDeshabilitado();
    }

    @Test
    public void test09ForgotPasswordEmailNoExiste() {
        steps.iniciamosForgotPassword();
        steps.escribimosForgot("nadie@investtrack.app", "password", "password");
        steps.pulsamosResetPassword();
        steps.comprobamosToast("No account was found with that email.");
    }

    @Test
    public void test10ForgotPasswordValidaConfirmacion() {
        steps.iniciamosForgotPassword();
        steps.escribimosForgot("demo@investtrack.app", "password", "distinta");
        steps.pulsamosResetPassword();
        steps.comprobamosToast("Passwords do not match.");
    }

    @Test
    public void test11ForgotPasswordActualizaPassword() {
        steps.iniciamosForgotPassword();
        steps.escribimosForgot("demo@investtrack.app", "newpass", "newpass");
        steps.pulsamosResetPassword();
        steps.comprobamosForgotFinaliza();
        steps.comprobamosLoginConCredenciales("demo@investtrack.app", "newpass");
    }

    @Test
    public void test12HomeInvitadoCargaDemo() {
        steps.iniciamosHomeComoInvitado();
        steps.comprobamosHomeTieneInversiones(23);
        steps.comprobamosHomeTieneFavoritos(0);
        steps.comprobamosRemoveInvestmentDeshabilitado();
    }

    @Test
    public void test12bHomeDemoPermiteRemoveSiHayInversiones() {
        steps.iniciamosHomeComoDemo();
        steps.comprobamosHomeTieneInversiones(23);
        steps.comprobamosRemoveInvestmentHabilitado();
    }

    @Test
    public void test12cHomeRemoveEliminaInversion() {
        steps.iniciamosHomeComoDemo();
        steps.comprobamosHomeTieneInversiones(23);
        steps.pulsamosRemoveInvestmentDesdeHome();
        steps.seleccionamosRemoveInvestmentEnPosicion(0);
        steps.comprobamosToast("Investment removed.");
        steps.comprobamosHomeTieneInversiones(22);
    }

    @Test
    public void test12dHomeAddDesdeListaEnUsuarioNuevo() {
        steps.iniciamosRegistro();
        steps.escribimosRegistro("Alicia", "alicia@investtrack.app", "password");
        steps.pulsamosCrearCuenta();
        steps.loginAsUser("alicia@investtrack.app", "password");
        steps.iniciamosHome();
        steps.comprobamosHomeTieneInversiones(0);
        steps.pulsamosAddInvestmentDesdeHome();
        steps.seleccionamosAddInvestmentEnPosicion(0);
        steps.escribimosCantidadCatalogo("2");
        steps.confirmamosCantidadCatalogo();
        steps.comprobamosToast("Investment added.");
        steps.comprobamosHomeTieneInversiones(1);
        steps.comprobamosHomePrimeraInversion("Apple");
        steps.comprobamosHomePrimeraInversionCantidad("2 shares");
        steps.comprobamosRemoveInvestmentHabilitado();
    }

    @Test
    public void test13HomeFiltroStockMuestraEmpresas() {
        steps.iniciamosHomeComoDemo();
        steps.pulsamosFiltroStock();
        steps.comprobamosHomeTieneInversiones(20);
    }

    @Test
    public void test14HomeFiltroCryptoMuestraTresCriptos() {
        steps.iniciamosHomeComoDemo();
        steps.pulsamosFiltroCrypto();
        steps.comprobamosHomeTieneInversiones(3);
    }

    @Test
    public void test15HomeBusquedaTeslaMuestraUnResultado() {
        steps.iniciamosHomeComoDemo();
        steps.buscamosEnHome("Tesla");
        steps.comprobamosHomeTieneInversiones(1);
        steps.comprobamosHomePrimeraInversion("Tesla");
    }

    @Test
    public void test16HomeMantieneBusquedaTrasRotar() {
        steps.iniciamosHomeComoDemo();
        steps.buscamosEnHome("Tesla");
        steps.rotamosHome();
        steps.comprobamosHomeTieneInversiones(1);
    }

    @Test
    public void test17InvitadoNoPuedeAbrirAddInvestment() {
        steps.iniciamosHomeComoInvitado();
        steps.pulsamosAddInvestmentDesdeHome();
        steps.comprobamosToast("Guest mode is a fixed demo. Log in to edit investments.");
    }

    @Test
    public void test18UsuarioLogueadoAbreAddInvestment() {
        steps.iniciamosHomeComoDemo();
        steps.pulsamosAddInvestmentDesdeHome();
        steps.seleccionamosAddInvestmentEnPosicion(0);
        steps.comprobamosHomeAbreAddInvestment();
    }

    @Test
    public void test19AddInvestmentValidaTexto() {
        steps.iniciamosAddInvestmentComoDemo();
        steps.escribimosNuevaInversion("", "ABC", "Stock", "10", "2", "8");
        steps.pulsamosGuardarNuevaInversion();
        steps.comprobamosToast("Enter a name and ticker.");
    }

    @Test
    public void test20AddInvestmentCreaActivoIndependiente() {
        steps.iniciamosAddInvestmentComoDemo();
        steps.escribimosNuevaInversion("Acme Corp", "ACME", "Stock", "10", "2", "8");
        steps.pulsamosGuardarNuevaInversion();
        steps.comprobamosAddFinaliza();
        steps.iniciamosHomeComoDemo();
        steps.buscamosEnHome("ACME");
        steps.comprobamosHomeTieneInversiones(1);
        steps.comprobamosHomePrimeraInversion("Acme Corp");
    }

    @Test
    public void test21AddInvestmentDetectaDuplicado() {
        steps.iniciamosAddInvestmentComoDemo();
        steps.escribimosNuevaInversion("Tesla", "TSLA", "Stock", "250", "1", "200");
        steps.pulsamosGuardarNuevaInversion();
        steps.comprobamosToast("That ticker already exists.");
    }

    @Test
    public void test22HomeAssetAbreDetalle() {
        steps.iniciamosHomeComoDemo();
        steps.pulsamosAssetHomeEnPosicion(0);
        steps.comprobamosHomeAbreDetalle();
    }

    @Test
    public void test23DetallePorDefectoMuestraMicrosoft() {
        steps.loginAsDemo();
        steps.iniciamosDetallePorDefecto();
        steps.comprobamosDetalleNombre("Microsoft");
    }

    @Test
    public void test24DetalleMuestraTeslaYMantieneRotacion() {
        steps.loginAsDemo();
        steps.iniciamosDetalle("tsla");
        steps.comprobamosDetalleNombre("Tesla");
        steps.rotamosDetalle();
        steps.comprobamosDetalleNombre("Tesla");
    }

    @Test
    public void test25DetallePermiteMarcarFavorito() {
        steps.loginAsDemo();
        steps.iniciamosDetalle("tsla");
        steps.pulsamosFavoritoEnDetalle();
        steps.comprobamosDetalleFavorito("In favorites");
        steps.iniciamosFavoritosComoDemo();
        steps.comprobamosFavoritosTieneInversiones(1);
        steps.comprobamosPrimerFavorito("Tesla");
    }

    @Test
    public void test26FavoritosInvitadoNoDisponible() {
        steps.iniciamosFavoritosComoInvitado();
        steps.comprobamosToast("Log in to use favorites.");
        steps.comprobamosFavoritesFinaliza();
    }

    @Test
    public void test27DetalleAbreManageSiUsuarioLogueado() {
        steps.loginAsDemo();
        steps.iniciamosDetalle("tsla");
        steps.pulsamosManageEnDetalle();
        steps.comprobamosDetalleAbreManage();
    }

    @Test
    public void test28ManageInvitadoSoloLectura() {
        steps.iniciamosManageInvestmentComoInvitado("tsla");
        steps.comprobamosToast("Guest mode is a fixed demo. Log in to edit investments.");
        steps.comprobamosManageFinaliza();
    }

    @Test
    public void test29ManageActualizaPrecioYCantidad() {
        steps.iniciamosManageInvestmentComoDemo("tsla");
        steps.cambiamosPrecioEnManage("300");
        steps.cambiamosCantidadEnManage("4");
        steps.pulsamosActualizarPrecio();
        steps.comprobamosManageFinaliza();
        steps.comprobamosPrecioActualizado("tsla", 300.0);
        steps.comprobamosCantidadActualizada("tsla", 4.0);
    }

    @Test
    public void test30ProfileYEditProfileFuncionan() {
        steps.iniciamosPerfilComoDemo();
        steps.comprobamosPerfil("Demo investor", "demo@investtrack.app", "+34 600 000 000");
        steps.pulsamosDarkModeEnPerfil();
        steps.comprobamosDarkModeActivado();

        steps.iniciamosEditarPerfilComoDemo();
        steps.escribimosEditarPerfil("Vera", "+34 611 222 333", "vera@investtrack.app");
        steps.pulsamosGuardarPerfil();
        steps.comprobamosPerfilGuardado("Vera", "+34 611 222 333", "vera@investtrack.app");
    }

    @Test
    public void test31LoginMantieneFormularioTrasRotar() {
        steps.iniciamosLogin();
        steps.escribimosLogin("demo@investtrack.app", "password");
        steps.rotamosLogin();
        steps.comprobamosLoginFormulario("demo@investtrack.app", "password");
        steps.pulsamosLogin();
        steps.comprobamosLoginAbreHome();
    }

    @Test
    public void test32RegistroMantieneFormularioTrasRotarYCreaUsuario() {
        steps.iniciamosRegistro();
        steps.escribimosRegistro("Rotado", "rotado@investtrack.app", "password");
        steps.rotamosRegistro();
        steps.comprobamosRegistroFormulario("Rotado", "rotado@investtrack.app", "password");
        steps.pulsamosCrearCuenta();
        steps.comprobamosUsuarioRegistradoExiste("rotado@investtrack.app");
    }

    @Test
    public void test33ForgotMantieneFormularioTrasRotarYActualizaPassword() {
        steps.iniciamosForgotPassword();
        steps.escribimosForgot("demo@investtrack.app", "rotado1", "rotado1");
        steps.rotamosForgotPassword();
        steps.comprobamosForgotFormulario("demo@investtrack.app", "rotado1", "rotado1");
        steps.pulsamosResetPassword();
        steps.comprobamosLoginConCredenciales("demo@investtrack.app", "rotado1");
    }

    @Test
    public void test34AddInvestmentMantieneFormularioTrasRotarYGuarda() {
        steps.iniciamosAddInvestmentComoDemo();
        steps.escribimosNuevaInversion("Rotate Corp", "ROTA", "Stock", "12", "3", "10");
        steps.rotamosAddInvestment();
        steps.comprobamosAddFormulario("Rotate Corp", "ROTA", "12", "3", "10");
        steps.pulsamosGuardarNuevaInversion();
        steps.iniciamosHomeComoDemo();
        steps.buscamosEnHome("ROTA");
        steps.comprobamosHomeTieneInversiones(1);
        steps.comprobamosHomePrimeraInversion("Rotate Corp");
    }

    @Test
    public void test35ManageMantienePrecioYCantidadTrasRotarYActualiza() {
        steps.iniciamosManageInvestmentComoDemo("tsla");
        steps.cambiamosPrecioEnManage("300");
        steps.cambiamosCantidadEnManage("4");
        steps.rotamosManageInvestment();
        steps.comprobamosManagePrecioInput("300");
        steps.comprobamosManageCantidadInput("4");
        steps.pulsamosActualizarPrecio();
        steps.comprobamosPrecioActualizado("tsla", 300.0);
        steps.comprobamosCantidadActualizada("tsla", 4.0);
    }

    @Test
    public void test36FavoritoPersisteAlRotarDetalleYFavoritos() {
        steps.loginAsDemo();
        steps.iniciamosDetalle("tsla");
        steps.pulsamosFavoritoEnDetalle();
        steps.rotamosDetalle();
        steps.comprobamosDetalleFavorito("In favorites");
        steps.iniciamosFavoritosComoDemo();
        steps.rotamosFavoritos();
        steps.comprobamosFavoritosTieneInversiones(1);
        steps.comprobamosPrimerFavorito("Tesla");
    }

    @Test
    public void test37PerfilMantieneDatosTrasRotar() {
        steps.iniciamosPerfilComoDemo();
        steps.rotamosPerfil();
        steps.comprobamosPerfil("Demo investor", "demo@investtrack.app", "+34 600 000 000");
    }

    @Test
    public void test38EditProfileMantieneFormularioTrasRotarYGuarda() {
        steps.iniciamosEditarPerfilComoDemo();
        steps.escribimosEditarPerfil("Rotated User", "+34 699 888 777", "rotated@investtrack.app");
        steps.rotamosEditarPerfil();
        steps.comprobamosEditarPerfilFormulario(
                "Rotated User",
                "+34 699 888 777",
                "rotated@investtrack.app"
        );
        steps.pulsamosGuardarPerfil();
        steps.comprobamosPerfilGuardado("Rotated User", "+34 699 888 777", "rotated@investtrack.app");
    }

    @Test
    public void test39HomeFiltroCryptoPersisteTrasRotar() {
        steps.iniciamosHomeComoDemo();
        steps.pulsamosFiltroCrypto();
        steps.rotamosHome();
        steps.comprobamosHomeTieneInversiones(3);
    }

    @Test
    public void test40DetalleFavoritoNoDisponibleParaInvitadoTrasRotar() {
        steps.iniciamosHomeComoInvitado();
        steps.iniciamosDetalle("tsla");
        steps.rotamosDetalle();
        steps.pulsamosFavoritoEnDetalle();
        steps.comprobamosToast("Log in to use favorites.");
    }

    @Test
    public void test41InvitadoNoPuedeModificarPerfil() {
        steps.iniciamosPerfilComoInvitado();
        steps.pulsamosEditarPerfilDesdePerfil();
        steps.comprobamosToast("Guest mode is read-only. Log in to edit your profile.");
        steps.comprobamosPerfilNoAbreEditarPerfil();

        steps.pulsamosCambiarFotoDesdePerfil();
        steps.comprobamosToast("Guest mode is read-only. Log in to edit your profile.");

        steps.iniciamosEditarPerfilComoInvitado();
        steps.comprobamosToast("Guest mode is read-only. Log in to edit your profile.");
        steps.comprobamosEditProfileFinaliza();
        steps.comprobamosPerfilGuardado("Guest investor", "+34 600 000 000", "guest@investtrack.app");
    }
}
