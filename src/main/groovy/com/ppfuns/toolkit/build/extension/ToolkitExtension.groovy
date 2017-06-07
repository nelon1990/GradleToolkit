package com.ppfuns.toolkit.build.extension

/**
 * Created by nelon on 17-4-10.
 */
class ToolkitExtension {
    int svnVersion = -1
    String projectName

    int svnVersion() {
        return svnVersion
    }

    void svnVersion(int pSvnVersion) {
        svnVersion = pSvnVersion
    }

    String projectName() {
        return projectName
    }

    void projectName(String pProjectName) {
        projectName = pProjectName
    }
}
