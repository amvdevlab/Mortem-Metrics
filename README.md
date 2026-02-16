![Image](https://github.com/user-attachments/assets/6c28b351-b856-48ef-b416-1fd0732dcbb1)

**Incident timeline generator for post-mortem analysis**

>[!IMPORTANT]
>```
>[HIBERNATED]
>```

## Overview

Mortem Metrics addresses the data aggregation challenge in post-mortem incident analysis. The application streamlines timeline construction by ingesting disparate data sources—Slack messages, PagerDuty alerts, screenshots, and ticket logs—and automatically generating a chronological incident timeline. This eliminates manual searching across siloed systems and reduces human error in reconstructing failure sequences.
The web application supports four initial file formats (.csv, .txt, .log, .json) with automated timeline generation. Output renders in markdown for easy editing and export, enabling seamless integration into final post-mortem reports. A history dashboard provides quick access to previously generated timelines, each tagged with incident identifiers.
Planned enhancements include customizable markdown templates for organizational compliance, advanced prompt engineering for fine-tuned reasoning, timeline regeneration capabilities, and persistent storage for both timelines and source files.

## Deliverables

###### Built

&check; UI Mockups  
&check; Database Schema  
&check; API Endpoints  
&check; Testing and CI/CD pipeline  
&check; Spring Profiles for development and production  
&check; Database connection for PostgreSQL & Supabase  
&check; File parsing from supported file types to compressed text

###### Delayed

&cross; AI integration  
&cross; Prompt engineering   
&cross; Frontend (Welcome, Import modal, Generation progress, Timeline editor, History dashboard)  
&cross; API service layer and frontend implementation
&cross; Deployment (Containerization with a Docker image)  

## Design and Development

###### UI

Complete UI/UX design system spanning the full user workflow: welcome screen, file import modal, generation progress indicators, markdown timeline editor, and history dashboard for reference and retrieval. 

<img width="1440" height="1024" alt="Image" src="https://github.com/user-attachments/assets/8a0158e4-5014-48ae-871e-b7d8d4219d39" />
<img width="1440" height="1024" alt="Image" src="https://github.com/user-attachments/assets/07083da6-d508-4cff-831a-df4dcfa4f56e" />
<img width="1440" height="1024" alt="Image" src="https://github.com/user-attachments/assets/1d551dea-088f-4ace-9731-a5e141d4bb95" />
<img width="1440" height="1024" alt="Image" src="https://github.com/user-attachments/assets/c686a8f5-d3bb-4f9c-983b-64742bcf1961" />
<img width="1440" height="1024" alt="Image" src="https://github.com/user-attachments/assets/e63db330-82eb-4463-b92c-a3752d68ef6f" />

###### Project Structure
 ```
├── HELP.md
├── README.md
├── Version2.md
├── build-context.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── na
│   │   │           └── postmortemproject
│   │   │               ├── PostmortemprojectApplication.java
│   │   │               ├── controller
│   │   │               │   └── UploadController.java
│   │   │               ├── entity
│   │   │               │   └── Timeline.java
│   │   │               ├── parser
│   │   │               │   ├── CsvParser.java
│   │   │               │   ├── FileParser.java
│   │   │               │   ├── JsonParser.java
│   │   │               │   ├── LogTxtParser.java
│   │   │               │   ├── ParserFactory.java
│   │   │               │   └── dto
│   │   │               │       └── ParseResult.java
│   │   │               ├── repository
│   │   │               │   └── TimelineRepository.java
│   │   │               ├── service
│   │   │               │   ├── FileStorageService.java
│   │   │               │   ├── ParseUploadException.java
│   │   │               │   ├── ParseUploadService.java
│   │   │               │   └── TextReducerService.java
│   │   │               └── util
│   │   │                   └── ContentTypeMapper.java
│   │   └── resources
│   │       ├── application-dev.properties
│   │       ├── application-dev.properties.template
│   │       ├── application-test.properties
│   │       ├── application.properties
│   │       ├── static
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── na
│                   └── postmortemproject
│                       ├── PostmortemprojectApplicationTests.java
│                       ├── parser
│                       │   ├── CsvParserTEST.java
│                       │   ├── JsonParserTest.java
│                       │   ├── LogTxtParserTEST.java
│                       │   └── ParserFactoryTEST.java
│                       ├── repository
│                       │   └── TimelineRepositoryTest.java
│                       └── service
│                           ├── TextReducerServiceCompressAggressiveTEST.java
│                           ├── TextReducerServiceCompressNoneTEST.java
│                           └── TextReducerServiceTEST.java
└── target
    ├── classes
    │   ├── application-dev.properties
    │   ├── application-dev.properties.template
    │   ├── application-test.properties
    │   ├── application.properties
    │   └── com
    │       └── na
    │           └── postmortemproject
    │               ├── PostmortemprojectApplication.class
    │               ├── controller
    │               │   ├── UploadController$ErrorResponse.class
    │               │   ├── UploadController$UploadResponse.class
    │               │   └── UploadController.class
    │               ├── entity
    │               │   └── Timeline.class
    │               ├── parser
    │               │   ├── CsvParser.class
    │               │   ├── FileParser.class
    │               │   ├── JsonParser.class
    │               │   ├── LogTxtParser.class
    │               │   ├── ParserFactory.class
    │               │   └── dto
    │               │       └── ParseResult.class
    │               ├── repository
    │               │   └── TimelineRepository.class
    │               ├── service
    │               │   ├── FileStorageService.class
    │               │   ├── ParseUploadException.class
    │               │   ├── ParseUploadService.class
    │               │   └── TextReducerService.class
    │               └── util
    │                   └── ContentTypeMapper.class
    ├── generated-sources
    │   └── annotations
    ├── generated-test-sources
    │   └── test-annotations
    ├── maven-archiver
    │   └── pom.properties
    ├── maven-status
    │   └── maven-compiler-plugin
    │       ├── compile
    │       │   └── default-compile
    │       │       ├── createdFiles.lst
    │       │       └── inputFiles.lst
    │       └── testCompile
    │           └── default-testCompile
    │               ├── createdFiles.lst
    │               └── inputFiles.lst
    ├── postmortemproject-0.0.1-SNAPSHOT.jar
    ├── postmortemproject-0.0.1-SNAPSHOT.jar.original
    ├── surefire-reports
    │   ├── 2026-02-02T19-44-02_059-jvmRun1-commands.bin
    │   ├── 2026-02-02T19-44-02_059-jvmRun1-events.bin
    │   ├── 2026-02-02T19-55-59_759-jvmRun1-commands.bin
    │   ├── 2026-02-02T19-55-59_759-jvmRun1-events.bin
    │   ├── 2026-02-02T19-59-52_972-jvmRun1-commands.bin
    │   ├── 2026-02-02T19-59-52_972-jvmRun1-events.bin
    │   ├── 2026-02-02T20-00-43_282-jvmRun1-commands.bin
    │   ├── 2026-02-02T20-00-43_282-jvmRun1-events.bin
    │   ├── TEST-com.na.postmortemproject.PostmortemprojectApplicationTests.xml
    │   ├── TEST-com.na.postmortemproject.parser.JsonParserTest.xml
    │   ├── TEST-com.na.postmortemproject.repository.TimelineRepositoryTest.xml
    │   ├── com.na.postmortemproject.PostmortemprojectApplicationTests.txt
    │   ├── com.na.postmortemproject.parser.JsonParserTest.txt
    │   └── com.na.postmortemproject.repository.TimelineRepositoryTest.txt
    └── test-classes
        └── com
            └── na
                └── postmortemproject
                    ├── PostmortemprojectApplicationTests.class
                    ├── parser
                    │   ├── CsvParserTEST.class
                    │   ├── JsonParserTest.class
                    │   ├── LogTxtParserTEST.class
                    │   └── ParserFactoryTEST.class
                    ├── repository
                    │   └── TimelineRepositoryTest.class
                    └── service
                        ├── TextReducerServiceCompressAggressiveTEST.class
                        ├── TextReducerServiceCompressNoneTEST.class
                        └── TextReducerServiceTEST.class
```

Spring Boot application with strategy pattern for multi-format file parsing, service layer orchestration, and comprehensive test coverage.



