package com.tools.seed.service;

import com.tools.seed.entity.SysFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 22913
* @description 针对表【sys_file(文件表)】的数据库操作Service
* @createDate 2026-05-30 15:12:10
*/
public interface SysFileService extends IService<SysFile> {

    /** 上传文件，返回文件记录 */
    SysFile upload(org.springframework.web.multipart.MultipartFile file, Long userId);

    /** 下载文件 */
    SysFile download(Long fileId);

    /** 获取文件列表 */
    List<SysFile> listFiles(Long userId);

    /** 删除文件（逻辑删除+物理删除文件） */
    void delete(Long fileId, Long userId);

    /** 获取文件的实际磁盘路径 */
    String getPhysicalPath(SysFile sysFile);
}
