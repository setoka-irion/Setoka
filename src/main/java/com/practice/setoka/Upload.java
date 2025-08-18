package com.practice.setoka;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Upload {

    @Value("${upload.path}")
    private String baseUploadPath;
    public final String imagePath = "/images/";
    public final String boardPath = "/board/";
    public final String tempPath = "/images/temp/";
    public final String carousel = "/images/Carousel/";
    
    public String BaseUploadPath() { return baseUploadPath; }
	public String SavePath() { return baseUploadPath + imagePath; }	
	public String TxtSavePath() { return baseUploadPath + boardPath; }
	public String TempImagePath() { return baseUploadPath + tempPath; }
	public String CarouselImagePath() { return baseUploadPath + carousel; }
	public String DefaultProfile() { return SavePath() + "defaultProfile.png"; }
	
	//사진 서버 디렉토리에 저장, 저장된 파일명 반환
	public String imageFileUpload(MultipartFile file)
	{
		String path = null;
		if (!file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String savedFilename = uuid + "_" + originalFilename;
			File uploadDir = new File(SavePath());

			path = savedFilename;
			if (!uploadDir.exists()) {
				uploadDir.mkdirs(); // 폴더가 없으면 자동 생성
			}

			File dest = new File(SavePath() + savedFilename);

			try {
				file.transferTo((dest));
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return imagePath + path;
	}
	
	public boolean imageFileDelete(String fileName)
	{
		File file = new File(SavePath() + fileName);
		if(!file.exists())
			return false;
		
		return file.delete();
	}
	
	public void SetDefaultBoard(MultipartFile file)
	{
		if (!file.isEmpty()) {
			File uploadDir = new File(SavePath());

			if (!uploadDir.exists()) {
				uploadDir.mkdirs(); // 폴더가 없으면 자동 생성
			}

			File dest = new File(SavePath() + "defaultBoard.png");

			try {
				file.transferTo((dest));
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void SetDefaultProfile(MultipartFile file)
	{
		if (!file.isEmpty()) {
			File uploadDir = new File(SavePath());

			if (!uploadDir.exists()) {
				uploadDir.mkdirs(); // 폴더가 없으면 자동 생성
			}

			File dest = new File(DefaultProfile());

			try {
				file.transferTo((dest));
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void SetDefaultAnimal(MultipartFile file)
	{
		if (!file.isEmpty()) {
			File uploadDir = new File(SavePath());

			if (!uploadDir.exists()) {
				uploadDir.mkdirs(); // 폴더가 없으면 자동 생성
			}

			File dest = new File(SavePath() + "defaultAnimal.png");

			try {
				file.transferTo((dest));
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String CarouselFileUpload(MultipartFile file)
	{
		String path = null;
		if (!file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String savedFilename = uuid + "_" + originalFilename;
			File uploadDir = new File(CarouselImagePath());

			path = savedFilename;
			if (!uploadDir.exists()) {
				uploadDir.mkdirs(); // 폴더가 없으면 자동 생성
			}

			File dest = new File(CarouselImagePath() + savedFilename);

			try {
				file.transferTo((dest));
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return carousel + path;
	}
	
	public boolean deleteCarousel(String fileName)
	{
		File file = new File(baseUploadPath + fileName);
		if(!file.exists())
			return false;
		
		return file.delete();
	}
	
	public String tempImageUpload(MultipartFile file)
	{
		String path = null;
		if (!file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String savedFilename = uuid + "_" + originalFilename;
			File uploadDir = new File(TempImagePath());

			path = savedFilename;
			if (!uploadDir.exists()) {
				uploadDir.mkdirs(); // 폴더가 없으면 자동 생성
			}

			File dest = new File(TempImagePath() + savedFilename);

			try {
				file.transferTo((dest));
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tempPath + path;
	}
	
	
	public String fileUpload(String txt)
	{
		String uuid = UUID.randomUUID().toString();
		String fileName = uuid + "_boardContent";
		File uploadDir = new File(TxtSavePath());
		if(!uploadDir.exists())
			uploadDir.mkdir();

		File file = new File(TxtSavePath() + fileName);
		try(FileWriter writer = new FileWriter(file))
		{
			writer.write(txt);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return boardPath + fileName;
	}
	
	public String fileLoad(String fileName)
	{
		File file = new File(baseUploadPath + fileName);
		if(!file.exists())
			return null;

		String text = null;
		
		try
		{
			text = Files.readString(Paths.get(baseUploadPath + fileName));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return text;
	}
	
	public boolean fileDelete(String fileName)
	{
		File file = new File(TxtSavePath() + fileName);
		if(!file.exists())
			return false;
		
		return file.delete();
	}
	
}