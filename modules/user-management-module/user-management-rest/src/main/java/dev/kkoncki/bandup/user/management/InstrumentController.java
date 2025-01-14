package dev.kkoncki.bandup.user.management;

import dev.kkoncki.bandup.user.management.instrument.Instrument;
import dev.kkoncki.bandup.user.management.instrument.forms.CreateInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.service.InstrumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instrument")
public class InstrumentController {

    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping("/{id}")
    public Instrument get(@PathVariable("id") String id) {
        return instrumentService.get(id);
    }

    @PostMapping("/save")
    public Instrument save(@RequestBody CreateInstrumentForm form) {
        return instrumentService.save(form);
    }

    @GetMapping("/all")
    public List<Instrument> getAll() {
        return instrumentService.getAll();
    }

    @GetMapping("/all-by-category/{categoryId}")
    public List<Instrument> getAllByCategoryId(@PathVariable("categoryId") String categoryId) {
        return instrumentService.getAllByCategoryId(categoryId);
    }
}
