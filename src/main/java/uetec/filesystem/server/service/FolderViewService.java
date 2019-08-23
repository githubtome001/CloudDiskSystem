package uetec.filesystem.server.service;

import javax.servlet.http.*;

/**
 * 
 * <h2>文件夹视图逻辑处理</h2>
 * <p>
 * 所谓文件夹视图——就是用于显示在页面上的一个文件夹信息封装，包含了内容、可用权限、信息等。
 * 该服务层用于查询、拼装、处理文件夹视图的相关请求，是uetec-file-system核心功能的主要组成之一。
 * </p>
 * @author 要么出众，要么出局
 * @version 1.0
 */
public interface FolderViewService{
	
	/**
	 * 
	 * <h2>根据主键获取文件夹视图</h2>
	 * <p>由请求主键返回该文件夹的视图并以JSON格式封装，用于前端页面显示。</p>
	 * @author 要么出众，要么出局
	 * @param fid 要获取的文件夹ID
	 * @param session 用户会话，用于判权
	 * @param request 请求对象
	 * @return java.lang.String 该文件夹视图的JSON格式，原型详见uetec.filesystem.server.pojo.FolderView类。
	 */
    String getFolderViewToJson(final String fid, final HttpSession session, final HttpServletRequest request);
    
    /**
     * 
     * <h2>全路径查询</h2>
     * <p>根据文件夹ID和关键字生成一个全路径查询视图，结构类似于普通文件夹视图，但是其中多出一些属性。</p>
     * @author 要么出众，要么出局
     * @param request 请求对象，其中应包含fid和keyworld两个参数，分别代表在哪个文件夹下查询以及查询哪个关键字
     * @return java.lang.String 该文件夹视图的JSON格式，原型详见uetec.filesystem.server.pojo.SreachView类。
     */
    String getSreachViewToJson(final HttpServletRequest request);
}
