package com.ppfuns.toolkit.build.task

import com.ppfuns.toolkit.build.PpfunsTools
import com.ppfuns.toolkit.build.extension.ToolkitExtension
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.tmatesoft.svn.core.wc.*

import java.util.zip.ZipFile

/**
 * Created by nelon on 17-4-10.
 */
class SvnLogTask extends DefaultTask {
    void createLog() {
        Map svnLog = new HashMap<>()
        def project = getProject()

        def file = new File(project.projectDir.absolutePath + '/src/main/assets/svn_log.json')
        if (!file.exists()) {
            println "svn.log is not exists, and it will be created"
            def parentFile = file.getParentFile()
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
        } else {
            file.delete()
        }
        file.createNewFile()

        List<Map> mapList = new ArrayList<>()
        project.configurations.compile.incoming.files.each {
            if (it.name.endsWith("aar")) {
                def log = readSvnLog(it.absolutePath)
                if (log != null && !log.isEmpty()) {
                    mapList.add(log)
                }
            }
        }

        ToolkitExtension toolkitExt = project.extensions.findByName(PpfunsTools.TOOLKIT_EXT)
        def svnVersion = toolkitExt.svnVersion
        if (svnVersion < 0) {
            svnVersion = getSvnVersion()
        }

        String projectName = toolkitExt.projectName
        if (projectName == null || "" == projectName) {
            projectName = getProject().name
        }

        svnLog.put(projectName, svnVersion)
        svnLog.put("dependencies", mapList)

        def output = new JsonOutput()
        def json = output.toJson(svnLog)

        println("---------- SVN LOG BEGIN ----------")
        println(output.prettyPrint(json))
        println("----------- SVN LOG END -----------")

        def fos = new FileOutputStream(file)
        fos.withPrintWriter { writer ->
            writer.append(json)
                    .flush()
        }
        fos.close()
    }

    static Map<String, Integer> readSvnLog(String pPath) {
        Map map = new HashMap()

        def zipFile = new ZipFile(new File(pPath))
        def assetsEntry = zipFile.getEntry("assets/svn_log.json")

        if (assetsEntry != null) {
            def zipIn = zipFile.getInputStream(assetsEntry)

            def jsonSlurper = new JsonSlurper()
            def parse = jsonSlurper.parse(zipIn)
            if (parse instanceof Map) {
                map.putAll(parse)
            }

            zipIn.close()
        }

        return map
    }

    long getSvnVersion() {
        long versionCode = -1

        try {
            ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
            SVNClientManager clientManager = SVNClientManager.newInstance(options);
            SVNStatusClient statusClient = clientManager.getStatusClient();
            SVNStatus status = statusClient.doStatus(getProject().projectDir, false);
            SVNRevision revision = status.getCommittedRevision();

            versionCode = revision.getNumber()
        } catch (Exception pE) {
            println(pE.getMessage())
        }
        return versionCode
    }
}
