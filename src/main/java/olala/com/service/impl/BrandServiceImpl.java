package olala.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import olala.com.constant.Constant;
import olala.com.entities.Brand;
import olala.com.model.PageInfo;
import olala.com.repository.BrandRepository;
import olala.com.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {
	@Autowired
	BrandRepository brandRepository;

	/*
	 * Save Set status to is active
	 */
	@Override
	public Brand save(Brand brand) {
		// check if record have already exist
		String name = brand.getName();
		if (existsByName(name)) {
			// set Id to existed Id
			brand.setId(findByName(name).getId());
		}
		// status to active
		return brandRepository.save(brand);
	}

	/* existByName */
	@Override
	public Boolean existsByName(String name) {
		return brandRepository.existsByName(name);
	}

	/* find By PageInfo */
	@Override
	public Page<Brand> findByPageInfo(PageInfo pageInfo) {
		String name = pageInfo.getKeyword().trim();
		Integer pageSize = pageInfo.getPageSize();
		Integer pageNumber = pageInfo.getPageNumber();

		Integer sortType = pageInfo.getSortType();
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());

		if (sortType == Constant.SortType.DESCENDING) {
			pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("name").descending());
		}

		Page<Brand> page = brandRepository.findByNameContains(name, pageRequest);

		return page;
	}

	/*
	 * deleteById set status to not active
	 */
	@Override
	public void deleteById(Long id) {
		brandRepository.deleteById(id);
	}

	/* findById */
	@Override
	public Brand findById(Long id) {
		Optional<Brand> brand = brandRepository.findById(id);
		if (brand.isPresent()) {
			return brand.get();
		}
		return null;
	}

	/* findByName */
	@Override
	public Brand findByName(String name) {
		Optional<Brand> brand = brandRepository.findByName(name);
		if (brand.isPresent()) {
			return brand.get();
		}
		return null;
	}
	
	/* find All */
	@Override
	public List<Brand> findAll() {
		return brandRepository.findAll();
	}
}
