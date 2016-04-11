package works.tonny.mobile.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.*;

import works.tonny.mobile.widget.IDLinkedHashMap;

/**
 * Created by tonny on 2016/3/22.
 */
public class XMLParser2 {
    public Map<String, Object> datas = new LinkedHashMap();

    private Node node;

    private void o(Object o) {
        System.out.println(o);
    }


    public List<Object> getList(String key) {
        return (List<Object>) get(datas, key, true);
    }


    public Object get(String key) {
        return get(datas, key, false);
    }

    private Object toList(Object o, boolean asList) {
        if (!asList || o instanceof List) {
            return o;
        }
        List<Object> list = new ArrayList<Object>();
        list.add(o);
        return list;
    }

    private Object get(Map<String, Object> node, String key, boolean asList) {
        String tag = StringUtils.substringBefore(key, ".");
        String childTag = StringUtils.substringAfter(key, ".");
        String cu = null;
        int index = -1;
        if (tag.matches("^\\w+\\[\\d+\\]$")) {
            cu = StringUtils.substringBefore(tag, "[");
            index = NumberUtils.toInt(StringUtils.substringBetween(tag, "[", "]"));
        } else {
            cu = tag;
        }

        Object o = node.get(cu);
        if (o == null) {
            return null;
        }

        if (o instanceof List) {
            List<Object> list = (List<Object>) o;
//            o("///"+index);
            if (StringUtils.isEmpty(childTag) && index < 0) {
                return list;
            } else if (StringUtils.isEmpty(childTag)) {
                return toList(list.get(index), asList);
            } else if (index < 0) {
                o = list.get(0);
            } else {
                o = list.get(index);
//                o("//"+o);
            }
        }

        if (StringUtils.isNotEmpty(childTag) && !(o instanceof Map)) {
            return null;
        } else if (StringUtils.isNotEmpty(childTag)) {
            return get((Map<String, Object>) o, childTag, asList);
        } else {
            return toList(o, asList);
        }
    }


    /**
     * 解析
     *
     * @param xml
     * @throws Exception
     */
    public void parse(String xml) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
            Element root = document.getDocumentElement();
            node = new Node();
            node.name = root.getTagName();
            node.depth = 0;
            listChildren(root, node);
            rebuild(node);
            datas.put(node.name, node.mapChild);
//            mappping(node);
            Set<String> strings = datas.keySet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listChildren(Element element, Node parent) {
        NodeList childNodes = element.getChildNodes();
        o(StringUtils.leftPad("", parent.depth + 1, "\t") + "开始:" + element.getTagName());
        boolean hasChildren = false;
        for (int i = 0; i < childNodes.getLength(); i++) {
            org.w3c.dom.Node c = childNodes.item(i);
            if (!(c instanceof Element)) {
//                o("非节点");
                continue;
            }
            o(StringUtils.leftPad("", parent.depth + 1, "\t") + "子节点:" + c.getNodeName());
            hasChildren = true;
            Element item = (Element) c;
            Node node = new Node();
            node.name = item.getTagName();
            node.depth = parent.depth + 1;
            node.setParent(parent);
            NamedNodeMap attributes = item.getAttributes();
            for (int j = 0; j < attributes.getLength(); j++) {
                node.attributes.put(attributes.item(j).getNodeName(), attributes.item(j).getTextContent().trim());
            }
            listChildren(item, node);
        }
        if (!hasChildren) {
            parent.text = element.getTextContent().trim();
            o(StringUtils.leftPad("", parent.depth + 1, "\t") + element.getNodeName() + " text " + parent.text);
        }
    }


    /**
     * 重构数据
     *
     * @param n
     */
    private void rebuild(Node n) {
        if (n.children != null) {
            for (int i = 0; i < n.children.size(); i++) {
                rebuild(n.children.get(i));
            }
        }
        n.rebuild();

    }


    public class Node implements Serializable {
        Node parent;
        List<Node> children;
        String name;
        String text;
        int depth;
        int index;
        Node child;
        IDLinkedHashMap mapChild = new IDLinkedHashMap();
        Map<String, String> attributes = new HashMap<String, String>();


        public void setParent(Node parent) {
            this.parent = parent;
            if (parent.children == null) {
                parent.children = new ArrayList<Node>();
            }
            this.index = parent.children.size();
            parent.children.add(this);
        }


        public void rebuild() {
            if (!attributes.isEmpty()) {
                Set<String> keySet = attributes.keySet();
                for (String s : keySet) {
                    mapChild.put(s, attributes.get(s));
                    o(name + " add attr " + s + "=" + attributes.get(s));
                }
            }
            if (children == null && mapChild.isEmpty()) {
                o(parent.name + " add child " + name + " " + text);
                index = parent.put(name, text);
//                Log.info("put in text " + name);
//                datas.put(getPath(), text);
                return;
            }

            if (!mapChild.isEmpty() && parent != null) {
                index = parent.put(name, this.mapChild);
//                o(getPath() + "=" + mapChild);
//                datas.put(getPath(), this.mapChild);
                this.children = null;
            }
        }


        public int put(String name, Object o) {
            if (!mapChild.containsKey(name)) {
                mapChild.put(name, o);
                return -1;
            } else {
                Object e = mapChild.get(name);
                if (e instanceof List) {
                    ((List) e).add(o);
                    return ((List) e).size() - 1;
                } else {
                    ArrayList list = new ArrayList();
                    list.add(e);
                    list.add(o);
                    mapChild.put(name, list);
                    return list.size() - 1;
                }
            }

        }

    }
}
