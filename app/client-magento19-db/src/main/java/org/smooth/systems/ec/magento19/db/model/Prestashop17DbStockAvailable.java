package org.smooth.systems.ec.magento19.db.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
@Data
@Entity
@Table(name="ps_stock_available")
@NoArgsConstructor
public class Prestashop17DbStockAvailable {

	public Prestashop17DbStockAvailable(Long productId, Long shopId, Long shopGroupId) {
		this.shopId = shopId;
		this.productId = productId;
		this.shopGroupId = shopGroupId;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_stock_available")
	private Long id;

	@Column(name = "id_product")
	private Long productId;

	@Column(name = "id_product_attribute")
	private Long productAttributeId = 0L;

	@Column(name = "id_shop")
	private Long shopId;

	@Column(name = "id_shop_group")
	private Long shopGroupId;

	@Column(name = "quantity")
	private Long quantity = 0L;

	@Column(name = "physical_quantity")
	private Long physical_quantity = 0L;

	@Column(name = "reserved_quantity")
	private Long reserved_quantity = 0L;

	@Column(name = "depends_on_stock")
	private Long depends_on_stock = 0L;

	@Column(name = "out_of_stock")
	private Long out_of_stock = 1L;
}