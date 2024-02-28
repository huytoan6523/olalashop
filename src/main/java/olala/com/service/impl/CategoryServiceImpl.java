package olala.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import olala.com.constant.Constant;
import olala.com.entities.Category;
import olala.com.model.PageInfo;
import olala.com.repository.CategoryRepository;
import olala.com.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	CategoryRepository categoryRepository;

	/*
	 * Save Set status to is active
	 */
	@Override
	public Category save(Category category) {
		// check if record have already exist
//		String name = category.getName();
//		if (existsByName(name)) {
//			// set Id to existed Id
//			category.setId(findByName(name).getId());
//		}
		// status to active
		return categoryRepository.save(category);
	}

	/* existByName */
	@Override
	public Boolean existsByName(String name) {
		return categoryRepository.existsByName(name);
	}

	/* find By PageInfo */
	@Override
	public Page<Category> findByPageInfo(PageInfo pageInfo) {
		String name = pageInfo.getKeyword().trim();
		Integer pageSize = pageInfo.getPageSize();
		Integer pageNumber = pageInfo.getPageNumber();

		Integer sortType = pageInfo.getSortType();
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());

		if (sortType == Constant.SortType.DESCENDING) {
			pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("name").descending());
		}

		Page<Category> page = categoryRepository.findByNameContainsAndParentCategoryNull(name, pageRequest);
		return page;
	}

	/*
	 * deleteById set status to not active
	 */
	@Override
	public void deleteById(Long id) {
		categoryRepository.deleteById(id);
	}

	/* findById */
	@Override
	public Category findById(Long id) {
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isPresent()) {
			return category.get();
		}
		return null;
	}

	/* findByName */
	@Override
	public Category findByName(String name) {
		Optional<Category> category = categoryRepository.findByName(name);
		if (category.isPresent()) {
			return category.get();
		}
		return null;
	}
	
	/* findByParentCategory */
	@Override
	public List<Category> findByParentCategory(Category parentCategory) {
		return categoryRepository.findByParentCategory(parentCategory);
	}
	
	/* findReadyCategory */
	@Override
	public List<Category> findReadyCategory() {
		return categoryRepository.findReadyCategory();
	}
}
