package com.futuristlabs.p2p.rest.v1;


import com.futuristlabs.p2p.func.cfg.SystemParam;
import com.futuristlabs.p2p.func.cfg.SystemParametersRepository;
import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeCategory;
import com.futuristlabs.p2p.func.lifeupgrade.ReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private ReferenceRepository referenceRepository;
    private SystemParametersRepository systemParametersRepository;

    @Autowired
    public AdminController(ReferenceRepository referenceRepository, SystemParametersRepository systemParametersRepository) {
        this.referenceRepository = referenceRepository;
        this.systemParametersRepository = systemParametersRepository;
    }

    @RequestMapping(value = "/lifeUpgradeCategories", method = GET)
    @ResponseBody
    public List<LifeUpgradeCategory> lifeUpgradeCategoriesList() {
        return referenceRepository.modifiedLifeUpgradeCategories(null);
    }

    @RequestMapping(value = "/lifeUpgradeCategories", method = POST)
    @ResponseBody
    public UUID lifeUpgradeCategoriesCreate(@RequestBody LifeUpgradeCategory category) {
        if (category.getId() == null) {
            category.setId(UUID.randomUUID());
        }

        referenceRepository.modifyLifeUpgradeCategory(category);
        return category.getId();
    }

    @RequestMapping(value = "/lifeUpgradeCategories/{id}", method = POST)
    @ResponseStatus(NO_CONTENT)
    public void lifeUpgradeCategoriesUpdate(@PathVariable("id") UUID categoryId, @RequestBody LifeUpgradeCategory category) {
        category.setId(categoryId);
        referenceRepository.modifyLifeUpgradeCategory(category);
    }

    @RequestMapping(value = "/lifeUpgradeCategories/{id}", method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void lifeUpgradeCategoriesDelete(@PathVariable("id") UUID categoryId) {
        referenceRepository.deleteLifeUpgradeCategory(categoryId);
    }

    @RequestMapping(value = "/system-params", method = POST)
    @ResponseStatus(NO_CONTENT)
    public void setSystemParameter(@RequestBody SystemParam param) {
        systemParametersRepository.setParameter(param);
    }

}
