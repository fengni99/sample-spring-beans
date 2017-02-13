/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.discovery.darchrow.test.appcontext;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import com.discovery.darchrow.test.bean.MyBeans;

public class MyClassPathXmlApplicationContext {
	 // 装载实例化bean

    private Map<String, Object> beanMap = new HashMap<String, Object>();

    // 装载配置文件的属性和值

    private List<MyBeans> beanlist = new ArrayList<MyBeans>();

   

    public MyClassPathXmlApplicationContext(String filename) {

           //第一步，解析spring配置文件

           readXml(filename);

           //第二步，通过反射，实例化所有注入bean

           initBeans();

    }



    /**

     * 通过反射机制，初始化配置文件中的bean

     */

    private void initBeans() {

           for (MyBeans bean : beanlist) {

                  try {

                         if (bean.getClassName() != null && !"".equals(bean.getClassName())) {

                                beanMap.put(bean.getId(), Class.forName(bean.getClassName()).newInstance());

                         }

                  } catch (Exception e) {

                         e.printStackTrace();

                  }

           }

    }



    /**

     * 解析配置文件，把解析后的bean设置到实体中，并保持到list

     *

     * @param filename

     */

    private void readXml(String filename) {

           SAXReader reader = new SAXReader();



           Document doc = null;

           URL xmlpath = this.getClass().getClassLoader().getResource(filename);

           try {

                  Map<String, String> nsMap = new HashMap<String, String>();

                  nsMap.put("ns", "http://www.springframework.org/schema/beans");

                  doc = reader.read(xmlpath);

                  XPath xpath = doc.createXPath("//ns:beans//ns:bean");// 创建//ns:beans//ns:bean查询路径

                  xpath.setNamespaceURIs(nsMap);// 设置命名空间

                  List<Element> eles = xpath.selectNodes(doc);// 取得文档下所有节点

                  for (Element element : eles) {

                         String id = element.attributeValue("id");

                         String cn = element.attributeValue("class");

                         //自定义实体bean，保存配置文件中id和class

                         MyBeans beans = new MyBeans(id, cn);

                         beanlist.add(beans);

                  }

           } catch (Exception e) {

                  e.printStackTrace();

           }



    }



    public Object getBean(String beanId) {

           return beanMap.get(beanId);

    }
}
