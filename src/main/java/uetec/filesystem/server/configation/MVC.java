package uetec.filesystem.server.configation;

import org.springframework.web.servlet.resource.*;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

import uetec.filesystem.server.pojo.ExtendStores;
import uetec.filesystem.server.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.*;
import org.springframework.boot.web.servlet.*;
import org.springframework.context.annotation.*;

/**
 * 
 * <h2>Web功能-MVC相关配置类</h2>
 * <p>
 * 该Spring配置类主要负责配置uetec-file-system网页服务器的处理行为。
 * </p>
 * 
 * @author 要么出众，要么出局
 * @version 1.0
 */
@Configurable
@ComponentScan({ "uetec.filesystem.server.controller", "uetec.filesystem.server.service.impl", "uetec.filesystem.server.util" })
@ServletComponentScan({ "uetec.filesystem.server.listener", "uetec.filesystem.server.filter" })
@Import({ DataAccess.class })
public class MVC extends ResourceHttpRequestHandler implements WebMvcConfigurer {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		// TODO 自动生成的方法存根
		configurer.enable();
	}

	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		// 将静态页面资源所在文件夹加入至资源路径中
		registry.addResourceHandler(new String[] { "/**" }).addResourceLocations(new String[] {
				"file:" + ConfigureReader.instance().getPath() + File.separator + "webContext" + File.separator });
		// 将所有文件块的保存路径加入至资源路径中（提供某些预览服务）
		List<String> paths = new ArrayList<>();
		paths.add("file:" + ConfigureReader.instance().getFileBlockPath());
		for (ExtendStores es : ConfigureReader.instance().getExtendStores()) {
			paths.add(
					"file:" + (es.getPath().getAbsolutePath().endsWith(File.separator) ? es.getPath().getAbsolutePath()
							: es.getPath().getAbsolutePath() + File.separator));
		}
		registry.addResourceHandler(new String[] { "/fileblocks/**" })
				.addResourceLocations(paths.toArray(new String[0]));
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		final MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(-1L);
		factory.setLocation(ConfigureReader.instance().getTemporaryfilePath());
		return factory.createMultipartConfig();
	}

	@Bean
	public Gson gson() {
		return new Gson();
	}
}
