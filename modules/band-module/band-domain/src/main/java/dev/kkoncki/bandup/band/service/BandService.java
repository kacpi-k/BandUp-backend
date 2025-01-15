package dev.kkoncki.bandup.band.service;

import dev.kkoncki.bandup.band.Band;
import dev.kkoncki.bandup.band.forms.AddBandMemberForm;
import dev.kkoncki.bandup.band.forms.UpdateBandMemberRoleForm;
import dev.kkoncki.bandup.band.forms.CreateBandForm;
import dev.kkoncki.bandup.band.forms.UpdateBandForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface BandService {
    Band createBand(@Valid CreateBandForm form, String leaderId);
    Band get(String id);
    Band updateBand(@Valid UpdateBandForm form);
    void deleteBand(String id);
    List<Band> getAllBands();
}
