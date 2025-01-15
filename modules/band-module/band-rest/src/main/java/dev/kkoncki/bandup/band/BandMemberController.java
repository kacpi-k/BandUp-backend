package dev.kkoncki.bandup.band;

import dev.kkoncki.bandup.band.forms.AddBandMemberForm;
import dev.kkoncki.bandup.band.forms.UpdateBandMemberRoleForm;
import dev.kkoncki.bandup.band.service.BandMemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/band-member")
public class BandMemberController {

    private final BandMemberService bandMemberService;

    public BandMemberController(BandMemberService bandMemberService) {
        this.bandMemberService = bandMemberService;
    }

    @GetMapping("/{id}")
    public BandMember get(@PathVariable String id) {
        return bandMemberService.get(id);
    }

    @GetMapping("/band/{bandId}")
    public List<BandMember> getAllMembersByBandId(@PathVariable String bandId) {
        return bandMemberService.getAllMembersByBandId(bandId);
    }

    @PostMapping
    public BandMember addMember(@RequestBody AddBandMemberForm form) {
        return bandMemberService.addMember(form);
    }

    @PutMapping("/role")
    public void updateMemberRole(@RequestBody UpdateBandMemberRoleForm form) {
        bandMemberService.updateMemberRole(form);
    }

    @DeleteMapping("/{id}")
    public void removeMember(@PathVariable String id) {
        bandMemberService.removeMember(id);
    }
}
