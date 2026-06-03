package com.tools.seed.controller;

import com.tools.seed.common.result.Result;
import com.tools.seed.entity.SysFile;
import com.tools.seed.service.SysFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Schema(description = "文件管理接口")
public class SysFileController {

    private final SysFileService sysFileService;

    /** 上传文件 */
    @Operation(description = "上传文件")
    @PostMapping("/upload")
    public Result<SysFile> upload(@RequestParam("file") MultipartFile file,
                                  @RequestAttribute("userId") Long userId) {
        return Result.success(sysFileService.upload(file, userId));
    }

    /** 下载文件 */
    @Operation(description = "下载文件")
    @GetMapping("/download/{id}")
    public void download(@PathVariable Long id, HttpServletResponse response) {
        SysFile sysFile = sysFileService.download(id);

        Path path = Paths.get(sysFileService.getPhysicalPath(sysFile));
        try {
            String encodedName = URLEncoder.encode(sysFile.getOriginalName(), StandardCharsets.UTF_8)
                    .replace("+", "%20");

            response.setContentType(sysFile.getFileType() != null ? sysFile.getFileType() :
                    MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedName);
            response.setContentLengthLong(sysFile.getFileSize());

            Files.copy(path, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }

    /** 文件列表 */
    @Operation(description = "文件列表")
    @GetMapping("/list")
    public Result<List<SysFile>> list(@RequestAttribute("userId") Long userId) {
        return Result.success(sysFileService.listFiles(userId));
    }

    /** 删除文件 */
    @Operation(description = "删除文件")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        sysFileService.delete(id, userId);
        return Result.success();
    }
}