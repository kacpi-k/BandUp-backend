package dev.kkoncki.bandup.user.management;

import dev.kkoncki.bandup.commons.LoggedUser;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.UserInstrument;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.forms.CreateUserInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.forms.UpdateUserInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.service.UserInstrumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-instrument")
public class UserInstrumentController {

    private final UserInstrumentService userInstrumentService;
    private final LoggedUser loggedUser;

    public UserInstrumentController(UserInstrumentService userInstrumentService, LoggedUser loggedUser) {
        this.userInstrumentService = userInstrumentService;
        this.loggedUser = loggedUser;
    }

    @GetMapping("{id}")
    public UserInstrument get(@PathVariable("id") String id) {
        return userInstrumentService.get(id);
    }

    @PostMapping("/save")
    public UserInstrument save(@RequestBody CreateUserInstrumentForm form) {
        return userInstrumentService.save(form, loggedUser.getUserId());
    }

    @PutMapping("/update")
    public void update(@RequestBody UpdateUserInstrumentForm form) {
        userInstrumentService.update(form);
    }

    @GetMapping("/all-by-user/{userId}")
    public List<UserInstrument> getAllByUserId(@PathVariable("userId") String userId) {
        return userInstrumentService.getAllByUserId(userId);
    }
}
