package ru.mts.siebel.order.service;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.siebel.order.entity.Storage;
import ru.mts.siebel.order.repository.StorageRepository;

import java.util.List;

@Log4j2
@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;

    public List<Storage> findAll() {
        return repository.findAll();
    }

    public Storage findByProductCode(final String productCode) {
        if (size() == 0) {
            log.info("На складе отсутсвуют товары");
        } else {
            List<Storage> storageList = findAll();
            for (Storage storage : storageList) {
                if (storage.getProductCode().equals(productCode))
                    return storage;
            }
        }
        return null;
    }

    public int size() {
        return findAll().size();
    }

    @Transactional
    public void add(final Storage storage) {
        repository.save(storage);
    }

    public boolean checkAndReserve(final String productCode, final Integer quantity) {
        Storage storage = findByProductCode(productCode);
        if (storage != null) {
            Integer storageQuantity = storage.getQuantity();
            if (quantity <= storageQuantity) {
                storage.setQuantity(storageQuantity - quantity);
                repository.save(storage);
                log.info("Товар " + productCode + " в количестве " + quantity + " зарезервирован на складе");
                return true;
            } else {
                log.info("Недостаточно товара {} на складе. Запрошено: {}, в наличии: {}", productCode, quantity, storageQuantity);
                return false;
            }
        } else {
            log.info("Товар " + productCode + " не найден на складе");
        }
        return false;
    }

}
