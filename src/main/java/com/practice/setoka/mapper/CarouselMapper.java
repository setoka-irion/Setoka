package com.practice.setoka.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.practice.setoka.dao.Carousel;
import com.practice.setoka.dto.CarouselItem;

@Mapper
public interface CarouselMapper {
	public void insertCarousel(CarouselItem item);
	public List<Carousel> selectAllCarousel();
	public boolean deleteCarousel(int num);
}
