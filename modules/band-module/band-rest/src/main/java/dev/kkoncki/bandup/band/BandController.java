package dev.kkoncki.bandup.band;

import dev.kkoncki.bandup.band.forms.AddBandMemberForm;
import dev.kkoncki.bandup.band.forms.CreateBandForm;
import dev.kkoncki.bandup.band.forms.UpdateBandForm;
import dev.kkoncki.bandup.band.service.BandService;
import dev.kkoncki.bandup.commons.LoggedUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/band")
public class BandController {

    private final BandService bandService;
    private final LoggedUser loggedUser;

    public BandController(BandService bandService, LoggedUser loggedUser) {
        this.bandService = bandService;
        this.loggedUser = loggedUser;
    }

    @PostMapping
    public Band createBand(@RequestBody CreateBandForm form) {
        return bandService.createBand(form, loggedUser.getUserId());
    }

    @GetMapping("/{id}")
    public Band get(@PathVariable String id) {
        return bandService.get(id);
    }

    @PutMapping
    public Band updateBand(@RequestBody UpdateBandForm form) {
        return bandService.updateBand(form);
    }

    @DeleteMapping("/{id}")
    public void deleteBand(@PathVariable String id) {
        bandService.deleteBand(id);
    }

    @GetMapping
    public List<Band> getAllBands() {
        return bandService.getAllBands();
    }
}
