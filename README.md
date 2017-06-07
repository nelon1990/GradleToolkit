# gradle toolkit
**`_针对Android工程使用_`**

##功能说明
1. **SVN版本记录**
    
    >gradle构建过程中,自动生成SVN版本记录文件(assets下,json文件,若依赖库中同样有此记录文件,插件会一同读取并写入apk的版本记录文件中),方便对APK版本进行追踪；
    >
    >        {
    >         "filemanager": 5388,
    >         "dependencies": [
    >           {
    >             "lib1": 5388,
    >             "dependencies": [
    >               {
    >                 "lib3": 5388,
    >                 "dependencies": []
    >               }
    >             ]
    >           },
    >           {
    >             "lib2": 5388,
    >             "dependencies": []
    >           }
    >         ]
    >       }