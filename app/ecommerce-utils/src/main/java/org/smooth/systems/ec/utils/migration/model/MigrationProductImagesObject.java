package org.smooth.systems.ec.utils.migration.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MigrationProductImagesObject {

	private Long srcProductId;

	private Long dstProductId;

	private List<File> imageUrls;

	public MigrationProductImagesObject(Long prodId, List<File> imageUrls) {
		srcProductId = prodId;
		this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
	}
}
