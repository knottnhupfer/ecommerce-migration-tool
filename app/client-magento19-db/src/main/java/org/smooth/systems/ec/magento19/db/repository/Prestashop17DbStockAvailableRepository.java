package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Prestashop17DbStockAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface Prestashop17DbStockAvailableRepository extends JpaRepository<Prestashop17DbStockAvailable, Long> {

//  List<Prestashop17DbStockAvailable> findAllByOrderByIdAsc();
}
