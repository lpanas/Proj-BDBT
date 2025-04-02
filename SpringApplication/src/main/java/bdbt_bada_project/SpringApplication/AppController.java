package bdbt_bada_project.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class AppController implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/main_admin").setViewName("admin/main_admin");
        registry.addViewController("/main_user").setViewName("user/main_user");
        registry.addViewController("/pojazdy_wglad").setViewName("pojazdy/pojazdy");
        registry.addViewController("/pojazdy_dodawanie").setViewName("pojazdy/dodawanie_pojazdu");
        registry.addViewController("/pojazdy_edytowanie").setViewName("pojazdy/edytowanie_pojazdu");
        registry.addViewController("/klienci_wglad").setViewName("klienci/wszyscy_klienci");
        registry.addViewController("/klienci_dodawanie").setViewName("klienci/rejestracja");
        registry.addViewController("/klienci_edytowanie").setViewName("klienci/edytowanie_klientow");
    }

    @Controller
    public class DashboardController {

        @Autowired
        private PojazdyDAO dao;
        @Autowired
        private KlientDAO daok;
        @Autowired
        private zamowieniaDAO daoz;


        @ExceptionHandler(IllegalStateException.class)
        public String handleIllegalStateException(IllegalStateException ex, Model model) {
            model.addAttribute("error", ex.getMessage());
            return "errors/tosamozamowienie"; // Nazwa widoku błędu
        }
        @RequestMapping("/deleteK/{id}")
        public String deleteClient(@PathVariable("id") int id, Model model) {
            try {
                // Sprawdzenie, czy klient ma powiązane zamówienia
                if (daok.isClientAssociatedWithOrders(id)) {
                    // Jeśli klient ma powiązane zamówienia, wyświetl widok błędu
                    model.addAttribute("errorMessage", "Nie można usunąć klienta, ponieważ ma powiązane zamówienia.");
                    return "errors/powiazanezamowieniaK"; // Widok błędu
                }

                // Jeśli nie ma powiązanych zamówień, wykonaj usunięcie
                daok.delete(id);
                return "redirect:/klient_wszyscy"; // Przekierowanie po udanym usunięciu
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Wystąpił błąd przy usuwaniu klienta.");
                return "errors/other"; // Ogólny widok błędu
            }
        }
        @RequestMapping("/delete/{id}")
        public String deleteCar(@PathVariable("id") int id, Model model) {
            try {
                // Sprawdzenie, czy klient ma powiązane zamówienia
                if (dao.isCarAssociatedWithOrders(id)) {
                    model.addAttribute("errorMessage", "Nie można usunąć pojazdu, ponieważ ma powiązane zamówienia.");
                    return "errors/powiazanezamowieniaP"; // Widok błędu
                }

                // Jeśli nie ma powiązanych zamówień, wykonaj usunięcie
                dao.delete(id);
                return "redirect:/pojazdy"; // Przekierowanie po udanym usunięciu
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Wystąpił błąd przy usuwaniu klienta.");
                return "errors/other"; // Ogólny widok błędu
            }
        }
        @GetMapping("/access-denied")
        public String accessDenied() {
            return "errors/accessDenied";
        }

        @RequestMapping("/zamowU/{nrPojazdu}")
        public String zamowPojazd(@PathVariable("nrPojazdu") int nrPojazdu) {
            String loggedInUsername = getLoggedInUsername();
            klienci klient = daok.getByLogin(loggedInUsername);

            if (daoz.isPojazdZamowionyByKlient(nrPojazdu, klient.getNrklienta())) {
                return "redirect:/bladZamowienia";
            }

            Pojazd pojazd = dao.get(nrPojazdu);
            if (pojazd == null) {
                return "redirect:/bladPojazdu";
            }

            zamowienia zamowienie = new zamowienia();
            zamowienie.setNrpojazdu(pojazd.getNr_Pojazdu());
            zamowienie.setNrklienta(klient.getNrklienta());
            zamowienie.setDatazamowienia(LocalDate.now());
            zamowienie.setStatus("oczekujące");

            daoz.save(zamowienie);
            return "redirect:/zamowienia_wszystkieU";
        }

        @RequestMapping("/bladZamowienia")
        public String bladZamowienia() {
            return "errors/bladzamowienia";
        }

        @RequestMapping("/bladPojazdu")
        public String bladPojazdu() {
            return "errors/bladpojazdu";
        }

        private String getLoggedInUsername() {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getUsername();
        }

        @RequestMapping("/zamowienia_wszystkieU")
        public String wszystkieZamowieniaU(Model model) {
            List<zamowienia> listzamowienia = daoz.listU();
            model.addAttribute("listzamowienia", listzamowienia);
            return "user/mojezamowienia";
        }

        @RequestMapping("/deleteZU/{id}")
        public String deleteZamowienie(@PathVariable("id") int id) {
            daoz.delete(id);
            return "redirect:/zamowienia_wszystkieU";
        }

        @RequestMapping("/pojazdyU")
        public String wszytskiePojazdyU(Model model) {
            List<Pojazd> listPojazd = dao.list();
            model.addAttribute("listPojazd", listPojazd);
            return "user/oferta_pojazdow";
        }

        @RequestMapping("/klient_wszyscyU/{login}")
        public String daneOMnie(@PathVariable(name = "login") String login, Model model) {
            klienci klient = daok.getByLogin(login);
            if (klient == null) {
                return "redirect:/bladKlienta";
            }
            model.addAttribute("klient", klient);
            return "user/daneom";
        }

        @RequestMapping("/editU/{nrklienta}")
        public String edytujDane(@PathVariable("nrklienta") int nrklienta, Model model) {
            klienci klient = daok.get(nrklienta);
            if (klient == null) {
                return "redirect:/bladKlienta";
            }
            model.addAttribute("klient", klient);
            return "user/edytowanie_danych_o_mnie";
        }

        @RequestMapping(value = "/updateU", method = RequestMethod.POST)
        public String zaaktualizujDaneOMnie(@ModelAttribute("klient") klienci klient) {
            daok.update(klient);
            return "redirect:/klient_wszyscyU/" + klient.getLogin();
        }

        @RequestMapping("/zamowienia_wszystkie")
        public String wszystkieZamowienia(Model model) {
            List<zamowienia> listzamowienia = daoz.list();
            model.addAttribute("listzamowienia", listzamowienia);
            return "zamowienia/wszystkie_zamowienia";
        }

        @GetMapping("/akceptZ/{nrzamowienia}")
        public String akceptujZamowienie(@PathVariable("nrzamowienia") int nrzamowienia, Model model) {
            try {
                daoz.akceptujZamowienie(nrzamowienia);
                return "redirect:/zamowienia_wszystkie";
            } catch (IllegalStateException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "errors/bladzamowienia";
            }
        }



        @GetMapping("/odrzutZ/{nrzamowienia}")
        public String odrzucZamowienie(@PathVariable("nrzamowienia") int nrzamowienia) {
            daoz.odrzucZamowienie(nrzamowienia);
            return "redirect:/zamowienia_wszystkie";
        }

        @RequestMapping("/rej")
        public String showRegistrationForm(Model model) {
            klienci klient = new klienci();
            model.addAttribute("klient", klient);
            return "klienci/rejestracja";
        }

        @RequestMapping("/klient_wszyscy")
        public String wszyscyKlienci(Model model) {
            List<klienci> listklienci = daok.list();
            model.addAttribute("listklienci", listklienci);
            return "klienci/wszyscy_klienci";
        }

        @RequestMapping(value = "/updateK", method = RequestMethod.POST)
        public String zaaktualizujKlienta(@ModelAttribute("klient") klienci klient) {
            daok.update(klient);
            return "redirect:/klient_wszyscy";
        }

        @RequestMapping(value = "/saveK", method = RequestMethod.POST)
        public String zapiszKlienta(@ModelAttribute("klient") klienci klient) {
            if (daok.isLoginTaken(klient.getLogin())) {
                return "errors/zajetylogin"; // Widok błędu dla zajętego loginu
            }

            if (daok.isIdTaken(klient.getNrklienta())) {
                return "errors/zajetyid"; // Widok błędu dla zajętego numeru ID
            }

            daok.save(klient);
            return "redirect:/index";
        }

        /*@RequestMapping("/deleteK/{id}")
        public String usunKlienta(@PathVariable(name = "id") int id) {
            daok.delete(id);
            return "redirect:/klient_wszyscy";
        }
*/
        @GetMapping("/editK/{nrklienta}")
        public String updateClient(@PathVariable int nrklienta, Model model) {
            klienci klient = daok.get(nrklienta);
            if (klient == null) {
                return "redirect:/bladKlienta";
            }
            model.addAttribute("klient", klient);
            return "klienci/edytowanie_klientow";
        }

        @RequestMapping("/pojazdy")
        public String viewHomePage(Model model) {
            List<Pojazd> listPojazd = dao.list();
            model.addAttribute("listPojazd", listPojazd);
            return "pojazdy/pojazdy";
        }

        @RequestMapping("/new")
        public String showNewForm(Model model) {
            Pojazd pojazd = new Pojazd();
            model.addAttribute("pojazd", pojazd);
            return "pojazdy/dodawanie_pojazdu";
        }

        @RequestMapping(value = "/save", method = RequestMethod.POST)
        public String save(@ModelAttribute("pojazd") Pojazd pojazd) {
            dao.save(pojazd);
            return "redirect:/pojazdy";
        }

        @GetMapping("/edit/{nrPojazdu}")
        public String showEditForm(@PathVariable int nrPojazdu, Model model) {
            Pojazd pojazd = dao.get(nrPojazdu);
            if (pojazd == null) {
                return "redirect:/bladPojazdu";
            }
            model.addAttribute("pojazd", pojazd);
            return "pojazdy/edytowanie_pojazdu";
        }

        @RequestMapping(value = "/update", method = RequestMethod.POST)
        public String update(@ModelAttribute("pojazd") Pojazd pojazd) {
            dao.update(pojazd);
            return "redirect:/pojazdy";
        }

        /*@RequestMapping("/delete/{id}")
        public String delete(@PathVariable(name = "id") int id) {
            dao.delete(id);
            return "redirect:/pojazdy";
        }
*/
        @RequestMapping("/main")
        public String defaultAfterLogin(HttpServletRequest request) {
            if (request.isUserInRole("ADMIN")) {
                return "redirect:/main_admin";
            } else if (request.isUserInRole("USER")) {
                return "redirect:/main_user";
            } else {
                return "redirect:/index";
            }
        }

        @GetMapping("/o-nas")
        public String oNas(Model model) {
            return "o_nas";
        }

        @GetMapping("/on_as")
        public String onAs(Model model) {
            return "o_nas";
        }

        @RequestMapping(value = {"/main_admin"})
        public String showAdminPage(Model model) {
            return "admin/main_admin";
        }

        @RequestMapping(value = {"/main_user"})
        public String showUserPage(Model model) {
            return "user/main_user";
        }

    }
}
