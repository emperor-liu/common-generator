generator提供了一个自动化生成web项目中orm映射文件的功能，可生成mybatis.xml dao do等文件，减少不必要的代码编写。
目前数据源支持：Mysql、Oracle；
暂时没时间提供构建脚本，可自己编译为maven项目导入eclipse中即可；
test下有执行方法

    public static void main(String[] args) throws SQLException {

        String dateSourceUrl = "jdbc:mysql://127.0.0.1:3306/test";
        String userName = "root";
        String password = "root";
        String dbType = "mysql";
        String dbName = "test";
        String filePackage = "com.lljqiu.test";
        String sequenceName = "a";
        List<TableList> tableList = null;
        SourceConfig sourceConfig = new SourceConfig(dateSourceUrl, userName,
                password, dbName, dbType, tableList, filePackage, sequenceName);
        DateObjectMaker dateObjectMaker = new DateObjectMaker();
        dateObjectMaker.createConfig(sourceConfig);
    }
