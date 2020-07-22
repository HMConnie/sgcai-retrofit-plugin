package com.sgcai.andoid.retrofit


import org.gradle.api.Plugin
import org.gradle.api.Project

class RetrofitModulePlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        def file = project.file("service.json")
        if (!file.exists()) {
            throw new RuntimeException('文件不存在,请检查文件!')
        }
        def service = ServiceCreate.getFileToBean(file)
        if (service != null && service.observables != null && !service.observables.isEmpty()) {
            def observablePackageName = new String(service.observablePath).replace("/", ".")
            def stringBuilder = new StringBuilder()
            stringBuilder.append("package " + observablePackageName + ";" + "\n")
            stringBuilder.append("public interface " + service.observableClassName + " {");
            for (Service.ObservableBean observableBean : service.observables) {
                ServiceCreate.createBean(service, observableBean)
                stringBuilder.append(ServiceCreate.createObservableMethod(observableBean))
            }
            stringBuilder.append("\n}")
            def realPath = service.observablePath.replace("/", File.separator)
            def apiFile = new File(Constants.SRC_JAVA_DIR + File.separator + realPath + File.separator + service.observableClassName + ".java")
            FileUtils.writeString2File(stringBuilder.toString(), apiFile)
        }


    }
}
