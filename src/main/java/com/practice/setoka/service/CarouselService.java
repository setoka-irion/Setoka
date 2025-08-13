package com.practice.setoka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.practice.setoka.Upload;
import com.practice.setoka.dao.Carousel;
import com.practice.setoka.dto.CarouselItem;
import com.practice.setoka.mapper.CarouselMapper;

@Service
public class CarouselService {

	@Autowired
	private Upload upload;
	@Autowired
	private CarouselMapper carouselMapper;
	
	public void insertCarousel(MultipartFile file, String linkUrl)
	{
		String fileName = upload.CarouselFileUpload(file);
		CarouselItem ca = new CarouselItem(fileName, linkUrl);
		carouselMapper.insertCarousel(ca);
	}
	
	public List<Carousel> selectAllCarousel()
	{
		return carouselMapper.selectAllCarousel();
	}
	
	public boolean deleteCarousel(int num, String fileName)
	{
		if(carouselMapper.deleteCarousel(num))
		{
			upload.deleteCarousel(fileName);
			return true;
		}
		return false;
	}
}
