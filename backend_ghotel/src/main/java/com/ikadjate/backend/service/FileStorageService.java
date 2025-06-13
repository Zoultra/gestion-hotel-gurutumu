package com.ikadjate.backend.service;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        // Créer le répertoire s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Sauvegarder le fichier
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        return fileName;
    }

    public String storeAndResizeImage(MultipartFile file, int targetWidth) throws IOException {
        String originalFileName = storeFile(file);
        
        // Redimensionner l'image
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage resizedImage = Scalr.resize(originalImage, targetWidth);
        
        // Générer un nouveau nom pour l'image redimensionnée
        String resizedFileName = "resized_" + originalFileName;
        Path resizedFilePath = Paths.get(uploadDir).resolve(resizedFileName);
        
        // Sauvegarder l'image redimensionnée
        ImageIO.write(resizedImage, "jpg", resizedFilePath.toFile());
        
        return resizedFileName;
    }

    public byte[] loadFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        return Files.readAllBytes(filePath);
    }
}