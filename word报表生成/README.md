XDocReport Samples
==================
### XdocReport的基本概念
XdocReport是模块化、轻量级基本用于XML的文档报告，对word文档的操作，java API合并MS OFFICE创建XML文档(docx、ppt)或OPENOFFICE(ODT),LibreOffice(ODT)与Java模型生成报告并转换为PDF、xhtml等形式。XdocReport基于Apache POI的应用，而poi提供API Java操作MS WORD、MS EXCEL等文件，主要的结构：  
- HSSF － 提供读写Microsoft Excel XLS格式档案的功能。  
- XSSF － 提供读写Microsoft Excel OOXML XLSX格式档案的功能。  
- **HWPF － 提供读写Microsoft Word DOC格式档案的功能（MS WORD2007版本之前）。**
- **XWPF － 提供读写Microsoft Word DOC格式档案的功能（MS WORD2007以及2007版本以上）。** 
- HSLF － 提供读写Microsoft PowerPoint格式档案的功能。  
- HDGF － 提供读Microsoft Visio格式档案的功能。  
- HPBF － 提供读Microsoft Publisher格式档案的功能。  
- HSMF － 提供读Microsoft Outlook格式档案的功能。  

### XdocReport对MS WORD的操作，以下都是2007版本及以上  
1. 不采用Java对象模型对docx文件的操作。  
2. 应用FreeMark语法并采用Java对象模型对docx文件的操作。  
3. 应用Velocity语法并采用Java对象模型对docx文件的操作。  
4. 在web端应该XdocReport对docx文件的操作。  

[![Build Status](https://secure.travis-ci.org/opensagres/xdocreport.samples.png)](http://travis-ci.org/opensagres/xdocreport.samples)
