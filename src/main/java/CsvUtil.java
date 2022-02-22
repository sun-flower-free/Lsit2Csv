import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author zhangyuhan
 */
public class CsvUtil {

    /**
     * 根据List<实体类>生成为CSV文件
     * 利用反射获得类中变量值，并将变量值写入到csv
     * @param exportData 源数据List
     * @param map csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName 文件名称
     * @return 返回文件
     */
    public static File createCSVFile(List exportData,
                                     LinkedHashMap map,
                                     String outPutPath,
                                     String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            // 定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv",
                    new File(outPutPath));
            System.out.println("csvFile：" + csvFile);
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(csvFile), "GBK"), 1024);
            System.out.println("csvFileOutputStream：" + csvFileOutputStream);
            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
                    .hasNext();) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
                        .next();
                csvFileOutputStream
                        .write((String) propertyEntry.getValue() != null ? new String(
                                ((String) propertyEntry.getValue())
                                        .getBytes("GBK"), "GBK") : "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
                System.out.println(new String(((String) propertyEntry
                        .getValue()).getBytes("GBK"), "GBK"));
            }
            csvFileOutputStream.write("\r\n");
            // =========写入文件内容============
            for (int j = 0; exportData != null
                            && !exportData.isEmpty()
                            && j < exportData.size(); j++) {
                Class clazz = exportData.get(j).getClass();
                Field[] fields = clazz.getDeclaredFields();
                String[] contents = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    String methodName = getMethodName(fields[i].getName());
                    Method method = clazz.getMethod(methodName);
                    method.setAccessible(true);
                    Object obj = method.invoke(exportData.get(j));
                    String str = String.valueOf(obj);
                    if (str == null || str.equals("null"))
                        str = "";
                    contents[i] = str;
                }
                for (String content : contents) {
                    // 将生成的单元格添加到工作表中
                    csvFileOutputStream.write(content);
                    csvFileOutputStream.write(",");
                }
                csvFileOutputStream.write("\r\n");
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 将第一个字母转换为大写字母并和get拼合成方法
     * @param origin 成员变量名称
     * @return 获取get方法字符串
     */
    private static String getMethodName(String origin) {
        StringBuffer sb = new StringBuffer(origin);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, "get");
        return sb.toString();
    }

    /**
     * 删除该目录filePath下的所有文件
     * @param filePath 文件目录路径
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 删除单个文件
     * @param filePath 文件目录路径
     * @param fileName 文件名称
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (files[i].getName().equals(fileName)) {
                        files[i].delete();
                        return;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        // =======改成list的格式，支持Arraylist传入实体类，改造的方法, TestBean为测试实体类============
        ArrayList<TestBean> beans = new ArrayList<>();
        TestBean bean1 = new TestBean();
        bean1.setId("123");
        bean1.setNumber("2022rz0001");

        TestBean bean2 = new TestBean();
        bean2.setId("456");
        bean2.setNumber("2022rz0002");

        beans.add(bean1);
        beans.add(bean2);

        LinkedHashMap map = new LinkedHashMap();
        map.put("1", "第一列");
        map.put("2", "第二列");
        map.put("3", "第三列");
        map.put("4", "第四列");

        String path = "C:\\Users\\zhangyuhan\\Desktop";
        String fileName = "文件导出";
        File file = CsvUtil.createCSVFile(beans, map, path,
                fileName);
        String fileName2 = file.getName();
        System.out.println("文件名称：" + fileName2);
    }


}
