package org.woehlke.simpleworklist.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Area;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.NewAreaFormBean;
import org.woehlke.simpleworklist.repository.AreaRepository;
import org.woehlke.simpleworklist.services.AreaService;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class AreaServiceImpl implements AreaService {

    @Inject
    private AreaRepository areaRepository;


    @Override
    public List<Area> getAllForUser(UserAccount user) {
        return areaRepository.findByUserAccount(user);
    }

    @Override
    public Area findByIdAndUserAccount(long newAreaId, UserAccount userAccount) {
        return areaRepository.findByIdAndUserAccount(newAreaId,userAccount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void createNewArea(NewAreaFormBean newArea, UserAccount user) {
        Area area = new Area();
        area.setNameEn(newArea.getNameEn());
        area.setNameDe(newArea.getNameDe());
        area.setUserAccount(user);
        areaRepository.saveAndFlush(area);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void updateArea(NewAreaFormBean editArea, UserAccount user, long areaId) {
        Area area = areaRepository.findOne(areaId);
        if(area.getUserAccount().getId() == user.getId()){
            area.setNameEn(editArea.getNameEn());
            area.setNameDe(editArea.getNameDe());
            areaRepository.saveAndFlush(area);
        }
    }
}
