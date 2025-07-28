package com.practice.setoka;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Upload {
	//public final String savePath = "/var/www/uploads/";	//실전
	public final String savePath = "C:/images/";	
	public final String txtSavePath = "C:/board/";
	
	//사진 서버 디렉토리에 저장, 저장된 파일명 반환
	public String imageFileUpload(MultipartFile file)
	{
		String path = null;
		if (!file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String savedFilename = uuid + "_" + originalFilename;
			File uploadDir = new File(savePath);

			path = savedFilename;
			if (!uploadDir.exists()) {
				uploadDir.mkdirs(); // 폴더가 없으면 자동 생성
			}

			File dest = new File(savePath + savedFilename);

			try {
				file.transferTo((dest));
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return path;
	}
	
	public boolean imageFileDelete(String fileName)
	{
		File file = new File(savePath + fileName);
		if(!file.exists())
			return false;
		
		return file.delete();
	}
	
	
	public String fileUpload(String txt)
	{
		String uuid = UUID.randomUUID().toString();
		String fileName = uuid + "_boardContent";
		File uploadDir = new File(txtSavePath);
		if(!uploadDir.exists())
			uploadDir.mkdir();
		
		File file = new File(txtSavePath + fileName);
		try(FileWriter writer = new FileWriter(file))
		{
			writer.write(txt);
			System.out.println("파일 저장 완료");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return fileName;
	}
	
	public String fileLoad(String fileName)
	{
		File file = new File(txtSavePath + fileName);
		if(!file.exists())
			return null;

		String text = null;
		
		try
		{
			text = Files.readString(Paths.get(txtSavePath + fileName));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return text;
	}
	
	public boolean fileDelete(String fileName)
	{
		File file = new File(txtSavePath + fileName);
		if(!file.exists())
			return false;
		
		return file.delete();
	}
}