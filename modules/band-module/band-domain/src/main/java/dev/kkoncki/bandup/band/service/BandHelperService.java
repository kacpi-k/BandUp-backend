package dev.kkoncki.bandup.band.service;

import dev.kkoncki.bandup.band.Band;
import dev.kkoncki.bandup.band.repository.BandRepository;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class BandHelperService {

    private final BandRepository bandRepository;

    public BandHelperService(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    public Band getBandOrThrow(String id) {
        return bandRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BAND_NOT_FOUND));
    }
}
