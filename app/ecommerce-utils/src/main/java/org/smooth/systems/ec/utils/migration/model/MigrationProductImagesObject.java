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

	public MigrationProductImagesObject(Long srcProdId, Long dstProdId, List<File> imageUrls) {
		srcProductId = srcProdId;
		dstProductId = dstProdId;
		this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
	}
}
