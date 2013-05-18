rem 
rem %1  zim 文档所在目录
rem %2  导出的html文档存放的路径
rem %3 zim home




set ZIM_HOME=%3

set ZIM_EXE_COMMAND=%ZIM_HOME%/zim.exe
set ZIM_TEMPLATE=%ZIM_HOME%/data/templates/html/Default.html

set DOCUMENT_ROOT_DIR=%1

set OUTPUT_DIR=%2

cd %DOCUMENT_ROOT_DIR%

%ZIM_EXE_COMMAND% --export --output=%OUTPUT_DIR% --format=html --template=%ZIM_TEMPLATE% .