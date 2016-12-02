package com.jeffreyning.nhversion.common.version.mapping;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.jeffreyning.nhversion.common.version.cmd.IControllerCommand;
import com.jeffreyning.nhversion.common.version.util.NHBeanUtil;

/**
 * controller与cmd版本映射工具
 * 
 * @author ninghao
 * 
 */
public class CommonControllerMapping {
	@SuppressWarnings("rawtypes")
	private static Map confMap = new ConcurrentHashMap();

	/**
	 * 设置映射关系
	 * 
	 * @param controllerName
	 *            cotroller名称
	 * @param funcName
	 *            成员方法名称
	 * @param funcVer
	 *            成员方法版本
	 * @param cmd
	 *            命令对象
	 * @exception runtimeException
	 *                funcVer error
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setMapping(String controllerName, String funcName,
			String funcVer, IControllerCommand cmd) {
		int version = 0;
		version = getVersionInt(funcVer);
		if (version < 0) {
			throw new RuntimeException("funcVer error during setMapping");
		}
		if (controllerName == null || "".equals(controllerName)) {
			throw new RuntimeException("controllerName error during setMapping");
		}
		if (funcName == null || "".equals(funcName)) {
			throw new RuntimeException("funcName error during setMapping");
		}
		if (cmd == null) {
			throw new RuntimeException("cmd error during setMapping");
		}

		Map controllerMap = (Map) confMap.get(controllerName);
		if (controllerMap == null) {
			controllerMap = new ConcurrentHashMap();
			confMap.put(controllerName, controllerMap);
		}
		Map funcMap = (Map) controllerMap.get(funcName);
		if (funcMap == null) {
			funcMap = new TreeMap();
			controllerMap.put(funcName, funcMap);
		}
		funcMap.put(version, cmd);

	}

	/**
	 * 查询映射关系并触发cmd执行，version支持自动向下寻找,默认返回retObj对象
	 * 
	 * @param controllerName
	 *            cotroller名称
	 * @param funcName
	 *            成员方法名称
	 * @param funcVer
	 *            成员方法版本
	 * @param paramMap
	 *            参数
	 */
	public static Object execVersionMappingSimple(String controllerName,
			String funcName, String funcVer, Map paramMap) {
		Map retMap = CommonControllerMapping.execVersionMapping(controllerName,
				funcName, funcVer, paramMap);
		if (retMap != null) {
			return retMap.get(IControllerCommand.RETOBJ);
		}
		return null;
	}

	/**
	 * 查询映射关系并触发cmd执行，version支持自动向下寻找
	 * 
	 * @param controllerName
	 *            cotroller名称
	 * @param funcName
	 *            成员方法名称
	 * @param funcVer
	 *            成员方法版本
	 * @param paramMap
	 *            参数
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map execVersionMapping(String controllerName,
			String funcName, String funcVer, Map paramMap) {
		int version = 0;
		version = getVersionInt(funcVer);
		if (version < 0) {
			throw new RuntimeException("version format<0 error");
		}

		Map controllerMap = (Map) confMap.get(controllerName);
		if (controllerMap == null) {
			throw new RuntimeException("controllerMap is null");
		}

		TreeMap funcMap = (TreeMap) controllerMap.get(funcName);
		if (funcMap == null) {
			throw new RuntimeException("funcMap is null");
		}

		Object key = funcMap.floorKey(version);
		if (key == null) {
			throw new RuntimeException("version key is null");
		}

		IControllerCommand command = (IControllerCommand) funcMap.get(key);
		if (command == null) {
			throw new RuntimeException("command is null");
		}

		Map retMap = command.execute(paramMap);
		return retMap;

	}

	// 将版本字符串转为int
	public static int getVersionInt(String verStr) {
		int version = 0;
		if (verStr == null || "".equals(verStr)) {
			throw new RuntimeException("verStr is null");
		}
		String versionStr = verStr.toLowerCase().replace("v", "");
		String[] verArray = versionStr.split("_");
		try {
			int size = verArray.length;
			for (int i = 0; i < size && i < 3; i++) {
				if (i == 0) {
					String tempStr = verArray[0];
					int tempInt = Integer.valueOf(tempStr);
					version = tempInt * 100 * 100;
				} else if (i == 1) {
					String tempStr = verArray[1];
					int tempInt = Integer.valueOf(tempStr);
					version = version + tempInt * 100;
				} else if (i == 2) {
					String tempStr = verArray[2];
					int tempInt = Integer.valueOf(tempStr);
					version = version + tempInt;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("version format error", e);
		}
		return version;
	}

	/**
	 * 注册命令对象，根据注解添加注册信息
	 * 
	 * @param cmd
	 *            命令对象
	 * @exception runtimeException
	 *                没有注解信息
	 */
	public static void registry(IControllerCommand cmd) {
		ControllerMappingAnno annotation = cmd.getClass().getAnnotation(
				ControllerMappingAnno.class);
		if (annotation == null) {
			throw new RuntimeException(
					"ControllerCommand not found mapping annotation");
		}
		String controllerName = annotation.controllerName();
		String funcName = annotation.funcName();
		String funcVer = annotation.funcVer();
		CommonControllerMapping.setMapping(controllerName, funcName, funcVer,
				cmd);
	}

	public static void batchRegistry(Set classSet) throws Exception {
		Iterator it = classSet.iterator();
		while (it.hasNext()) {
			Class c = (Class) it.next();
			ControllerMappingAnno anno = (ControllerMappingAnno) c
					.getAnnotation(ControllerMappingAnno.class);
			if (anno == null) {
				continue;
			}
			String beanId=anno.beanId();
			IControllerCommand ic=null;
			if(beanId==null || beanId.equals("")){
				ic=(IControllerCommand) c.newInstance();
			}else{
				ic=(IControllerCommand) NHBeanUtil.getNHBean(beanId);
			}
			CommonControllerMapping.registry(ic);
		}
	}

	/**
	 * 根据包路径批量加载cmd
	 * 
	 * @param packDir
	 *            包路径
	 * @throws Exception
	 */
	public static void batchRegistryByPackage(String packDir) throws Exception {
		Set<Class> set = CommonControllerMapping
				.getClasses(packDir);
		batchRegistry(set);
	}

	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param pack
	 * @return
	 */
	public static Set<Class> getClasses(String pack) {

		Set<Class> classes = new LinkedHashSet<Class>();

		boolean recursive = true;

		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');

		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader()
					.getResources(packageDirName);

			while (dirs.hasMoreElements()) {

				URL url = dirs.nextElement();

				String protocol = url.getProtocol();

				if ("file".equals(protocol)) {

					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");

					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive, classes);
				} else if ("jar".equals(protocol)) {

					JarFile jar = null;
					try {

						jar = ((JarURLConnection) url.openConnection())
								.getJarFile();

						Enumeration<JarEntry> entries = jar.entries();

						while (entries.hasMoreElements()) {

							JarEntry entry = entries.nextElement();
							String name = entry.getName();

							if (name.charAt(0) == '/') {

								name = name.substring(1);
							}

							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');

								if (idx != -1) {

									packageName = name.substring(0, idx)
											.replace('/', '.');
								}

								if ((idx != -1) || recursive) {

									if (name.endsWith(".class")
											&& !entry.isDirectory()) {

										String className = name.substring(
												packageName.length() + 1,
												name.length() - 6);
										try {

											classes.add(Class
													.forName(packageName + '.'
															+ className));
										} catch (ClassNotFoundException e) {

											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, Set<Class> classes) {

		File dir = new File(packagePath);

		if (!dir.exists() || !dir.isDirectory()) {

			return;
		}

		File[] dirfiles = dir.listFiles(new FileFilter() {

			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});

		for (File file : dirfiles) {

			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(
						packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive, classes);
			} else {

				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					classes.add(Thread.currentThread().getContextClassLoader()
							.loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {

					e.printStackTrace();
				}
			}
		}
	}

}
