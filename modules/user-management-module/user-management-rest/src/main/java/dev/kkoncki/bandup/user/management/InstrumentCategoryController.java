package dev.kkoncki.bandup.user.management;


import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategory;
import dev.kkoncki.bandup.user.management.instrument.category.forms.CreateInstrumentCategoryForm;
import dev.kkoncki.bandup.user.management.instrument.category.service.InstrumentCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instrument-category")
public class InstrumentCategoryController {

    private final InstrumentCategoryService instrumentCategoryService;

    public InstrumentCategoryController(InstrumentCategoryService instrumentCategoryService) {
        this.instrumentCategoryService = instrumentCategoryService;
    }

    @GetMapping("/{id}")
    public InstrumentCategory get(@PathVariable("id") String id) {
        return instrumentCategoryService.get(id);
    }

    @PostMapping("/save")
    public InstrumentCategory save(@RequestBody CreateInstrumentCategoryForm form) {
        return instrumentCategoryService.save(form);
    }

    @GetMapping("/all")
    public List<InstrumentCategory> getAll() {
        return instrumentCategoryService.getAll();
    }
}
