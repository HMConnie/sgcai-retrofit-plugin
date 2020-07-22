package com.sgcai.andoid.retrofit;

import java.io.File;
import java.util.Map;

public class ServiceCreate {


    private static String reqParamBeanContent(Service service, Service.ObservableBean observableBean) {
        StringBuilder sb = new StringBuilder();
        String reqPackage = service.requestPath.replace("/", ".");
        sb.append("package " + reqPackage + ";" + "\n");
        if (service.baseParamClass != null && !"".equals(service.baseParamClass)) {
            sb.append("import " + service.baseParamClass + ";\n");
            int indexOf = service.baseParamClass.lastIndexOf('.') + 1;
            String className = service.baseParamClass.substring(indexOf);
            sb.append("public class " + observableBean.requestClassName() + " extends " + className + " {");
        } else {
            sb.append("public class " + observableBean.requestClassName() + " {");
        }

        sb.append(JsonUtils.getJavaFromJson(observableBean.requestJson));
        sb.append("\n}");
        return sb.toString();
    }

    private static String respBeanContent(Service service, Service.ObservableBean observableBean) {
        StringBuilder sb = new StringBuilder();
        String respPackage = service.responsePath.replace("/", ".");
        sb.append("package " + respPackage + ";" + "\n");
        if (service.baseBeanClass != null && !"".equals(service.baseBeanClass)) {
            sb.append("import " + service.baseBeanClass + ";\n");
            int indexOf = service.baseBeanClass.lastIndexOf('.') + 1;
            String className = service.baseBeanClass.substring(indexOf);
            sb.append("public class " + observableBean.responseClassName() + " extends " + className + " {");
        } else {
            sb.append("public class " + observableBean.responseClassName() + " {");
        }
        sb.append(JsonUtils.getJavaFromJson(observableBean.responseJson));
        sb.append("\n}");
        return sb.toString();
    }

    /**
     * 创建请求和响应数据模型
     */
    public static void createBean(Service service, Service.ObservableBean observableBean) {
        //生成请求model
        String reqContent = reqParamBeanContent(service, observableBean);
        FileUtils.writeString2File(reqContent, new File(Constants.SRC_JAVA_DIR + File.separator + service.requestPath + File.separator + observableBean.requestClassName() + ".java"));

        //生成响应model
        String respContent = respBeanContent(service, observableBean);
        FileUtils.writeString2File(respContent, new File(Constants.SRC_JAVA_DIR + File.separator + service.responsePath + File.separator + observableBean.responseClassName() + ".java"));
    }

    /**
     * 创建请求参数
     */
    public static String createParam(String json) {
        if (json == null || "".equals(json)) {
            return "";
        }
        Map<String, Object> map = JsonUtils.fromJsonToMap(json);
        Object[] objects = map.keySet().toArray();
        String query = "";
        for (int i = 0; i < objects.length; i++) {
            String name = objects[i].toString();
            query += "@Query(\"" + name + "\") String " + name;
            if (i < objects.length - 1) {
                query += " , ";
            }
        }
        return query;
    }

    /**
     * 创建observable方法
     */
    public static String createObservableMethod(Service.ObservableBean observableBean) {
        String result = "";
        String responseClassName = observableBean.responseClassName();
        String methodName = observableBean.methodName();
        if ("GET".equals(observableBean.requestMethod)) {
            result = "\n@GET(\"" + observableBean.url + "\")\n" + "Observable<" + responseClassName + "> " + methodName + "(" + createParam(observableBean.requestJson) + ");\n";
        } else if ("POST".equals(observableBean.requestMethod)) {
            result = "\n@POST(\"" + observableBean.url + "\")\n" + "Observable<" + responseClassName + "> " + methodName + "(" + createParam(observableBean.requestJson) + ");\n";
        }
        return result;

    }

    /**
     * 读取json文件
     */
    public static Service getFileToBean(File file) {
        String content = FileUtils.readToString(file);
        return JsonUtils.fromJsonBean(content, Service.class);
    }

}
