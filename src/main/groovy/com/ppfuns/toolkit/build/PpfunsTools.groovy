package com.ppfuns.toolkit.build

import com.ppfuns.toolkit.build.extension.ToolkitExtension
import com.ppfuns.toolkit.build.task.SvnLogTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by nelon on 17-4-10.
 */
class PpfunsTools implements Plugin<Project> {
    public static final String TOOLKIT_EXT = "toolkit"
    public static final String SVN_TASK_NAME = "createSvnLog"

    @Override
    void apply(Project target) {
        target.extensions.create(TOOLKIT_EXT, ToolkitExtension)

        createSvnLogTask(target)
        createUploadTask(target)

    }

    void createUploadTask(Project pProject) {

    }

    void createSvnLogTask(Project pProject) {
        def svnLogTask = pProject.tasks.create(SVN_TASK_NAME, SvnLogTask)
        svnLogTask.doLast {
            createLog()
        }
        pProject.tasks.all {
            def taskSuperClass = it.class.superclass.simpleName

            if (taskSuperClass == "PackageApplication"
                    || taskSuperClass == "Zip") {
                it.dependsOn SVN_TASK_NAME
            }
        }
    }
}
