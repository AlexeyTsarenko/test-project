package com.springProject.services;

import com.springProject.entities.HistoryEntity;
import com.springProject.entities.SimpleEntity;
import com.springProject.models.RequestModel;
import com.springProject.models.SimpleEntityModel;
import com.springProject.models.UpdateModel;
import com.springProject.repositories.EntityRepository;
import com.springProject.repositories.HistoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EntityService {

    private final EntityRepository entityRepository;
    private final HistoryRepository historyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    EntityService(EntityRepository entityRepository, ModelMapper modelMapper, HistoryRepository historyRepository) {
        this.entityRepository = entityRepository;
        this.modelMapper = modelMapper;
        this.historyRepository = historyRepository;
    }

    public SimpleEntity save(SimpleEntityModel simpleEntityModel) {
        SimpleEntity simpleEntity = modelMapper.map(simpleEntityModel, SimpleEntity.class);
        simpleEntity.setTotalPrice(simpleEntity.getPrice() * simpleEntity.getAmount());
        simpleEntity.setStatus("active");
        return entityRepository.save(simpleEntity);
    }

    public ResponseEntity<String> update(UpdateModel updateModel) {
        Optional<SimpleEntity> optional = entityRepository.findById(updateModel.getId());
        if (optional.isPresent()&&!(optional.get().getStatus().equals("deleted"))) {
            SimpleEntity simpleEntity = optional.get();
            return createHistory(updateModel, simpleEntity);
        } else {
            return new ResponseEntity<>("No record with such id", HttpStatus.NOT_FOUND);
        }

    }
    public ResponseEntity<String> delete(int id) {
        Optional<SimpleEntity> optional = entityRepository.findById(id);
        if(optional.isPresent()){
            SimpleEntity simpleEntity = optional.get();
            simpleEntity.setStatus("deleted");
            entityRepository.save(simpleEntity);
            return new ResponseEntity<>(simpleEntity.getId().toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>("No record with such id", HttpStatus.NOT_FOUND);
    }
    public Page<SimpleEntity> get(RequestModel requestModel, Pageable pageableRequest) {
        if((requestModel.getId() != null)&&(requestModel.getStatus() != null)){
            return entityRepository.findAllByIdAndStatus(requestModel.getId(),requestModel.getStatus(), pageableRequest);
        }else if(requestModel.getId()!=null){
            return entityRepository.findAllById(requestModel.getId(), pageableRequest);
        }else if(requestModel.getStatus()!=null){
            return entityRepository.findAllByStatus(requestModel.getStatus(), pageableRequest);
        }
        else return null;
    }

    private ResponseEntity<String> createHistory(UpdateModel updateModel, SimpleEntity simpleEntity) {
        boolean recalculation = false;
        boolean nothingToUpdate = true;
        HistoryEntity historyEntity;
        List<HistoryEntity> historyEntityList = new ArrayList<>();
        if (!(simpleEntity.getAmount().equals(updateModel.getAmount()))&&updateModel.getAmount()!=null) {
            historyEntity = new HistoryEntity();
            historyEntity.setFieldName("amount");
            historyEntity.setOldValue(simpleEntity.getAmount().toString());
            historyEntity.setNewValue(updateModel.getAmount().toString());
            simpleEntity.setAmount(updateModel.getAmount());
            historyEntityList.add(historyEntity);
            recalculation = true;
            nothingToUpdate = false;
        }
        if (!(simpleEntity.getPrice().equals(updateModel.getPrice()))&&updateModel.getPrice()!=null) {
            historyEntity = new HistoryEntity();
            historyEntity.setFieldName("price");
            historyEntity.setOldValue(simpleEntity.getPrice().toString());
            historyEntity.setNewValue(updateModel.getPrice().toString());
            simpleEntity.setPrice(updateModel.getPrice());
            historyEntityList.add(historyEntity);
            recalculation = true;
            nothingToUpdate = false;
        }
        if (!(simpleEntity.getReleaseDate().equals(updateModel.getReleaseDate()))&&updateModel.getReleaseDate()!=null) {
            historyEntity = new HistoryEntity();
            historyEntity.setFieldName("date");
            historyEntity.setOldValue(simpleEntity.getReleaseDate().toString());
            historyEntity.setNewValue(updateModel.getReleaseDate().toString());
            simpleEntity.setReleaseDate(updateModel.getReleaseDate());
            historyEntityList.add(historyEntity);
            nothingToUpdate = false;
        }
        if (recalculation) {
            historyEntity = new HistoryEntity();
            historyEntity.setFieldName("totalPrice");
            historyEntity.setOldValue(simpleEntity.getTotalPrice().toString());
            int newTotalPrice = simpleEntity.getPrice() * simpleEntity.getAmount();
            historyEntity.setNewValue(Integer.toString(newTotalPrice));
            simpleEntity.setTotalPrice(newTotalPrice);
            historyEntityList.add(historyEntity);
        }
        if(nothingToUpdate){
            return new ResponseEntity<>("Nothing to update", HttpStatus.OK);
        }
        simpleEntity.setStatus("updated");
        historyEntityList.forEach(x -> x.setEntityId(simpleEntity.getId()));
        historyEntityList.forEach(historyRepository::save);
        entityRepository.save(simpleEntity);
        return new ResponseEntity<>(simpleEntity.getId().toString(), HttpStatus.OK);
    }

}
