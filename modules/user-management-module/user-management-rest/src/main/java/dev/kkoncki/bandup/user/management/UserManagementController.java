package dev.kkoncki.bandup.user.management;

import dev.kkoncki.bandup.commons.LoggedUser;
import dev.kkoncki.bandup.user.management.forms.CreateUserForm;
import dev.kkoncki.bandup.user.management.forms.UpdateUserLocationForm;
import dev.kkoncki.bandup.user.management.service.UserManagementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-management")
public class UserManagementController {

    private final UserManagementService userManagementService;
    private final LoggedUser loggedUser;

    public UserManagementController(UserManagementService userManagementService, LoggedUser loggedUser) {
        this.userManagementService = userManagementService;
        this.loggedUser = loggedUser;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") String id) {
        return userManagementService.get(id);
    }

    @PostMapping("/save")
    public User save(@RequestBody CreateUserForm form) {
        return userManagementService.save(form);
    }

    @PutMapping("/block/{id}")
    public User block(@PathVariable("id") String id) {
        return userManagementService.block(id);
    }

    @PutMapping("/unblock/{id}")
    public User unBlock(@PathVariable("id") String id) {
        return userManagementService.unBlock(id);
    }

    @PostMapping("/add-or-remove-instrument/{userInstrumentId}")
    public void addOrRemoveInstrument(@PathVariable("userInstrumentId") String userInstrumentId) {
        userManagementService.addOrRemoveInstrument(userInstrumentId, loggedUser.getUserId());
    }

    @PostMapping("/add-or-remove-genre/{genreId}")
    public void addOrRemoveGenre(@PathVariable("genreId") String genreId) {
        userManagementService.addOrRemoveGenre(genreId, loggedUser.getUserId());
    }

    @PutMapping("/location")
    public void updateUserLocation(@RequestBody UpdateUserLocationForm form) {
        userManagementService.updateUserLocation(loggedUser.getUserId(), form);
    }
}
