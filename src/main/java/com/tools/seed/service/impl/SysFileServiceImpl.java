package com.tools.seed.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.seed.common.exception.BusinessException;
import com.tools.seed.config.FileProperties;
import com.tools.seed.entity.SysFile;
import com.tools.seed.mapper.SysFileMapper;
import com.tools.seed.service.SysFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
* @author 22913
* @description 针对表【sys_file(文件表)】的数据库操作Service实现
* @createDate 2026-05-30 15:12:10
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile>
    implements SysFileService{

    private final FileProperties fileProperties;

    @Override
    public SysFile upload(MultipartFile file, Long userId) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        String originalName = file.getOriginalFilename();
        String extension = getExtension(originalName);
        validateExtension(extension);

        // 按日期分目录：uploads/2026/05/30/
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path dirPath = Paths.get(fileProperties.getUploadPath(), dateDir);
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new BusinessException("创建上传目录失败");
        }

        // 存储文件名用UUID防重名和路径遍历
        String storedName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        Path filePath = dirPath.resolve(storedName);

        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new BusinessException("文件保存失败");
        }

        SysFile sysFile = new SysFile();
        sysFile.setOriginalName(originalName);
        sysFile.setStoredName(storedName);
        sysFile.setFilePath(dateDir + "/" + storedName);
        sysFile.setFileSize(file.getSize());
        sysFile.setFileType(file.getContentType());
        sysFile.setExtension(extension);
        sysFile.setUploadUserId(userId);

        this.save(sysFile);
        log.info("文件上传成功: {} → {}", originalName, filePath);
        return sysFile;
    }

    @Override
    public SysFile download(Long fileId) {
        SysFile sysFile = this.getById(fileId);
        if (sysFile == null) {
            throw new BusinessException("文件不存在");
        }
        Path path = Paths.get(fileProperties.getUploadPath(), sysFile.getFilePath());
        if (!Files.exists(path)) {
            throw new BusinessException("文件已被删除或丢失");
        }
        return sysFile;
    }

    @Override
    public List<SysFile> listFiles(Long userId) {
        return this.list(
                Wrappers.<SysFile>lambdaQuery()
                        .eq(SysFile::getUploadUserId, userId)
                        .orderByDesc(SysFile::getCreateTime)
        );
    }

    @Override
    public void delete(Long fileId, Long userId) {
        SysFile sysFile = this.getById(fileId);
        if (sysFile == null) {
            throw new BusinessException("文件不存在");
        }
        if (!sysFile.getUploadUserId().equals(userId)) {
            throw new BusinessException("无权删除他人文件");
        }
        // 逻辑删除数据库记录
        this.lambdaUpdate().set(SysFile::getDeleted, true).eq(SysFile::getId, fileId).update();
        // 物理删除文件
        try {
            Path path = Paths.get(fileProperties.getUploadPath(), sysFile.getFilePath());
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.warn("物理删除文件失败: {}", sysFile.getFilePath(), e);
        }
    }

    @Override
    public String getPhysicalPath(SysFile sysFile) {
        return Paths.get(fileProperties.getUploadPath(), sysFile.getFilePath()).toString();
    }

    private String getExtension(String filename) {
        if (!StringUtils.hasText(filename) || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private void validateExtension(String extension) {
        if (!StringUtils.hasText(extension)) {
            throw new BusinessException("不允许上传无扩展名的文件");
        }
        Set<String> allowed = Set.of(fileProperties.getAllowedExtensions().split(","));
        if (!allowed.contains(extension.toLowerCase())) {
            throw new BusinessException("不允许上传 " + extension + " 类型的文件");
        }
    }
}




