package uetec.filesystem.server.service.impl;

import uetec.filesystem.server.service.*;
import org.springframework.stereotype.*;

import com.google.gson.Gson;

import uetec.filesystem.server.mapper.*;

import java.io.File;

import javax.annotation.*;
import javax.servlet.http.*;
import uetec.filesystem.server.model.*;
import uetec.filesystem.server.pojo.VideoInfo;
import uetec.filesystem.printer.Printer;
import uetec.filesystem.server.enumeration.*;
import uetec.filesystem.server.util.*;
import ws.schild.jave.MultimediaObject;

@Service
public class PlayVideoServiceImpl implements PlayVideoService {
	@Resource
	private NodeMapper fm;
	@Resource
	private Gson gson;
	@Resource
	private FileBlockUtil fbu;
	@Resource
	private LogUtil lu;

	private VideoInfo foundVideo(final HttpServletRequest request) {
		final String fileId = request.getParameter("fileId");
		if (fileId != null && fileId.length() > 0) {
			final Node f = this.fm.queryById(fileId);
			final VideoInfo vi = new VideoInfo(f);
			if (f != null) {
				final String account = (String) request.getSession().getAttribute("ACCOUNT");
				if (ConfigureReader.instance().authorized(account, AccountAuth.DOWNLOAD_FILES)) {
					final String fileName = f.getFileName();
					// 检查视频格式
					final String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					switch (suffix) {
					case "mp4":
					case "mov":
						// 对于mp4后缀的视频，进一步检查其编码是否为h264，如果是，则设定无需转码直接播放
						File target=fbu.getFileFromBlocks(f);
						if(target == null || !target.isFile()) {
							return null;
						}
						MultimediaObject mo = new MultimediaObject(target);
						try {
							if (mo.getInfo().getVideo().getDecoder().indexOf("h264") >= 0) {
								vi.setNeedEncode("N");
								return vi;
							}
						} catch (Exception e) {
							Printer.instance.print(e.getMessage());
							lu.writeException(e);
						}
						// 对于其他编码格式，则设定需要转码
						vi.setNeedEncode("Y");
						return vi;
					case "webm":
					case "avi":
					case "wmv":
					case "mkv":
					case "flv":
						vi.setNeedEncode("Y");
						return vi;
					default:
						break;
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getPlayVideoJson(final HttpServletRequest request) {
		final VideoInfo v = this.foundVideo(request);
		if (v != null) {
			return gson.toJson((Object) v);
		}
		return "ERROR";
	}
}
